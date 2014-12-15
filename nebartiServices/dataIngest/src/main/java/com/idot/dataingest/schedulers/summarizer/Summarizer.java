/*
 * Nebarti
 * Copyright © 2012-2013 All rights reserved.
 */
package com.idot.dataingest.schedulers.summarizer;

import com.idot.dataingest.coreference.CoReferenceSummarizer;
import com.idot.dataingest.namedentities.NamedEntitySummarizer;
import com.idot.dataingest.references.ReferenceSummarizer;
import com.idot.dataingest.wordcount.WordCountSummarizer;
import com.nebarti.dataaccess.domain.Model;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 * Pulls text data stored for a topic and extracts entities to be stored in the 
 * model's db collections. The following entities are extracted:
 *     - word counts
 *     - 2 word co-references
 *     - 3 word co-references
 *     - famous people
 *     - images
 *     - popular web sites
 * 
 * The data stored in the collection is to eliminate processing each request from a
 * webapp to calculate the values. This program should be run on a periodic
 * basis for each data set currently ingesting data.
 */
public class Summarizer {
    private static final List<String> modelsnottouse = new ArrayList<String>();
    private static String webServicesLocation;
    private static final com.idot.dataingest.utilities.Properties properties = new com.idot.dataingest.utilities.Properties();
    
    static {
        // ToDo: add lsit of models in a config parameter
        modelsnottouse.add("config_params");
        modelsnottouse.add("book_reviews");
        modelsnottouse.add("movie_reviews");
        modelsnottouse.add("mitt_romney");
        modelsnottouse.add("gary_johnson");
        webServicesLocation = properties.getProperty("web.services.location");
        if (webServicesLocation == null) webServicesLocation = "http://localhost:8080/";
    }
    public static final Logger logger = Logger.getLogger(Summarizer.class.getName());

    private static List<Model> getModels() {
        ObjectMapper mapper = new ObjectMapper();
        Client client = Client.create();
        client.setFollowRedirects(true);
        WebResource webResource = client.resource(webServicesLocation + "services/configparams/models");

        try {
            String json = webResource.accept(MediaType.APPLICATION_JSON).get(String.class);
            List<Model> models = mapper.readValue(json, new TypeReference<List<Model>>() {});
            return models;
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getLocalizedMessage(), e);       
        }
        
        return new ArrayList<Model>();
    }
    
    
    private static void saveWordCounts(Model model, Integer limit) {
        WordCountSummarizer.saveWordCount(model.getName(), limit);
    }
    
    private static void saveCoRefs(Model model, Integer limit, int coRefCount, Boolean rebuild) {
// commented old web service call to do calculation for reference. Seems to kill the web server
//        ObjectMapper mapper = new ObjectMapper();
//        Client client = Client.create();
//        client.setFollowRedirects(true);
//        WebResource webResource = client.resource(webServicesLocation + "services/models/coreferences/" + model.getName() + "?count=" + count);
//        
//        List<CoReference> coReferences = new ArrayList<CoReference>();
//        try {
//            String json = webResource.accept(MediaType.APPLICATION_JSON).get(String.class);
//            coReferences = mapper.readValue(json, new TypeReference<List<CoReference>>() {});
//        } catch (IOException e) {
//            logger.log(Level.WARNING, e.getLocalizedMessage(), e);       
//        }
//        
//        GenericDao.setCoreferences(model.getName(), "coreferences", coReferences, rebuild);
        
        CoReferenceSummarizer.saveCoReferences(model.getName(), limit, coRefCount, rebuild);
    }
    
    private static void saveEntities(Model model, Integer limit) {
        NamedEntitySummarizer.saveEntities(model.getName(), limit);
    }    
    
    private static void saveReferences(Model model, Integer limit) {
        ReferenceSummarizer.saveReferences(model.getName(), limit);
    }
    
    public static void summarizeAndSave() {              
        // for each data set, calculate the values
        List<Model> models = getModels();
        for(Model model : models) {
            logger.log(Level.INFO, "Summarizing model = {0}", model.getName());
            if (!modelsnottouse.contains(model.getName())) {
                logger.log(Level.INFO, "Summarizing word counts for model = {0}", model.getName());
                saveWordCounts(model, 6000);
                logger.log(Level.INFO, "Summarizing 2 word corefs for model = {0}", model.getName());
                saveCoRefs(model, 6000, 2, true);
                logger.log(Level.INFO, "Summarizing 3 word corefs for model = {0}", model.getName());
                saveCoRefs(model, 6000, 3, false);
                logger.log(Level.INFO, "Summarizing entities for model = {0}", model.getName());
                saveEntities(model, 6000);
                logger.log(Level.INFO, "Summarizing image and web page references for model = {0}", model.getName());
                saveReferences(model, 1000);
            }
            logger.log(Level.INFO, "Finished summarizing model = {0}", model.getName());
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        summarizeAndSave();
        
        
        // testing
        Model model = new Model();
        model.setName("barack_obama");
        saveWordCounts(model, 6000);
        saveCoRefs(model, 6000, 2, true);
        saveCoRefs(model, 6000, 3, false);
        saveEntities(model, 0); // second parameter defines how many most recent documents to process. 0 = all.
        saveReferences(model, 1000);
    }

}
