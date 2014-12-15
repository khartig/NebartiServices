/*
 * Nebarti
 * Copyright © 2012All rights reserved.
 */
package com.nebarti.dataaccess.dao;

import com.nebarti.dataaccess.domain.Classifier;
import com.nebarti.dataaccess.domain.ClassifierCategory;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 *
 */
public class ClassifierDao {

    MongoTemplate mongoTemplate;
    String collectionName = "classifiers";

    public ClassifierDao() {
        ApplicationContext ctx = new GenericXmlApplicationContext("mongo-config.xml");
        mongoTemplate = (MongoTemplate) ctx.getBean("ConfigParamsTemplate");
    }

    public synchronized List<Classifier> getAll() {
        List<Classifier> classifiers = mongoTemplate.find(
                new Query(Criteria.where("_id").exists(true)),
                Classifier.class,
                "classifiers");

        return classifiers;
    }

    public List<Classifier> getClassifiersForCategory(String category_id) {
        ClassifierCategory category = mongoTemplate.findById(category_id, ClassifierCategory.class, "classifierCategories");
        List<Classifier> classifiers = mongoTemplate.find(
                new Query(Criteria.where("category").is(category.getName())),
                Classifier.class,
                "classifiers");

        return classifiers;
    }

    public synchronized void save(Classifier classifier) {
        if (!mongoTemplate.collectionExists(collectionName)) {
            mongoTemplate.createCollection(collectionName);
        }

        mongoTemplate.save(classifier, collectionName);
    }

    public Classifier getClassifierById(String id) {
        Classifier classifier = null;
        if (!(id == null) && !(id.equals(""))) {
            classifier = mongoTemplate.findById(id, Classifier.class, collectionName);
        }

        return classifier;
    }
    
    public Classifier getClassifierByModelName(String modelName) {
        Classifier classifier = null;
        if (!(modelName == null) && !(modelName.equals(""))) {
            classifier = mongoTemplate.findOne(
                new Query(Criteria.where("name").is(modelName)),
                Classifier.class,
                "classifiers");
        }
        
        return classifier;
    }

    public void deleteClassifier(String id) {
        if (!(id == null) && !(id.equals(""))) {
            mongoTemplate.remove(
                    new Query(Criteria.where("_id").is(new ObjectId(id))),
                    collectionName);
        }
    }
}
