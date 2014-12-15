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
public class PolarityReviewClassifier extends ReviewClassifier {

    public PolarityReviewClassifier(String modelFilename) {
        super(modelFilename);
    }
    
    public PolarityReviewClassifier(File modelFile) {
        super(modelFile);
    }

    @Override
    public ScoredClassification evaluate(String reviewText) {
        if (classifier != null) {
            return classifier.classify(reviewText);
        } else {
            return null;
        }
    }
}
