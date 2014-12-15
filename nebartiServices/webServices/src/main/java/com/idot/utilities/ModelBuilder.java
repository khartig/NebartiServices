/*
 * Nebarti
 * Copyright 2013 All rights reserved.
 */
package com.idot.utilities;

import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.classify.NaiveBayesClassifier;
import com.aliasi.classify.ScoredClassification;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;
import com.idot.evaluators.CategoryReviewClassifier;
import com.idot.evaluators.ReviewClassifier;
import com.nebarti.dataaccess.dao.GenericDao;
import com.nebarti.dataaccess.domain.ClassificationSet;
import com.nebarti.dataaccess.domain.ClassifiedText;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.types.ObjectId;

/**
 * Generate a new model.
 */
public class ModelBuilder {

    private static final File modelsDirRoot;
    private static String webServicesLocation;
    private static final HashMap<String, ReviewClassifier> classifiers = new HashMap<String, ReviewClassifier>();
    private static final com.idot.utilities.Properties properties = new com.idot.utilities.Properties();

    static {
        modelsDirRoot = new File(properties.getProperty("model.storage.dir"));
        webServicesLocation = properties.getProperty("web.services.location");
        for (File dir : modelsDirRoot.listFiles(new NonDotFilenameFilter())) {
            for (File modelFile : dir.listFiles(new NonDotFilenameFilter())) {
                classifiers.put(modelFile.getName(), new CategoryReviewClassifier(modelFile));
            }
        }
    }
    private File modelFile;
    private List<String> classifications = new ArrayList<String>();
    private TokenizerFactory ieTf = new IndoEuropeanTokenizerFactory();
    private TokenizerFactory factory = ieTf;
    private int charNGramLength = 6;
//    private DynamicLMClassifier<TokenizedLM> classifier;
    private NaiveBayesClassifier classifier;
    private ClassificationSet classificationSet;
    public static final Logger logger = Logger.getLogger(ModelBuilder.class.getName());

    public void build(String modelName) throws IOException {
        classificationSet = GenericDao.getClassifications(modelName, "classifications");

        defineClassifier(modelName);

        // Construct the object used to define the search criteria
        BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();
        String searchString = ".*?";

        HashMap mapValues = new HashMap<String, Object>();
        mapValues.put("$regex", searchString);
        mapValues.put("$options", "i"); // case insensitive
        builder.add("text", mapValues);
        builder.add("validated", Boolean.valueOf(true));
        builder.add("useInModel", Boolean.valueOf(true));

        int offset = 0;
        int limit = GenericDao.getCount(modelName, "sentiment");
        List<ClassifiedText> documents = GenericDao.findDocuments(modelName, "sentiment", modelName, limit, offset, builder);

        int numTrainingChars = 0;

        logger.log(Level.INFO, "Training text files...");
        for (ClassifiedText document : documents) {

            // temporary for testing
//                review = SpellingCorrector.correctSpelling(new StringBuilder(review)).toString();
            //review = review.toLowerCase();
//                review = Negation.negateString(review);

            numTrainingChars += document.getText().length();
            Classification classification = new Classification(document.getClassification());
            Classified<CharSequence> classified = new Classified<CharSequence>(document.getText(), classification);
            classifier.handle(classified);
        }

        modelFile = new File(properties.getProperty("model.storage.dir") + classificationSet.getName() + "/" + modelName);
        if (modelFile.exists()) {
            modelFile.delete();
        }
        com.aliasi.util.AbstractExternalizable.compileTo(classifier, modelFile);

        logger.log(Level.INFO, "  # Training Chars completed = {0}", numTrainingChars);

        if (reloadModel(modelName)) {
            logger.log(Level.INFO, "  Reloaded model = {0}", modelName);
        } else {
            logger.log(Level.INFO, "  Reload faile for model = {0}", modelName);
        }
    }

