/*
 * Nebarti
 * Copyright 2013 All rights reserved.
 */
package com.idot.evaluators;

import com.aliasi.classify.JointClassification;
import com.aliasi.classify.ScoredClassification;
import java.io.File;

/**
 *
 * 
 */
public class CategoryReviewClassifier extends ReviewClassifier {

    public CategoryReviewClassifier(String modelFilename) {
        super(modelFilename);
    }
    
    public CategoryReviewClassifier(File modelFile) {
        super(modelFile);
    }

    /**
     * Returns a classification for a string of text passed in. This is the 
     * method that also handles special text prepping if needed before classification
     * is calculated.
     * 
     * @param reviewText a String of text to be classified
     * @return the JointClassification object with the classification results.
     *         null if there is some error or the classifier is not defined.
     */
    @Override
    public ScoredClassification evaluate(String reviewText) {
        if (classifier != null) {
            return classifier.classify(reviewText);
        } else {
            return null;
        }
    }
}
