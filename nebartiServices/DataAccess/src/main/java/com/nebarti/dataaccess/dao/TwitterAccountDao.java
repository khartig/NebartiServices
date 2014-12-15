/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.nebarti.dataaccess.dao;

import com.mongodb.BasicDBObjectBuilder;
import com.nebarti.dataaccess.domain.TwitterAccount;
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
 * Data accessor for TwitterAccount attribute data
 */
public class TwitterAccountDao {

    MongoTemplate mongoTemplate;
    String collectionName = "twitteraccounts";

    public TwitterAccountDao() {
        ApplicationContext ctx = new GenericXmlApplicationContext("mongo-config.xml");
        mongoTemplate = (MongoTemplate) ctx.getBean("ConfigParamsTemplate");
    }

    public synchronized List<TwitterAccount> getAll() {
        BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();
        List<TwitterAccount> accounts = mongoTemplate.find(
                new Query(Criteria.where("_id").exists(true)),
                TwitterAccount.class,
                "twitteraccounts");

        return accounts;
    }

    public synchronized Boolean save(TwitterAccount account) {
        Boolean saved = false;

        if (!mongoTemplate.collectionExists(collectionName)) {
            mongoTemplate.createCollection(collectionName);
        }

        if (getByUserID(account.getUserID()) == null) {
            String date = new Date().toString();
            account.setCreationDate(date);
            account.setUpdatedDate(date);
            mongoTemplate.save(account, collectionName);
            saved = true;
        }
        
        return saved;
    }

    public synchronized void update(TwitterAccount account) {
        Update update = new Update();
        update.set("userId", account.getUserID());
        update.set("consumerKey", account.getConsumerKey());
        update.set("consumerSecret", account.getConsumerSecret());
        update.set("accessToken", account.getAccessToken());
        update.set("accessTokenSecret", account.getAccessTokenSecret());
        update.set("creationDate)", account.getCreationDate());
        update.set("updatedDate", new Date().toString());

        account = mongoTemplate.findAndModify(
                Query.query(Criteria.where("_id").is(new ObjectId(account.getId()))),
                update,
                TwitterAccount.class,
                collectionName);
    }

    public synchronized void delete(String userID) {
        if (!(userID == null) || !(userID.equals(""))) {
            mongoTemplate.remove(Query.query(Criteria.where("userID").is(userID)), collectionName);
        }
    }

    public synchronized TwitterAccount getByUserID(String userID) {
        TwitterAccount account = null;
        if (!(userID == null) && !(userID.equals(""))) {
            account = mongoTemplate.findOne(new Query(Criteria.where("userID").is(userID)), TwitterAccount.class, collectionName);
        }

        return account;
    }
}
