/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.nebarti.dataaccess.dao;

import com.nebarti.dataaccess.domain.DataSource;
import com.mongodb.BasicDBObjectBuilder;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * Data access definitions and management for data source information.
 */
public class DataSourcesDao {
    MongoTemplate mongoTemplate;
    String collectionName = "datasources";

    public DataSourcesDao() {
        ApplicationContext ctx = new GenericXmlApplicationContext("mongo-config.xml");
        mongoTemplate = (MongoTemplate) ctx.getBean("ConfigParamsTemplate");
    }
    
    public synchronized List<DataSource> getAll() {
        BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();
        List<DataSource> dataSources = mongoTemplate.find (
                new Query(Criteria.where("name").exists(true)), 
                DataSource.class,
                "datasources"
                );
        
        return dataSources;
    }
    
}
