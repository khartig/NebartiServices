/*
 * NEbarti
 * Copyright © 2012. All rights reserved.
 */
package com.idot.dataingest.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

/**
 * This class exists so the stop words file can be statically loaded once 
 * at the initialization of the book reviews service. It needs to use the 
 * class loader to get the stop words file.
 * 
 */
public class StopWords {
    String stopWords;
    public static final Logger logger = Logger.getLogger(StopWords.class.getName());
    
    public StopWords() {
        try {
            Properties properties = new Properties();
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("stop_words.txt");
            stopWords = IOUtils.toString(inputStream, "UTF-8");
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }
    
    public Set<String> getwords() {
        Scanner ignoreScanner = new Scanner(stopWords);
        Set<String> words = new HashSet<String>();
        ignoreScanner.useDelimiter("[^A-Za-z!-~]+");

        while (ignoreScanner.hasNext()) {
            words.add(ignoreScanner.next());
        }
        
        return words;
    }
    
    @Override
    public String toString() {
        return stopWords;
    }
}
