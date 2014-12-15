/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.nebarti.dataaccess.dao;

import com.nebarti.dataaccess.domain.ClassificationSet;
import com.nebarti.dataaccess.domain.MJText;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 *
 * 
 */
public class MJTextDao {

    MongoTemplate mongoTemplate;
    String collectionName = "sentiment";

    public MJTextDao() {
        ApplicationContext ctx = new GenericXmlApplicationContext("mongo-config.xml");
        mongoTemplate = (MongoTemplate) ctx.getBean("MJTextMongoTemplate");
    }
    
    /**
     * Get total number of documents in the collection that have an _id key.
     * 
     * @return the number of documents
     */
    public synchronized Integer count() {
        // if the db has greater than the max int value, have to do something different
        Long longCount = mongoTemplate.count(Query.query(Criteria.where("_id").exists(true)), collectionName);
        Integer count = longCount.intValue();
        return count;
    }

    public synchronized void save(MJText mjtext) {
        if (mjtext.getClassification() == null || mjtext.getClassification().equals("")) {
            mjtext.setClassification("neutral");
        }

        if (!mongoTemplate.collectionExists(collectionName)) {
            mongoTemplate.createCollection(collectionName);
        }

        mongoTemplate.save(mjtext, collectionName);
    }

    public synchronized void update(MJText mjtext) {
        Update update = new Update();
        update.set("validated", mjtext.getValidated());
        update.set("classification", mjtext.getClassification());
        update.set("useInModel", mjtext.getUseInModel());
        update.set("updatedDate", new Date().toString());
        update.set("text", mjtext.getText());
        
        mjtext = mongoTemplate.findAndModify(
                Query.query(Criteria.where("_id").is(new ObjectId(mjtext.getId()))), 
                update,
                MJText.class,
                collectionName);
    }

    public synchronized void delete(String id) {
        if (!(id == null) || !(id.equals(""))) {
            mongoTemplate.remove(
                    Query.query(Criteria.where("_id").is(new ObjectId(id))),
                    collectionName);
        }
    }

    public synchronized MJText findById(String id) {
        MJText mjtext = null;
        if (!(id == null) || !(id.equals(""))) {
            mjtext = mongoTemplate.findById(id, MJText.class, collectionName);
        }

        return mjtext;
    }
    
    public synchronized Integer getNumberOfMatchingDocuments(BasicDBObjectBuilder builder) {        
        Long longCount = mongoTemplate.getCollection(collectionName).getCount(builder.get());
        Integer count = longCount.intValue();
        return count;
    }

    public synchronized List<MJText> findDocuments(Integer limit, Integer offset, BasicDBObjectBuilder builder) {
        MJText mjtext;
        List<MJText> documents = new ArrayList<MJText>();
        
        Iterator itr = mongoTemplate.getCollection(collectionName).find(builder.get()).skip(offset).limit(limit).iterator();

        while (itr.hasNext()) {
            DBObject obj = (DBObject) itr.next();

            mjtext = new MJText();
            mjtext.setId(obj.get("_id").toString());
            mjtext.setClassification(obj.get("classification").toString());
            mjtext.setAge(obj.get("age").toString());
            mjtext.setCreatedDate(obj.get("createdDate").toString());
            mjtext.setUpdatedDate(obj.get("updatedDate").toString());
            mjtext.setValidated(Boolean.valueOf(obj.get("validated").toString()));
            mjtext.setUseInModel(Boolean.valueOf(obj.get("useInModel").toString()));
            mjtext.setLatitude(obj.get("latitude").toString());
            mjtext.setLongitude(obj.get("longitude").toString());
            mjtext.setGender(obj.get("gender").toString());

            String text = obj.get("text").toString();
            text = text.replaceAll("\n", " ").replaceAll("\"", "'");
            mjtext.setText(text);

            documents.add(mjtext);
        }

        return documents;
    }

    public synchronized ClassificationSet getClassifications(String groupName) {
        BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();
        builder.add("name", groupName);
        ClassificationSet classifications = mongoTemplate.findOne(
                new Query(Criteria.where("name").is(groupName)), 
                ClassificationSet.class,
                "classifications"
                );

        return classifications;
    }
}
