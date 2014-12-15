/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idot.twitter.textprocessing;

import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author khartig
 */
public class TweetPreprocessorTest {
    public static final Logger logger = Logger.getLogger(TweetPreprocessorTest.class.getName());
    
    public TweetPreprocessorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testRemoveURLs() {
        logger.info("removeURLs");
        
        String tweetString = "Test cleanse of url at end of string http://t.co/XjYKNSL";
        String expResult = "Test cleanse of url at end of string ";
        assertEquals(expResult, expResult);
        
        tweetString = "Test http://t.co/XjYKNSL cleanse of multiple urls in string http://t.co/XjYKNSL";
        expResult = "Test cleanse of multiple urls in string ";
        assertEquals(expResult, expResult);
    }
    
    
    /**
     * Test of removeHashes method, of class TweetPreprocessor.
     */
    @Test
    public void testRemoveHashes() {
        logger.info("removeHashes");
        String tweetString = "Test cleanse of #hashmarks in #string";
        String expResult = "Test cleanse of hashmarks in string";
        String result = TweetPreprocessor.removeHashes(tweetString);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of removeRTs method, of class TweetPreprocessor.
     */
    @Test
    public void testRemoveRTs() {
        logger.info("removeRTs");
        String tweetString = "Test cleanse of RT in string";
        String expResult = "Test cleanse of in string";
        String result = TweetPreprocessor.removeRTs(tweetString);
        assertEquals(expResult, result);
        
        tweetString = "Test cleanse of RT: in string";
        expResult = "Test cleanse of in string";
        result = TweetPreprocessor.removeRTs(tweetString);
        assertEquals(expResult, result);
    } 
    
    /**
     * Test of removeNames method, of class TweetPreprocessor.
     */
    @Test
    public void testRemoveNames() {
        logger.info("removeNames");
        String tweetString = "Test removing @names";
        String expResult = "Test removing ";
        String result = TweetPreprocessor.removeNames(tweetString);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of removeMultiSpaces method, of class TweetPreprocessor.
     */
    @Test
    public void testFixSpaces() {
        logger.info("fixSpaces");
        // string contains spaces and tabs
        String tweetString = "   Test   removing   multiple     spaces";
        String expResult = "Test removing multiple spaces";
        String result = TweetPreprocessor.fixSpaces(tweetString);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of addTerminator method, of class TweetPreprocessor.
     */
    @Test
    public void testAddTerminator() {
        logger.info("addTerminator");
        String tweetString = "Test adding terminator";
        String expResult = "Test adding terminator .";
        String result = TweetPreprocessor.addTerminator(tweetString);
        assertEquals(expResult, result);
        
        tweetString = "Test adding terminator.";
        expResult = "Test adding terminator .";
        result = TweetPreprocessor.addTerminator(tweetString);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of prepare method, of class TweetPreprocessor.
     */
    @Test
    public void testPrepare() {
        logger.info("prepare");
   
        String tweetString = "@heyyou RT: @someName Test #cleanse  of all text http://t.co/XjYKNSL";
        String expResult = "Test cleanse of all text .";
        String result = TweetPreprocessor.prepare(tweetString);
        assertEquals(expResult, result);
    }

}
