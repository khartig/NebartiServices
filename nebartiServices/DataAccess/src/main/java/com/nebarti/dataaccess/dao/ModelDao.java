/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.nebarti.dataaccess.dao;

import com.nebarti.dataaccess.domain.Model;
import com.mongodb.BasicDBObjectBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * Data accessor for Model attribute data
 */
public class ModelDao {

    MongoTemplate mongoTemplate;
    String collectionName = "models";

    public ModelDao() {
        ApplicationContext ctx = new GenericXmlApplicationContext("mongo-config.xml");
        mongoTemplate = (MongoTemplate) ctx.getBean("ConfigParamsTemplate");
    }

    public synchronized List<Model> getAll() {
        BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();
        List<Model> models = mongoTemplate.find(
                new Query(Criteria.where("_id").exists(true)),
                Model.class,
                "models");

        return models;
    }
    
    public synchronized boolean exists(String modelName) {
        Model model;
        if (!(modelName == null) && !(modelName.equals(""))) {
            model = mongoTemplate.findOne(new Query(Criteria.where("name").is(modelName)), Model.class, collectionName);
            if (model == null || model.getId() == null) {
                return false;
            } else {
                return true;
            }
        } 
        
        return false;
    }

    public synchronized void save(Model model) {
        if (!mongoTemplate.collectionExists(collectionName)) {
            mongoTemplate.createCollection(collectionName);
        }

        String date = new Date().toString();
        model.setCreationDate(date);
        model.setUpdatedDate(date);
        mongoTemplate.save(model, collectionName);
    }

    public synchronized void update(Model model) {
        Update update = new Update();
        update.set("name", model.getName());
        update.set("visibleName", model.getVisibleName());
        update.set("category", model.getCategory());
        update.set("classifier", model.getClassifier());
        update.set("owner", model.getOwner());
        update.set("creationDate)", model.getCreationDate());
        update.set("updatedDate", new Date().toString());

        model = mongoTemplate.findAndModify(
                Query.query(Criteria.where("_id").is(new ObjectId(model.getId()))),
                update,
                Model.class,
                collectionName);
    }

    public synchronized void delete(String name) {
        if (!(name == null) || !(name.equals(""))) {
            mongoTemplate.remove(Query.query(Criteria.where("name").is(name)), collectionName);
        }
    }
    
    public synchronized Model getByName(String name) {
        Model model = null;
        if (!(name == null) && !(name.equals(""))) {
            model = mongoTemplate.findOne(new Query(Criteria.where("name").is(name)), Model.class, collectionName);
        }

        return model;
    }
    
    public synchronized List<Model> getByClassifierType(String classifierType) {
        List<Model> models = new ArrayList<Model>();
        if (classifierType != null && !(classifierType.equals(""))) {
            models = mongoTemplate.find(new Query(Criteria.where("classifier").is(classifierType)), Model.class, collectionName);
        }
        return models;
    }
}
