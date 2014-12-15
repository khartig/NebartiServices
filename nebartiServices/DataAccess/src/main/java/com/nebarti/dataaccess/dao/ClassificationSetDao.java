/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.nebarti.dataaccess.dao;

import com.nebarti.dataaccess.domain.ClassificationSet;
import com.mongodb.BasicDBObjectBuilder;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * Data access for classifier type information.
 */
public class ClassificationSetDao {
    MongoTemplate mongoTemplate;
    String collectionName = "classifications";

    public ClassificationSetDao() {
        ApplicationContext ctx = new GenericXmlApplicationContext("mongo-config.xml");
        mongoTemplate = (MongoTemplate) ctx.getBean("ConfigParamsTemplate");
    }
    
    public synchronized List<ClassificationSet> getAll() {
        BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();
        List<ClassificationSet> classifierTypes = mongoTemplate.find (
                new Query(Criteria.where("_id").exists(true)), 
                ClassificationSet.class,
                "classifications"
                );
        
        return classifierTypes;
    }
    
    public synchronized ClassificationSet getByName(String name) {
        ClassificationSet classificationSet = null;
        if (!(name == null) && !(name.equals(""))) {
            classificationSet = mongoTemplate.findOne(new Query(Criteria.where("name").is(name)), ClassificationSet.class, collectionName);
        }

        return classificationSet;
    }
    
}
