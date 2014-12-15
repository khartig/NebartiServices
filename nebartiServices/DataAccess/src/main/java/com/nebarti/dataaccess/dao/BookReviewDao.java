/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.nebarti.dataaccess.dao;

import com.nebarti.dataaccess.domain.BookReview;
import java.util.List;
import java.util.Set;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 *
 * 
 */
public class BookReviewDao {
    
    MongoTemplate mongoTemplate;
    
    public BookReviewDao() {
            ApplicationContext ctx = new GenericXmlApplicationContext("mongo-config.xml");
            mongoTemplate = (MongoTemplate) ctx.getBean("mongoTemplate");
    }
    
    public synchronized void save(BookReview review) {
        int maxLength = 50;
        
        String collectionName = review.getTitle().
                replaceAll("[\\(]", "_").
                replaceAll("[\\)]", "_").
                replaceAll("[\\.]", "").
                replaceAll(",", "");
        collectionName = collectionName.replaceAll("__", "_");
        
        if (collectionName.length() > maxLength) {
            collectionName = collectionName.substring(0, maxLength-1);
        }
        
        if (collectionName.endsWith("_")) {
            collectionName = collectionName.substring(0, collectionName.length()-2);
        }
        
        if( !mongoTemplate.collectionExists(collectionName) ) {
            mongoTemplate.createCollection(collectionName);
        }
        
        mongoTemplate.save(review, collectionName);
    }
    
    public synchronized Set<String> listBooks() {
        return mongoTemplate.getCollectionNames();
    }
    
    public synchronized List<BookReview> findByTitle(String title) {
        title = title.replaceAll(" ", "_");
        List<BookReview> reviews = mongoTemplate.findAll(BookReview.class, title);
        return reviews;
    }

}
