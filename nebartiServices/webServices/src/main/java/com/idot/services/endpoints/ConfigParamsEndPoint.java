/*
 * Nebarti
 * Copyright 2013 All rights reserved.
 */
package com.idot.services.endpoints;

import com.nebarti.dataaccess.dao.ClassificationSetDao;
import com.nebarti.dataaccess.dao.ClassifierCategoryDao;
import com.nebarti.dataaccess.dao.ClassifierDao;
import com.nebarti.dataaccess.dao.DataSourcesDao;
import com.nebarti.dataaccess.dao.ModelDao;
import com.nebarti.dataaccess.domain.ClassificationSet;
import com.nebarti.dataaccess.domain.Classifier;
import com.nebarti.dataaccess.domain.ClassifierCategory;
import com.nebarti.dataaccess.domain.DataSource;
import com.nebarti.dataaccess.domain.Model;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 */
@Path("/configparams")
public class ConfigParamsEndPoint {
    @Context
    private UriInfo uriInfo;
    private static final DataSourcesDao dataSourcesDao = new DataSourcesDao();
    private static final ClassificationSetDao classifierTypeDao = new ClassificationSetDao();
    private static final ModelDao modelDao = new ModelDao();
    private static final ClassifierDao classifierDao = new ClassifierDao();
    private static final ClassifierCategoryDao classifierCategoryDao = new ClassifierCategoryDao();
    public static final Logger logger = Logger.getLogger(ConfigParamsEndPoint.class.getName());
    
    /**
     * Get all current data sources used to pull text.
     */
    @GET
    @Path("/datasourcenames")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getDataSourceNames() {
        List<String> names = new ArrayList<String>();
        List<DataSource> dataSources = dataSourcesDao.getAll();
        for (DataSource dataSource : dataSources) {
            names.add(dataSource.getName());
        }
        
        return names;
    }
    
    /**
     * Get all the classifier types. 
     */
    @GET
    @Path("/classificationtypes")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ClassificationSet> getClassificationTypes() {
        return classifierTypeDao.getAll();
    }
    
    /**
     * Get the list of models and their attributes. 
     */
    @GET
    @Path("/models")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Model> getAllModels() {
        return modelDao.getAll();
    }
    
    /**
     * Get the list of models and their attributes. 
     */
    @GET
    @Path("/models/{classifierType}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Model> getModels(@PathParam("classifierType") String classifierType) {
        if (classifierType.equals("null") || 
            classifierType.equals("undefined") ||
            classifierType.equals("") ){
            return modelDao.getAll();
        } else {
            return modelDao.getByClassifierType(classifierType);
        }
    }
    
    /**
     * Get the list of all classifiers and their attributes. 
     */
    @GET
    @Path("/classifiers")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Classifier> getClassifiers() {
        return classifierDao.getAll();
    }
    
    /**
     * Get the list of classifiers and their attributes for a specified category. 
     */
    @GET
    @Path("/classifiersforcategory/{category_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Classifier> getClassifiersForCategory(@PathParam("category_id") String category_id) {
        return classifierDao.getClassifiersForCategory(category_id);
    }
    
    /**
     * Get the list of classifiers and their attributes for a specified category. 
     */
    @GET
    @Path("/classifiers/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Classifier getClassifiersById(@PathParam("id") String id) {
        return classifierDao.getClassifierById(id);
    }    
    
    /** 
     * Save classifier info to collection.
     */
    @POST
    @Path("/classifiers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Classifier saveClassifier(Classifier classifier) {     
        // if id is an empty string, an id has never been created.
        // set to null so mongo creates a new id
        if(classifier.getId().equals("")) {
            classifier.setId(null);
        }
        
        classifier.setName(classifier.getVisibleName().toLowerCase().replaceAll(" ", "_"));         
        classifierDao.save(classifier);

        return classifier;
    }
    
    /**
     * Get the list of classifier categories and their attributes. 
     */
    @GET
    @Path("/classifierCategories")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ClassifierCategory> getClassifierCategories() {
        return classifierCategoryDao.getAll();
    }
    
    /**
     * Delete the classifier with the corresponding id.
     */
    @DELETE
    @Path("/classifiers/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteClassifier(@PathParam("id") String id) {
        URI uri;
        classifierDao.deleteClassifier(id);
        
        uri = uriInfo.getAbsolutePathBuilder().path(id).build();
        return Response.created(uri).build();
    }
}
