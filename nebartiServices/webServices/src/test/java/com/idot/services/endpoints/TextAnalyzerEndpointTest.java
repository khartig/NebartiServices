/*
 * Nebarti
 * Copyright 2013 All rights reserved.
 */
package com.idot.services.endpoints;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MultivaluedMap;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 *
 */
public class TextAnalyzerEndpointTest {
    public static final Logger logger = Logger.getLogger(TextAnalyzerEndpointTest.class.getName());
    
    @BeforeClass
    public void setUp() {
        // code that will be invoked before this test starts
    }
    
    @Test
    public void categorizeTest() {
        /**
        try {
            Client client = Client.create();
//            WebResource webResource = client.resource("http://localhost:8080/services/textanalyzer/categorize?text=\"test text\"");
//            ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
            
            MultivaluedMap formData = new MultivaluedMapImpl();
            formData.add("reviewText", "Yall ready for this dive off the fiscal cliff ...didnt think so");
            formData.add("model", "fiscal_cliff");
            WebResource webResource = client.resource("http://localhost:8080/services/textanalyzer/categorize");
            ClientResponse response = webResource.type("application/x-www-form-urlencoded").post(ClientResponse.class, formData);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            String json = response.getEntity(String.class);
            String classification = json.substring(json.indexOf(":") + 2, json.lastIndexOf("\","));
            String score = json.substring(json.indexOf("score\":") + 8, json.lastIndexOf("\"}"));

            logger.info("Output from Server .... \n");
            logger.log(Level.INFO, "classification = {0}", classification);
            logger.log(Level.INFO, "score = {0}", score);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        * */
    }
    
    @AfterClass
    public void cleanUp() {
        // code that will be invoked after this test ends
    }
}


