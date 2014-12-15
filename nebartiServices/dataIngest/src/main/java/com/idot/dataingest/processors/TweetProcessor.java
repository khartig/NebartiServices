/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.idot.dataingest.processors;

import com.idot.dataingest.twitter.listeners.ClientStatusListener;
import com.mongodb.BasicDBObject;
import com.mongodb.WriteResult;
import com.nebarti.dataaccess.dao.GenericDao;
import com.nebarti.dataaccess.domain.Classifier;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MultivaluedMap;
import twitter4j.Status;

/**
 *
 */
class TweetProcessor implements Runnable {
    Boolean running = true;
    
    private Set<Classifier> classifierSet = new HashSet<Classifier>();
    private String classifierName = ""; // matches the db name used in Mongo to store the classified text
    private static final String webServicesLocation;
    private static final com.idot.dataingest.utilities.Properties properties = new com.idot.dataingest.utilities.Properties();
    public static final Logger logger = Logger.getLogger(TweetProcessor.class.getName());

    static {
        if (properties.getProperty("web.services.location") != null) {
            webServicesLocation = properties.getProperty("web.services.location");
        } else {
            webServicesLocation = "http://localhost:8080/";
        }
    }
        
    public void stop() {
        running = false;
    }
    
    public void addFilterQuery(Classifier classifier) {
        classifierSet.add(classifier);
    }
    
    public void removeFilterQuery(Classifier classifier) {
        classifierSet.remove(classifier);
    }
    
    @Override
    public void run() {
        while(running) {
            try {
                Status status = ClientStatusListener.tweetQueue.take();
                handleStatus(status);
            } catch (InterruptedException ix) {
                logger.log(Level.SEVERE, ix.getLocalizedMessage(), ix);
            }
        }
    }

    /**
     * 
     */
    private void handleStatus(Status status) {
        Set<Classifier> classifiers = findMatchingClassifiers(status.getText());
        Iterator itr = classifiers.iterator();
        Classifier classifier;
        
        while (itr.hasNext()) {
            classifier = (Classifier) itr.next();
            
            try {

                Client client = Client.create();

                MultivaluedMap formData = new MultivaluedMapImpl();
                formData.add("reviewText", status.getText());
//            formData.add("model", "polarity");
                formData.add("model", classifier.getModel());
                WebResource webResource = client.resource(webServicesLocation + "services/textanalyzer/categorize");
                ClientResponse response = webResource.type("application/x-www-form-urlencoded").post(ClientResponse.class, formData);

                if (response.getStatus() != 200) {
                    throw new RuntimeException("Failed to analyze text at " + webResource.toString() + ": HTTP error code : " + response.getStatus());
                }

                String json = response.getEntity(String.class);
                String classification = json.substring(json.indexOf(":") + 2, json.lastIndexOf("\","));
                String score = json.substring(json.indexOf("score\":") + 8, json.lastIndexOf("\"}"));
            
//                logger.log(Level.INFO, "Classification from Server for text \n{0}\n", classification);
                if (classification.equals("Error")) {
                    return;
                }

                // store the classified text
//            String date = new Date().toString();
                SimpleDateFormat dateFormatGmt = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
                String date = dateFormatGmt.format(new Date());

                BasicDBObject dbo = new BasicDBObject();

                dbo.put("createdDate", date);
                dbo.put("updatedDate", date);
                dbo.put("text", status.getText());
                dbo.put("classification", classification);
                dbo.put("score", score);
                dbo.put("useInModel", false);
                dbo.put("validated", false);
                if (status.getGeoLocation() != null) {
                    dbo.put("latitude", status.getGeoLocation().getLatitude());
                    dbo.put("longitude", status.getGeoLocation().getLongitude());
//                    logger.log(Level.INFO, "Found geolocation = Lat: {0}, Lon: {1}", 
//                            new Object[]{status.getGeoLocation().getLatitude(), status.getGeoLocation().getLongitude()});
                } else {
                    dbo.put("latitude", 0.0);
                    dbo.put("longitude", 0.0);
                }
                dbo.put("gender", "");
                dbo.put("age", "");

                WriteResult result = GenericDao.save(classifier.getName(), "sentiment", dbo);
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }      
    }
    
    /**
     * Looking for filter strings from classifiers that match 
     * @param text
     * @return 
     */
    private Set<Classifier> findMatchingClassifiers(String text) {
        Set<Classifier> classifiers = new HashSet<Classifier>();
        Iterator itr = classifierSet.iterator();

        Classifier classifier;
        while (itr.hasNext()) {
            String[] tokens;
            classifier = (Classifier) itr.next();
            if (classifier.getFilter().contains("\"")) {
                tokens = new String[] { classifier.getFilter().replace("\"", "") };
            } else {
                tokens = classifier.getFilter().split(" ");
            }
            List<String> tokenList = Arrays.asList(tokens);

            for (String token : tokenList) {
                if (text.toLowerCase().contains(token.toLowerCase())) {
                    classifiers.add(classifier);
                }
            }
        }
        return classifiers;
    }
}