    /**
     * Send web service request to reload the new model.
     * @param modelName
     * @return 
     */
    private Boolean reloadModel(String modelName) {
        Client client = Client.create();
        WebResource webResource = client.resource(webServicesLocation + "services/textanalyzer/reload/" + modelName);
        ClientResponse response = webResource.type("application/x-www-form-urlencoded").put(ClientResponse.class);

        if (response.getStatus() != 201) {
            return false;
        } else {
            return true;
        }
    }

    public void reclassify(String modelName) {
        logger.log(Level.INFO, "  Start reclassifying text for model = {0}", modelName);

        BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();
        String searchString = ".*?";

        HashMap mapValues = new HashMap<String, Object>();
        mapValues.put("$regex", searchString);
        mapValues.put("$options", "i"); // case insensitive
        builder.add("text", mapValues);

        int offset = 0;
        int limit = GenericDao.getCount(modelName, "sentiment");
        List<ClassifiedText> documents = GenericDao.findDocuments(modelName, "sentiment", modelName, limit, offset, builder);

        Client client = Client.create();
//        WebResource webResource = client.resource(webServicesLocation + "services/textanalyzer/categorize");
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));

        int docCounter = 0;
        for (ClassifiedText document : documents) {
            if ((++docCounter % 500) == 0) {
                logger.log(Level.INFO, "Reclasified {0} of {1} documents for model = {2}", new Object[]{docCounter, documents.size(), modelName});
            }

//            MultivaluedMap formData = new MultivaluedMapImpl();
//            formData.add("reviewText", document.getText());
//            formData.add("model", modelName);
//            ClientResponse response = webResource.type("application/x-www-form-urlencoded").post(ClientResponse.class, formData);
//
//            if (response.getStatus() != 200) {
//                logger.log(Level.WARNING, "Failed : HTTP error code : {0}", response.getStatus());
//            }
//            
//            String classification = response.getEntity(String.class);
//            classification = classification.substring(classification.indexOf(":")+2, classification.lastIndexOf("\"}"));  

            String oldClassification = document.getClassification();
            if (!document.getUseInModel() && !document.getValidated()) {
                ScoredClassification classification = classifyText(modelName, document.getText());
                if (!classification.bestCategory().equals(oldClassification)) {
                    logger.log(Level.INFO, "Classification changed to {0} for text = {1}", new Object[]{classification,document.getText()});
                }

                String date = dateFormatGmt.format(new Date());
                BasicDBObject dbo = new BasicDBObject();

                dbo.put("_id", new ObjectId(document.getId()));
                dbo.put("createDate", document.getCreatedDate());
                dbo.put("updatedDate", date);
                dbo.put("text", document.getText());
                dbo.put("latitude", document.getLatitude());
                dbo.put("longitude", document.getLongitude());
                dbo.put("classification", classification.bestCategory());
                dbo.put("score", Double.toString(classification.score(0)) );
                dbo.put("useInModel", document.getUseInModel());
                dbo.put("validated", document.getValidated());
                dbo.put("gender", document.getGender());
                dbo.put("age", document.getAge());

                DBObject updatedObject = GenericDao.update(modelName, "sentiment", dbo);
            }

        }

        logger.log(Level.INFO, "Finished reclassification for model = {0}", modelName);
    }

    private ScoredClassification classifyText(String modelName, String text) {
        ReviewClassifier reviewClassifier = classifiers.get(modelName);
        ScoredClassification scoredClassification = null;

        if (reviewClassifier != null) {
            scoredClassification = reviewClassifier.evaluate(text);
            if (scoredClassification == null) {
                return null;
            } 
        } 

        return scoredClassification;
    }

    private void defineClassifier(String modelName) {
        classifications.addAll(classificationSet.getClassifications());
        String[] strArray = new String[classifications.size()];
        classifier = new NaiveBayesClassifier(
                classifications.toArray(strArray),
                factory,
                charNGramLength);
    }
}
