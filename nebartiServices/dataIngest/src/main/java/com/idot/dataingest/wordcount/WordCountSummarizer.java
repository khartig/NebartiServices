/*
 * Nebarti
 * Copyright © 2012-2013 All rights reserved.
 */
package com.idot.dataingest.wordcount;

import com.idot.dataingest.utilities.StopWords;
import com.mongodb.BasicDBObjectBuilder;
import com.nebarti.dataaccess.dao.GenericDao;
import com.nebarti.dataaccess.domain.ClassifiedText;
import com.nebarti.dataaccess.domain.WordCount;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Calculates the frequency of words contained within all text for a defined model.
 * stop words are excluded from the count. Word and word frequency summarization
 * information are written to a Mongo collection.
 */
public class WordCountSummarizer {
    private static final StopWords stopWords = new StopWords();
    public static final Logger logger = Logger.getLogger(WordCountSummarizer.class.getName());
    
    public static List<WordCount> saveWordCount(String modelName, Integer limit) {
        BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();
        List<WordCount> wordCounts = new ArrayList<WordCount>();

        if (limit <= 0) {
            limit = GenericDao.getCount(modelName, "sentiment");
        }
        
        List<ClassifiedText> documents = GenericDao.findDocuments(
                modelName, "sentiment", modelName, 
                limit, 0, builder);

        if (!documents.isEmpty()) {
            WordCounter wordCounter = new WordCounter();
            wordCounter.ignore(stopWords.toString());

            for (ClassifiedText document : documents) {
                wordCounter.countWords(document.getText());
            }

            wordCounts = wordCounter.getWordCounts(WordCounter.SortOrder.BY_FREQUENCY_DESCENDING);
            int i = -1;
            for (WordCount wordcount : wordCounts) {
                wordcount.setClassifications(getWordCountClassifications(wordcount.getWord(), documents));
                wordCounts.set(++i, wordcount);
            }
            
            GenericDao.setWordCounts(modelName, "wordcounts", wordCounts);
        }
        
        return wordCounts;
    }
    
    private static List<String> getWordCountClassifications(String word, List<ClassifiedText> documents) {
        ArrayList<String> classifications = new ArrayList<String>();
     
        for (ClassifiedText document : documents) {
            if (document.getText().contains(word)) {
                if (!classifications.contains(document.getClassification())) {
                    classifications.add(document.getClassification());
                }
            }
        }
        return classifications;
    }
}
