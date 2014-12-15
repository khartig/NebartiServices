/*
 * Nebarti
 * Copyright © 2012-2013. All rights reserved.
 */
package com.idot.dataingest.namedentities;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunking;
import com.aliasi.dict.DictionaryEntry;
import com.aliasi.dict.ExactDictionaryChunker;
import com.aliasi.dict.MapDictionary;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.idot.dataingest.namedentities.dictionaries.Dictionaries;
import com.idot.dataingest.namedentities.dictionaries.Dictionary;
import com.mongodb.BasicDBObjectBuilder;
import com.nebarti.dataaccess.dao.GenericDao;
import com.nebarti.dataaccess.domain.ClassifiedText;
import com.nebarti.dataaccess.domain.NamedEntity;
import com.nebarti.dataaccess.domain.comparators.NamedEntityComparator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 *
 */
public class NamedEntitySummarizer {

    private static final double CHUNK_SCORE = 1.0;
    private static final Dictionaries dictionaries = new Dictionaries();
    private static final ExactDictionaryChunker dictionaryChunker;
    public static final Logger logger = Logger.getLogger(NamedEntitySummarizer.class.getName());

    static {
        MapDictionary<String> dictionary = new MapDictionary<String>();
        for (Dictionary d : dictionaries.getDictionaryCollection()) {
            for (String value : d.getValues()) {
                dictionary.addEntry(new DictionaryEntry<String>(value, d.getType().toString(), CHUNK_SCORE));
            }
        }

        dictionaryChunker = new ExactDictionaryChunker(
                dictionary,
                IndoEuropeanTokenizerFactory.INSTANCE,
                false, true); // find all occurances, case sensitive
        dictionaryChunker.setReturnAllMatches(false);
    }

    public static List<NamedEntity> saveEntities(String modelName, Integer limit) {
        BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();
        if (limit == 0) {
            limit = GenericDao.getCount(modelName, "sentiment");
        }
        List<ClassifiedText> documents = GenericDao.findDocuments(modelName, "sentiment", modelName, limit, 0, builder);

        StringBuilder stringBuilder = new StringBuilder();

        for (ClassifiedText document : documents) {
            stringBuilder.append(document.getText());
        }

        List entitySet = new ArrayList<NamedEntity>();
        HashMap<String, NamedEntity> namedEntities = new HashMap<String, NamedEntity>();
        NamedEntityComparator comparator = new NamedEntityComparator(namedEntities);
        TreeMap<String, NamedEntity> sorted_map = new TreeMap(comparator);
        Chunking chunking = dictionaryChunker.chunk(stringBuilder.toString());

        for (Chunk chunk : chunking.chunkSet()) {
            String entity = stringBuilder.substring(chunk.start(), chunk.end());
            NamedEntity namedEntity;

            if (namedEntities.containsKey(entity)) {
                namedEntity = namedEntities.get(entity);
                namedEntity.setFrequency(namedEntity.getFrequency() + 1);
                namedEntities.put(entity, namedEntity);
            } else {
                List<String> classifications = getEntityClassifications(entity, documents);
                namedEntities.put(entity,
                        new NamedEntity(
                        chunk.type(),
                        Double.toString(chunk.score()),
                        entity,
                        1, classifications));
            }
        }

        sorted_map.putAll(namedEntities);
        entitySet = new ArrayList<NamedEntity>(sorted_map.values());

        GenericDao.setNamedEntities(modelName, "namedentities", entitySet);
        
        return entitySet;
    }

    private static List<String> getEntityClassifications(String entity, List<ClassifiedText> documents) {
        ArrayList<String> classifications = new ArrayList<String>();

        for (ClassifiedText document : documents) {
            if (document.getText().contains(entity)) {
                if (!classifications.contains(document.getClassification())) {
                    classifications.add(document.getClassification());
                }
            }
        }
        return classifications;
    }
}
