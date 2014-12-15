/*
 * Nebarti
 * Copyright © 2012-2013 All rights reserved.
 */
package com.nebarti.dataaccess.dao;

import com.nebarti.dataaccess.domain.ClassificationSet;
import com.nebarti.dataaccess.domain.ClassifiedText;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import com.nebarti.dataaccess.domain.CoReference;
import com.nebarti.dataaccess.domain.ImageReference;
import com.nebarti.dataaccess.domain.NamedEntity;
import com.nebarti.dataaccess.domain.Reference;
import com.nebarti.dataaccess.domain.WordCount;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoDbUtils;

/**
 * Used to read/write to any collection in a database defined for a model and
 * its text.
 */
public class GenericDao {

    private static Mongo mongo;
    public static final Logger logger = Logger.getLogger(GenericDao.class.getName());

    static {
        try {
            mongo = new Mongo("127.0.0.1", 27017);
        } catch (UnknownHostException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (MongoException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }
    
    public static synchronized void close() {
        mongo.close();
    }

    public static synchronized WriteResult save(String databaseName, String collectionName, DBObject dbo) {
        DB db = MongoDbUtils.getDB(mongo, databaseName);
        DBCollection collection = db.getCollection(collectionName);
        
        WriteResult result = collection.save(dbo);
        
        return result;
    }
    
    public static synchronized DBObject update(String databaseName, String collectionName, DBObject dbo) {
        DB db = MongoDbUtils.getDB(mongo, databaseName);
        DBCollection collection = db.getCollection(collectionName);
        
        DBObject query = new BasicDBObject();
        query.put("_id", dbo.get("_id"));
        DBObject retrievedObject = collection.findOne(query);
        dbo.put("createdDate", retrievedObject.get("createdDate"));
        DBObject dbObject = collection.findAndModify(query, dbo);
        
        return dbObject;
    }
    
    public static synchronized void delete(String databaseName, String collectionName, String id) {
        DB db = MongoDbUtils.getDB(mongo, databaseName);
        DBCollection collection = db.getCollection(collectionName);
        
        if (!(id == null) || !(id.equals(""))) {
            DBObject query = new BasicDBObject();
            query.put("_id", new ObjectId(id));
            DBObject dbo = collection.findOne(query, query);
            
            collection.remove(dbo);
        } else {
            logger.log(Level.WARNING, "docuemnt with id={0} not deleted.", id);
        }

    }
    
    public static synchronized ClassificationSet getClassifications(String databaseName, String collectionName) {
        DB db = MongoDbUtils.getDB(mongo, databaseName);
        DBCollection collection = db.getCollection(collectionName);
        
        DBObject dbObject = collection.findOne();
        
        ClassificationSet classificationSet = new ClassificationSet();
        classificationSet.setClassifications((List<String>)dbObject.get("classifications"));
        classificationSet.setName((String)dbObject.get("name"));
        classificationSet.setVisibleName((String)dbObject.get("visibleName"));

        return classificationSet;
    }
    
    public static synchronized List<ClassifiedText> findDocuments(
        String databaseName, String collectionName, 
        String modelName, Integer limit, Integer offset, BasicDBObjectBuilder builder) {
        
        DB db = MongoDbUtils.getDB(mongo, databaseName);
        DBCollection collection = db.getCollection(collectionName);
        
        ClassifiedText classifiedText;
        List<ClassifiedText> documents = new ArrayList<ClassifiedText>();
        
        // sort by latest records first
        DBObject dbo = new BasicDBObject();
        dbo.put("$natural", -1); 
        
        Iterator itr = collection.find(builder.get()).sort(dbo).skip(offset).limit(limit).iterator();

        while (itr.hasNext()) {
            DBObject obj = (DBObject) itr.next();

            try {
                classifiedText = new ClassifiedText();
                classifiedText.setId(obj.get("_id").toString());
                classifiedText.setClassification(obj.get("classification").toString());
                
                // temp until all records have this value set
                if (obj.get("score") == null) {
                    classifiedText.setScore("9000.0");
                } else { 
                    classifiedText.setScore(obj.get("score").toString());
                }
                
                classifiedText.setAge(obj.get("age").toString());
                classifiedText.setCreatedDate(obj.get("createdDate").toString());
                classifiedText.setUpdatedDate(obj.get("updatedDate").toString());
                classifiedText.setValidated(Boolean.valueOf(obj.get("validated").toString()));
                classifiedText.setUseInModel(Boolean.valueOf(obj.get("useInModel").toString()));
                classifiedText.setLatitude(obj.get("latitude").toString());
                classifiedText.setLongitude(obj.get("longitude").toString());
                classifiedText.setGender(obj.get("gender").toString());

                String text = obj.get("text").toString();
                text = text.replaceAll("\n", " ").replaceAll("\"", "'");
                classifiedText.setText(text);

                documents.add(classifiedText);
            } catch (Exception e) {
                logger.log(Level.WARNING, "Bad DBObject values retrievedfrom DB. {0}", obj.toString());
                logger.log(Level.WARNING, e.getLocalizedMessage(), e);
            }
        }

        return documents;        
    }
    
    public static synchronized void removeDuplicates(String databaseName, String collectionName) {
        DB db = MongoDbUtils.getDB(mongo, databaseName);
        DBCollection collection = db.getCollection(collectionName);
        
        // ToDo: find and remove documents with identical text
        
        // Construct the object used to define the search criteria
        String searchString = ".*?";  // match all strings  
        HashMap mapValues = new HashMap<String, Object>();
        mapValues.put("$regex", searchString);
        
        BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();        
        builder.add("text", mapValues);
        
        // sort by text string so all duplicates are grouped together
        DBObject dbo = new BasicDBObject();
        dbo.put("text", 1); 
        
        String previousText = "";
        String currentText;
        Iterator itr = collection.find(builder.get()).sort(dbo).limit(0).iterator();
        while (itr.hasNext()) {
            DBObject obj = (DBObject) itr.next();
            currentText = obj.get("text").toString();
            try {
                if (currentText.equals(previousText)) {
                    DBObject query = new BasicDBObject();
                    query.put("_id", new ObjectId(obj.get("_id").toString()));
                    DBObject objToRemove = collection.findOne(query, query);
                    collection.remove(objToRemove);
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, e.getLocalizedMessage(), e);
            }
        
            previousText = currentText;
        }
    }
           
    public static synchronized Integer getNumberOfMatchingDocuments(
            String databaseName, String collectionName, BasicDBObjectBuilder builder) {
        
        DB db = MongoDbUtils.getDB(mongo, databaseName);
        DBCollection collection = db.getCollection(collectionName);
        
        Long longCount = collection.getCount(builder.get());
        Integer count = longCount.intValue();
        return count;
    }
    
    /**
     * Get total number of documents in the collection that have an _id key.
     * 
     * @return the number of documents
     */
    public static synchronized Integer getCount(String databaseName, String collectionName) {
        DB db = MongoDbUtils.getDB(mongo, databaseName);
        DBCollection collection = db.getCollection(collectionName);
        
        // if the db has greater than the max int value, have to do something different
        Long longCount = collection.getCount();
        Integer count = longCount.intValue();

        return count;
    }
    
    /**
     * 
     * @param wordcounts 
     */
    public static synchronized void setWordCounts(String databaseName, String collectionName, List<WordCount> wordcounts) {
        DB db = MongoDbUtils.getDB(mongo, databaseName);
        DBCollection collection = db.getCollection(collectionName);
        
        collection.drop(); // assumption is all new data should be written
        for (WordCount wordcount : wordcounts) {    
            DBObject dbo = new BasicDBObject();
            dbo.put("word", wordcount.getWord());
            dbo.put("frequency", wordcount.getFrequency());
            dbo.put("classifications", wordcount.getClassifications());
            collection.save(dbo);
        }

    }
    
    /**
     * 
     */
    public static synchronized List<WordCount> getWordCounts(String databaseName, String collectionName, int offset, int limit, BasicDBObjectBuilder builder) {
        Iterator itr;
        WordCount wordCount;
        List<WordCount> wordCounts = new ArrayList<WordCount>();
 
        DB db = MongoDbUtils.getDB(mongo, databaseName);
        DBCollection collection = db.getCollection(collectionName);
                
        if (limit == 0) {
            itr = collection.find(builder.get()).skip(offset).iterator();
        } else {
            itr = collection.find(builder.get()).skip(offset).limit(limit).iterator();
        }

        while (itr.hasNext()) {
            DBObject obj = (DBObject) itr.next();
            wordCount = new WordCount();
            wordCount.setId(obj.get("_id").toString());
            wordCount.setWord(obj.get("word").toString());
            wordCount.setFrequency(Integer.decode(obj.get("frequency").toString()));
            wordCount.setClassifications((List<String>)obj.get("classifications"));
            wordCounts.add(wordCount);
        }
               
        return wordCounts;
    }

    public static synchronized void setCoreferences(String databaseName, String collectionName, List<CoReference> coReferences, Boolean rebuild) {
        DB db = MongoDbUtils.getDB(mongo, databaseName);
        DBCollection collection = db.getCollection(collectionName);
        
        if (rebuild) {
            collection.drop(); 
        }
        
        for (CoReference coReference : coReferences) {    
            DBObject dbo = new BasicDBObject();
            dbo.put("count", coReference.getCount());
            dbo.put("words", coReference.getWords());
            dbo.put("text", coReference.getText());
            dbo.put("score", coReference.getScore());
            dbo.put("indicies", coReference.getIndicies());
            dbo.put("classifications", coReference.getClassifications());
            collection.save(dbo);
        }

    }

    public static synchronized List<CoReference> getCoreferences(String databaseName, String collectionName, Integer offset, Integer limit, BasicDBObjectBuilder builder) {
        Iterator itr;
        CoReference coreference;
        List<CoReference> coreferences = new ArrayList<CoReference>();
   
        DB db = MongoDbUtils.getDB(mongo, databaseName);
        DBCollection collection = db.getCollection(collectionName);
        
        if (limit == 0) {
            itr = collection.find(builder.get()).skip(offset).iterator();
        } else {
            itr = collection.find(builder.get()).skip(offset).limit(limit).iterator();
        }
        
        while (itr.hasNext()) {
            DBObject obj = (DBObject) itr.next();
            coreference = new CoReference();
            coreference.setId(obj.get("_id").toString());
            coreference.setCount(Integer.decode(obj.get("count").toString()));
            coreference.setScore(obj.get("score").toString());
            coreference.setWords(obj.get("words").toString());
            coreference.setText(obj.get("text").toString());
            coreference.setIndicies((List<String>)obj.get("indicies"));
//            Object temp = obj.get("indicies");
            coreference.setClassifications((List<String>)obj.get("classifications"));
            coreferences.add(coreference);
        }
                
        return coreferences;
    }
    
    public static synchronized void setNamedEntities(String databaseName, String collectionName, List<NamedEntity> entities) {
        DB db = MongoDbUtils.getDB(mongo, databaseName);
        DBCollection collection = db.getCollection(collectionName);
        
        collection.drop(); // assume all new data should be rewritten
        for (NamedEntity entity : entities) {    
            DBObject dbo = new BasicDBObject();
            dbo.put("type", entity.getType());
            dbo.put("score", entity.getScore());
            dbo.put("phrase", entity.getPhrase());
            dbo.put("frequency", entity.getFrequency());
            dbo.put("classifications", entity.getClassifications());
            collection.save(dbo);
        }

    }
    
    public static synchronized List<NamedEntity> getNamedEntities(String databaseName, String collectionName, Integer offset, Integer limit, BasicDBObjectBuilder builder) {
        Iterator itr;
        NamedEntity namedEntity;
        List<NamedEntity> namedEntities = new ArrayList<NamedEntity>();
        
        DB db = MongoDbUtils.getDB(mongo, databaseName);
        DBCollection collection = db.getCollection(collectionName);
        
        if (limit == 0) {
            itr = collection.find(builder.get()).skip(offset).iterator();
        } else {
            itr = collection.find(builder.get()).skip(offset).limit(limit).iterator();
        }
        
        while (itr.hasNext()) {
            DBObject obj = (DBObject) itr.next();
            namedEntity = new NamedEntity();
            namedEntity.setId(obj.get("_id").toString());
            namedEntity.setType(obj.get("type").toString());
            namedEntity.setScore(obj.get("score").toString());
            namedEntity.setPhrase(obj.get("phrase").toString());
            namedEntity.setFrequency(Integer.decode(obj.get("frequency").toString()));
            namedEntity.setClassifications((List<String>)obj.get("classifications"));
            namedEntities.add(namedEntity);
        }
        
        return namedEntities;
    }
    
    public static synchronized void setReferences(String databaseName, String collectionName, List<Reference> references) {
        DB db = MongoDbUtils.getDB(mongo, databaseName);
        DBCollection collection = db.getCollection(collectionName);
        
        collection.drop(); // assume all new data should be written
        for (Reference reference : references) {    
            DBObject dbo = new BasicDBObject();
            dbo.put("shortURLString", reference.getShortURLString());
            dbo.put("urlString", reference.getUrlString());
            dbo.put("title", reference.getTitle());
            dbo.put("count", reference.getCount());
            dbo.put("classifications", reference.getClassifications());
            collection.save(dbo);
        }

    }

    public static synchronized Set<Reference> getReferences(String databaseName, String collectionName, Integer offset, Integer limit, BasicDBObjectBuilder builder) {
        Iterator itr;
        Reference reference;
        Set<Reference> references = new HashSet<Reference>();
        
        DB db = MongoDbUtils.getDB(mongo, databaseName);
        DBCollection collection = db.getCollection(collectionName);
        
        if (limit == 0) {
            itr = collection.find(builder.get()).skip(offset).iterator();
        } else {
            itr = collection.find(builder.get()).skip(offset).limit(limit).iterator();
        }
        
        while (itr.hasNext()) {
            DBObject obj = (DBObject) itr.next();
            reference = new Reference();
            reference.setId(obj.get("_id").toString());
            reference.setTitle(obj.get("title").toString());
            reference.setShortURLString(obj.get("shortURLString").toString());
            reference.setUrlString(obj.get("urlString").toString());
            reference.setCount(Integer.decode(obj.get("count").toString()));
            reference.setClassifications((List<String>)obj.get("classifications"));
            references.add(reference);
        }
        
        return references;
    }

    public static synchronized void setImages(String databaseName, String collectionName, List<ImageReference> images) {
        DB db = MongoDbUtils.getDB(mongo, databaseName);
        DBCollection collection = db.getCollection(collectionName);
        
        collection.drop(); // assume all new data should be written
        for (ImageReference image : images) {    
            DBObject dbo = new BasicDBObject();
            dbo.put("urlString", image.getUrlString());
            dbo.put("classifications", image.getClassifications());
            collection.save(dbo);
        }
    }
    
    public static synchronized List<ImageReference> getImages(String databaseName, String collectionName, Integer offset, Integer limit, BasicDBObjectBuilder builder) {
        Iterator itr;
        ImageReference image;
        List<ImageReference> images = new ArrayList<ImageReference>();
        
        DB db = MongoDbUtils.getDB(mongo, databaseName);
        DBCollection collection = db.getCollection(collectionName);
        
        // sort by latestest records first
        DBObject dbo = new BasicDBObject();
        dbo.put("$natural", -1);  
        
        if (limit == 0) {
            itr = collection.find(builder.get()).sort(dbo).skip(offset).iterator();
        } else {
            itr = collection.find(builder.get()).sort(dbo).skip(offset).limit(limit).iterator();
        }
        
        while (itr.hasNext()) {
            DBObject obj = (DBObject) itr.next();
            image = new ImageReference();
            image.setId(obj.get("_id").toString());
            image.setUrlString(obj.get("urlString").toString());
            image.setClassifications((List<String>)obj.get("classifications"));
            images.add(image);
        }

        return images;
    }

    /**
     * Delete records in sentiment collection to defined limit.
     */
    public static synchronized void cleanToRecordLimit(String databaseName, String collectionName) {
        DB db = MongoDbUtils.getDB(mongo, databaseName);
        DBCollection collection = db.getCollection(collectionName);
        
        Integer limit = 20000; // ToDo: parameterize this
        Long longCount = collection.getCount();
        Integer totalCount = longCount.intValue();
        BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();
        
        // Auto created _id field has a date embedded in it 
        // The 1 sorts ascending (oldest to newest) and -1 sorts descending (newest to oldest.)
        DBObject dbo = new BasicDBObject();
        dbo.put("_id", 1); 
        
        if(totalCount > limit) {
            Iterator itr = collection.find(builder.get()).sort(dbo).limit(totalCount-limit).iterator();
            while (itr.hasNext()) {
                DBObject obj = (DBObject) itr.next();
//                logger.info("deleting object = " + obj.toString());
                collection.remove(obj);
            }
        }

    }
}
