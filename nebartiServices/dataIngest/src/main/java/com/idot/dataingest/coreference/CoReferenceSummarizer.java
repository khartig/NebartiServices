/*
 * Nebarti
 * Copyright © 2012-2013. All rights reserved.
 */
package com.idot.dataingest.coreference;

import com.aliasi.lm.TokenizedLM;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.LowerCaseTokenizerFactory;
import com.aliasi.tokenizer.StopTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;
import com.aliasi.util.ScoredObject;
import com.idot.dataingest.utilities.StopWords;
import com.mongodb.BasicDBObjectBuilder;
import com.nebarti.dataaccess.dao.GenericDao;
import com.nebarti.dataaccess.domain.ClassifiedText;
import com.nebarti.dataaccess.domain.CoReference;
import com.nebarti.dataaccess.domain.comparators.CoreferenceComparator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;
import org.bson.types.ObjectId;

/**
 *
 */
public class CoReferenceSummarizer {
    private static final StopWords stopWords = new StopWords();
    public static final Logger logger = Logger.getLogger(CoReferenceSummarizer.class.getName());

    public static List<CoReference> saveCoReferences(String modelName, Integer limit, Integer wordCount, Boolean rebuild) {
        if (wordCount < 2) {
            wordCount = 2;
        } else if (wordCount > 3) {
            wordCount = 3;
        }

        int NGRAM_REPORTING_LENGTH = wordCount;
        int NGRAM;
        if (wordCount == 2) {
            NGRAM = 3;
        } else {
            NGRAM = 5;
        }

        int MIN_COUNT = 5;
        int MAX_COUNT = 100;

        TokenizerFactory ietf = IndoEuropeanTokenizerFactory.INSTANCE;
        TokenizerFactory lctf = new LowerCaseTokenizerFactory(ietf, Locale.ENGLISH);
        TokenizerFactory tf = new StopTokenizerFactory(lctf, stopWords.getwords());
        
        if (limit == 0) {
            limit = GenericDao.getCount(modelName, "sentiment");
        }

        BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();
        List<ClassifiedText> documents = GenericDao.findDocuments(
                modelName, "sentiment", modelName, limit, 0, builder);

        TokenizedLM model = new TokenizedLM(tf, NGRAM);

        for (ClassifiedText document : documents) {
            String text = prepText(document);
            model.handle(text);
        }

        model.sequenceCounter().prune(3);
        SortedSet<ScoredObject<String[]>> collocations = model.collocationSet(NGRAM_REPORTING_LENGTH, MIN_COUNT, MAX_COUNT);

        String fullText = null;
        List<CoReference> coreferences;
        CoreferenceComparator comparator = new CoreferenceComparator();
        TreeSet<CoReference> sorted_set = new TreeSet(comparator);
        
        for (ScoredObject<String[]> collocation : collocations) {
            HashSet<String> indicies = getDocumentIndicies(documents, collocation.getObject());
            if (!indicies.isEmpty()) {
                List<String> classifications = getClassifications(modelName, indicies);
                Object[] indiciesArray = indicies.toArray();
                fullText = getText(modelName, (String) indiciesArray[0]);
                sorted_set.add(
                        new CoReference (
                        wordCount, Double.toString(collocation.score()),
                        collocation.getObject(), new ArrayList<String>(indicies), fullText, classifications));
            }
        }

        coreferences = new ArrayList<CoReference>(sorted_set);
        GenericDao.setCoreferences(modelName, "coreferences", coreferences, rebuild);
        
        return coreferences;
    }

    private static String prepText(ClassifiedText document) {
        String text = document.getText().replaceAll("'", "").replaceAll("\\-", "").replaceAll("\\*", "");
        text = text.replaceAll("\\<.*?\\>", "").replaceAll("!", "");
        return text;
    }

    private static HashSet<String> getDocumentIndicies(List<ClassifiedText> documents, String[] wordArray) {
        HashSet<String> indicies = new HashSet<String>();

        for (ClassifiedText document : documents) {
            if (stringContainsAllStrings(prepText(document), wordArray)) {
                indicies.add(document.getId());
            }
        }

        return indicies;
    }

    private static Boolean stringContainsAllStrings(String text, String[] wordArray) {
        List<String> textTokens = Arrays.asList(text.split("\\s+"));

        for (int i = 0; i < wordArray.length; i++) {
            if (!textTokens.contains(wordArray[i])) {
                return false;
            }

            if (i > 0) {
                if (textTokens.indexOf(wordArray[i]) < textTokens.indexOf(wordArray[i - 1])) {
                    return false;
                }
            }
        }

        return true;
    }
    
    private static List<String> getClassifications(String modelName, HashSet<String> indicies) {
        ArrayList<String> classifications = new ArrayList<String>();
        BasicDBObjectBuilder builder;
        List<ClassifiedText> documents;
        
        for (String index : indicies) {
            builder = BasicDBObjectBuilder.start();
            builder.add("_id", new ObjectId(index));
            documents = GenericDao.findDocuments(modelName, "sentiment", modelName, 1, 0, builder);
            if (!documents.isEmpty()) {
                for (ClassifiedText document : documents) {
                    if (!classifications.contains(document.getClassification())) {
                        classifications.add(document.getClassification());
                    }
                }
            }                  
        }
        
        return classifications;
    }
    
    private static String getText(String modelName, String index) {
        String text = null;
        BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();
        builder.add("_id", new ObjectId(index));
        List<ClassifiedText> documents = GenericDao.findDocuments(modelName, "sentiment", modelName, 1, 0, builder);
        
        if (!documents.isEmpty()) {
            text = documents.get(0).getText();
        }
        
        return text;
    }
}
