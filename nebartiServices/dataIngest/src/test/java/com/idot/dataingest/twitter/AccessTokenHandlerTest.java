/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.idot.dataingest.twitter;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

/**
 *
 * 
 */
public class AccessTokenHandlerTest {
    public static final Logger logger = Logger.getLogger(AccessTokenHandlerTest.class.getName());
    
    @BeforeClass
    public void setUp() {
        // code that will be invoked before this test starts
    }
    
    @Test
    public void authTest() {
//        TwitterFactory factory = new TwitterFactory();       
//        Twitter twitter = factory.getInstance();
//        
//        AccessTokenHandler handler = new AccessTokenHandler(twitter);
//        twitter = handler.authorizeUser("Nebarti");
//        try {
//            User user = twitter.showUser("khartig");
//            Assert.assertEquals(10446732L, user.getId());
//        } catch (TwitterException e) {
//            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
//        }
    }
    
    @AfterClass
    public void cleanUp() {
        // code that will be invoked after this test ends
    }
}
