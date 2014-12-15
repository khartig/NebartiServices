/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.nebarti.dataaccess.dao;

import com.nebarti.dataaccess.domain.ClassifierCategory;
import com.mongodb.BasicDBObjectBuilder;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * 
 */
public class ClassifierCategoryDao {
    MongoTemplate mongoTemplate;
    String collectionName = "classifierCategories";
    
    public ClassifierCategoryDao() {
        ApplicationContext ctx = new GenericXmlApplicationContext("mongo-config.xml");
        mongoTemplate = (MongoTemplate) ctx.getBean("ConfigParamsTemplate");
    }
    
    public synchronized List<ClassifierCategory> getAll() {
        BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();
        List<ClassifierCategory> categories = mongoTemplate.find(
                new Query(Criteria.where("_id").exists(true)),
                ClassifierCategory.class,
                "classifierCategories");

        return categories;
    }

    public synchronized void save(ClassifierCategory classifierCategory) {
        if (!mongoTemplate.collectionExists(collectionName)) {
            mongoTemplate.createCollection(collectionName);
        }

        mongoTemplate.save(classifierCategory, collectionName);
    }    
}
