/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.idot.dataingest.tasks;

import com.idot.dataingest.exceptions.ModelCreationException;
import com.idot.dataingest.processors.TwitterProcessor;
import com.idot.dataingest.twitter.TwitterProcessorExecutorService;
import com.nebarti.dataaccess.domain.Classifier;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MultivaluedMap;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 *
 */
public class StateChangeWatcher implements Runnable {

    TwitterProcessorExecutorService twitterProcessorExecutorService;
    MongoTemplate mongoTemplate;
    String collectionName = "classifiers";
    List<Classifier> currentClassifiers = new ArrayList<Classifier>(); // classifier data freshly pulled from db
    List<Classifier> cachedClassifiers = new ArrayList<Classifier>(); //classfier data cached from previous db read
    TwitterProcessor processor;
    private static String webServicesLocation;
    private static final com.idot.dataingest.utilities.Properties properties = new com.idot.dataingest.utilities.Properties();
    public static final Logger logger = Logger.getLogger(StateChangeWatcher.class.getName());
    
    static {
        if (properties.getProperty("web.services.location") != null) {
            webServicesLocation = properties.getProperty("web.services.location"); 
        } else {
            webServicesLocation = "http://localhost:8080/";
        }
    }

    public StateChangeWatcher() {
        ApplicationContext ctx = new GenericXmlApplicationContext("mongo-config.xml");
        mongoTemplate = (MongoTemplate) ctx.getBean("ConfigParamsTemplate");
        twitterProcessorExecutorService = new TwitterProcessorExecutorService();
        
        // start the processor thread to start ingesting data
        processor = new TwitterProcessor(UUID.randomUUID().toString(), "twitter processor");
        twitterProcessorExecutorService.startProcessor(processor);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                currentClassifiers.clear();
                currentClassifiers = getAll();
                checkClassifiers();
//                logger.log(Level.INFO, "Number of classifiers = {0}", currentClassifiers.size());
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            }
        }
        logger.info("State Change Watcher thread exiting");
    }

    private synchronized List<Classifier> getAll() {
        List<Classifier> classifiers = mongoTemplate.find(
                new Query(Criteria.where("_id").exists(true)),
                Classifier.class,
                "classifiers");

        return classifiers;
    }

    private void checkClassifiers() {
        for (Classifier classifier : currentClassifiers) {
            if (!cachedClassifiers.contains(classifier)) {
                if (classifier.getDataFeedSwitch()) {
                    addFilter(classifier);
                    logger.log(Level.INFO, "Start ingesting data for new classifier {0}.", classifier.getName());
                }
            } else {
                boolean isIngesting = processor.isFiltering(classifier);
                if (isIngesting && !classifier.getDataFeedSwitch()) {
                    removeFilter(classifier);
                    logger.log(Level.INFO, "Turn off existing data feed for classifier = {0}.", classifier.getName());
                } else if (!isIngesting && classifier.getDataFeedSwitch()) {
                    addFilter(classifier);
                    logger.log(Level.INFO, "Start ingesting data for existing classifier= {0}", classifier.getName());
                } else {
//                    logger.log(Level.INFO, "Classifier state = {0} has Not changed for classifier = {1}", new Object[]{classifier.getDataFeedSwitch(), classifier.getName()});
                }
            }
        }

        cachedClassifiers.clear();
        cachedClassifiers.addAll(currentClassifiers);
    }

    private void addFilter(Classifier classifier) {
        try {
            createNewModel(classifier); // create new model db if one doesn't exist already.
            processor.addFilter(classifier);
        } catch (ModelCreationException ex) {
            logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }

    private void removeFilter(Classifier classifier) {
        processor.removeFilter(classifier);
    }

    /**
     * 
     * 
     * @param classifier 
     */
    private void createNewModel(Classifier classifier) throws ModelCreationException {
        Client client = Client.create();
        WebResource webResource = client.resource(webServicesLocation + "services/models");

        MultivaluedMap formData = new MultivaluedMapImpl();
        formData.add("name", classifier.getName());
        formData.add("visibleName", classifier.getVisibleName());
        formData.add("category", classifier.getCategory());
        formData.add("classifier", classifier.getClassificationSet());
        formData.add("owner", classifier.getOwner());
        ClientResponse response = webResource.type("application/x-www-form-urlencoded").post(ClientResponse.class, formData);

        if (response.getStatus() != 201) { // 201 means successful create
//            throw new ModelCreationException("Model creation failed : HTTP error code : "
//                    + response.getStatus() + ". Probably due to model information already existing.");
            logger.log(Level.WARNING, 
                    "Model creation failed : HTTP error code : {0}. Probably due to model information already existing.", 
                    response.getStatus());
        } else {

//        String output = response.getEntity(String.class);
            String output = response.getLocation().toASCIIString();
            URI uri = response.getLocation();

            logger.info("Created new model database ... \n" + uri.toASCIIString());
        }
    }
}
