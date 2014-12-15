/*
 * Nebarti
 * Copyright 2013 All rights reserved.
 */
package com.idot.services.endpoints;

import com.nebarti.dataaccess.domain.MJText;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * 
 */
public class MJTextEndpointTest {

    public static final Logger logger = Logger.getLogger(MJTextEndpointTest.class.getName());

    @BeforeClass
    public void setUp() {
        // code that will be invoked before this test starts
    }

//    @Test
//    public void saveTest() {
//
//        try {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            JAXBContext jc = JAXBContext.newInstance(com.idot.domain.MJText.class);
//            Marshaller marshaller = jc.createMarshaller();
//
//            Client client = Client.create();
//            client.setFollowRedirects(true);
//            WebResource webResource = client.resource("http://localhost:8080/services/mjlegalization");
//
//            MJText mjtext = new MJText();
//            mjtext.setText("Test text");
//
//            marshaller.marshal(mjtext, baos);
//            String request = baos.toString();
//
//            ClientResponse response = webResource.accept("application/json").post(ClientResponse.class, mjtext);
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
//    }

//    @Test
//    public void getDocumentsTest() {
//        Client client = Client.create();
//        client.setFollowRedirects(true);
//        WebResource webResource = client.resource("http://localhost:8080/services/mjlegalization/neutral?offset=0&limit=10");
//
//        GenericType<List<MJText>> genericType = new GenericType<List<MJText>>() {};
//        List<MJText> response = webResource.get(genericType);
//        
//        logger.info("test");
//    }

    @AfterClass
    public void cleanUp() {
        // code that will be invoked after this test ends
    }
}
