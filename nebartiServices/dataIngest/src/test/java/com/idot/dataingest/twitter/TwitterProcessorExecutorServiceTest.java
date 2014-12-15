/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.idot.dataingest.twitter;

import com.idot.dataingest.processors.Processor;
import com.idot.dataingest.processors.TwitterProcessor;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 */
public class TwitterProcessorExecutorServiceTest {
    
    @BeforeClass
    public void setUp() {
        // code that will be invoked before this test starts
    }
    
    @Test
    public void serviceTest() {
//        String processorName = "Test Processor";
//        TwitterProcessorExecutorService service = new TwitterProcessorExecutorService();
//        String[] track = {"MacBook Pro"};
//        Processor processor = new TwitterProcessor("id_1", processorName, processorName, track);
//        processor.setName(processorName);
//        service.startProcessor(processor);
//        
//        try {
//            Thread.sleep(30 * 1000);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(TwitterProcessorExecutorServiceTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        service.stopProcessor(processorName);
//        service.shutdown();
    }
    
    @AfterClass
    public void cleanUp() {
        // code that will be invoked after this test ends
    }
}
