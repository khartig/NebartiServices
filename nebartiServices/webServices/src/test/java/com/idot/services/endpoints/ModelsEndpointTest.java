/*
 * Nebarti
 * Copyright 2013 All rights reserved.
 */
package com.idot.services.endpoints;

import com.nebarti.dataaccess.domain.ClassifiedText;
import com.nebarti.dataaccess.domain.Model;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * 
 */
public class ModelsEndpointTest {

    public static final Logger logger = Logger.getLogger(ModelsEndpointTest.class.getName());

    @BeforeClass
    public void setUp() {
        // code that will be invoked before this test starts
    }

    @Test
    public void createTest() {

//        try {
//            Client client = Client.create();
////            client.setFollowRedirects(true);
//            WebResource webResource = client.resource("http://localhost:8080/services/models");
//
//            Model model = new Model();
//            model.setName("movie_reviews");
//            model.setVisibleName("Movie Reviews");
//            model.setCategory("movies");
//            model.setClassifier("pnn");
//            model.setOwner("public");
//
//            MultivaluedMap formData = new MultivaluedMapImpl();
//            formData.add("name", "movie_reviews");
//            formData.add("visibleName", "Movie Reviews");
//            formData.add("category", "sports");
//            formData.add("classifier", "pnn");
//            formData.add("owner", "public");
//            ClientResponse response = webResource.type("application/x-www-form-urlencoded").post(ClientResponse.class, formData);
//
//            if (response.getStatus() != 201) { // 201 means successful create
//                throw new RuntimeException("Failed : HTTP error code : "
//                        + response.getStatus());
//            }
//
////            String output = response.getEntity(String.class);      
//            String output = response.getLocation().toASCIIString();
//            URI uri = response.getLocation();
//
//            logger.info("Output from Server .... \n" + uri.toASCIIString());
//
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
//        }
    }

    @Test
    public void deleteTest() {
//        Client client = Client.create();
//        client.setFollowRedirects(true);
//        WebResource webResource = client.resource("http://localhost:8080/services/models");
//
//        ClientResponse response = webResource.path("test_model").accept(MediaType.APPLICATION_JSON).delete(ClientResponse.class);
//
//        if (response.getStatus() != 200) {
//            throw new RuntimeException("Failed : HTTP error code : "
//                    + response.getStatus());
//        }
//
//        logger.info("Output from Server .... \n" + response.getEntity(String.class));

    }
    
    @Test
    public void getByIdsTest() {
        /**
        Client client = Client.create();
        client.setFollowRedirects(true);
        WebResource webResource = client.resource("http://localhost:8080/services/documents/barack_obama/");

        List<String> ids = new ArrayList<String>();
        ids.add("50453524036483d9b3025fd9");
        
        List<ClassifiedText> response = webResource.
                accept(MediaType.APPLICATION_JSON).post(List.class, ids);

        for (ClassifiedText classifiedText : response) {
            logger.info(classifiedText.toString());
        }
       */
    }
    
    @Test
    public void saveClassifiedText() throws JAXBException {
//        Client client = Client.create();
//        client.setFollowRedirects(true);
//        WebResource webResource = client.resource("http://localhost:8080/services/models");
//        
//        ClassifiedText classifiedText = new ClassifiedText();
//        classifiedText.setModelName("test_model");
//        classifiedText.setAge("32");
//        classifiedText.setClassification("neutral");
//        classifiedText.setGender("M");
//        classifiedText.setLatitude("0");
//        classifiedText.setLongitude("0");
//        classifiedText.setText("test text");
//        classifiedText.setUseInModel(Boolean.TRUE);
//        classifiedText.setValidated(Boolean.TRUE);
//
//        ClientResponse response = webResource
//                .path("classifytext")
//                .type(MediaType.APPLICATION_XML)
//                .accept(MediaType.APPLICATION_JSON)
//                .post(ClientResponse.class, classifiedText);
//        
//        if (response.getStatus() != 200) { // 201 means successful create
//            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
//        }
//
////            String output = response.getEntity(String.class);      
//        String output = response.getLocation().toASCIIString();
//        URI uri = response.getLocation();
//
//        logger.info("Output from Server .... \n" + uri.toASCIIString());
    }

    @AfterClass
    public void cleanUp() {
        // code that will be invoked after this test ends
    }
}
