/*
 * Nebarti
 * Copyright 2013 All rights reserved.
 */
package com.idot.services.endpoints;

import com.aliasi.classify.ScoredClassification;
import com.nebarti.dataaccess.dao.GenericDao;
import com.nebarti.dataaccess.domain.Classification;
import com.nebarti.dataaccess.domain.ClassificationSet;
import com.idot.evaluators.CategoryReviewClassifier;
import com.idot.evaluators.ReviewClassifier;
import com.idot.utilities.NonDotFilenameFilter;
import com.idot.utilities.Properties;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 */
@Path("/textanalyzer")
public class TextAnalyzerEndpoint {

    private static ReviewClassifier categoryClassifier;
    private static ReviewClassifier polarityClassifier;
    private static final File modelsDirRoot;
    private static final HashMap<String, ReviewClassifier> classifiers = new HashMap<String, ReviewClassifier>();
    private static final Properties properties = new Properties();
    public static final Logger logger = Logger.getLogger(TextAnalyzerEndpoint.class.getName());

    static {
        
//        categoryClassifier = new CategoryReviewClassifier(properties.getProperty("bayesian.category.bookreviews.model.file"));
//        polarityClassifier = new PolarityReviewClassifier(properties.getProperty("bayesian.polarity.20k.bookreviews.model.file"));

        modelsDirRoot = new File(properties.getProperty("model.storage.dir"));
        for (File dir : modelsDirRoot.listFiles(new NonDotFilenameFilter())) {
            for (File modelFile : dir.listFiles(new NonDotFilenameFilter())) {
                classifiers.put(modelFile.getName(), new CategoryReviewClassifier(modelFile));
            }
        }
    }

    // ToDo: change this to return ScoredClassification type
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/categorize")
    public Classification categorize(
            @FormParam("reviewText") String reviewText,
            @FormParam("model") String modelName) throws IOException {

        Classification classification = null;

        ReviewClassifier classifier = classifiers.get(modelName);
        ScoredClassification jc;

        if (modelName.equals("category")) { // left over to support old book review web page calls
            jc = categoryClassifier.evaluate(reviewText);
            if (jc != null) {
                String suffix;
                String category = jc.bestCategory();

                if (category.equals("1")) {
                    suffix = "Star";
                } else {
                    suffix = "Stars";
                }

                classification = new Classification(jc.bestCategory() + " " + suffix, Double.toString(jc.score(0)));
            } else {
                classification = new Classification("Error", "0.0");
            }
        } else if (modelName.equals("polarity")) {
            jc = polarityClassifier.evaluate(reviewText);
            if (jc != null) {
                classification = new Classification(jc.bestCategory(), Double.toString(jc.score(0)));
            } else {
                classification = new Classification("Error", "0.0");
            }
        } else {
            if (classifier != null) {
                jc = classifier.evaluate(reviewText);
                if (jc != null) {
                    classification = new Classification(jc.bestCategory(), Double.toString(jc.score(0)) );
                } else {
                    classification = new Classification("Error", "9000.0");
                }
            } else {
                classification = new Classification("Error", "9000.0");
            }
        }

        return classification;
    }
    
    /**
     * Reload the model requested by name
     */
    @PUT
    @Path("/reload/{modelName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reloadModel(@PathParam("modelName") String modelName) {
        URI uri = null;
        ClassificationSet classificationSet = GenericDao.getClassifications(modelName, "classifications");

        File modelFile = new File(properties.getProperty("model.storage.dir") + "/" + classificationSet.getName() + "/" + modelName);
        
        classifiers.remove(modelName);
        classifiers.put(modelName, new CategoryReviewClassifier(modelFile));
        
        return Response.created(uri).build();
    }
}
