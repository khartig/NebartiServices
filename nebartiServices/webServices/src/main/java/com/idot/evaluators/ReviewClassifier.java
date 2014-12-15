/*
 * Nebarti
 * Copyright 2013. All rights reserved.
 */
package com.idot.evaluators;

import com.aliasi.classify.LMClassifier;
import com.aliasi.classify.NaiveBayesClassifier;
import com.aliasi.classify.ScoredClassification;
import com.idot.utilities.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * 
 */
public abstract class ReviewClassifier {

//    protected JointClassifier<CharSequence> classifier;
    protected LMClassifier classifier;
    public static final Logger logger = Logger.getLogger(ReviewClassifier.class.getName());

    /**
     * Constructor to read in model file for classifier. The String filename
     * is prepended with the model.dir directory that defines the top level directory
     * in the .war webapp file.
     * 
     * @param modelFilename
     */
    public ReviewClassifier(String modelFilename) {
        Properties properties = new Properties();
        readModelFile(properties.getProperty("model.dir") + modelFilename);
    }
    
    public ReviewClassifier(File modelFile) {
        try {
            readModelFile(modelFile.getCanonicalPath());
        } catch (IOException ex) {
            logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }

    private void readModelFile(String modelFilename) {
        FileInputStream fileIn = null;
        ObjectInputStream objIn = null;

        try {
            logger.log(Level.INFO, "Reading Compiled Model from file={0}", modelFilename);
//            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(modelFilename);  // was used to load files from war file
            InputStream inputStream = new FileInputStream(new File(modelFilename));
            objIn = new ObjectInputStream(inputStream);
//            classifier = (JointClassifier<CharSequence>) objIn.readObject();
            classifier = (LMClassifier) objIn.readObject();
            
            logger.log(Level.INFO, "Successfully Compiled Model from file={0}", modelFilename);
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            try {
                if (fileIn != null) {
                    fileIn.close();
                }

                if (objIn != null) {
                    objIn.close();
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }
    }
    
    public LMClassifier getBaseClassifier() {
        return classifier;
    }

    public abstract ScoredClassification evaluate(String reviewText);
}
