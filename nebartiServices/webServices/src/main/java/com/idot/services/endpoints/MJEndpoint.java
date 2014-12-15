/*
 * Nebarti
 * Copyright 2013 All rights reserved.
 */
package com.idot.services.endpoints;

import com.nebarti.dataaccess.dao.MJTextDao;
import com.nebarti.dataaccess.domain.ClassificationSet;
import com.nebarti.dataaccess.domain.DataTable;
import com.nebarti.dataaccess.domain.DataTableRecord;
import com.nebarti.dataaccess.domain.MJText;
import com.mongodb.BasicDBObjectBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.support.GenericApplicationContext;

/**
 *
 * 
 */
@Path("/mjlegalization")
public class MJEndpoint {

    @Context
    private UriInfo uriInfo;
    private static final MJTextDao mjTextDao = new MJTextDao();
    static {
        GenericApplicationContext ctx = new GenericApplicationContext();
        BeanDefinitionBuilder bdb = BeanDefinitionBuilder
            .rootBeanDefinition("com.idot.jackson.JacksonMapperProvider")
            .setLazyInit(false);
        ctx.registerBeanDefinition("objectMapper", bdb.getBeanDefinition());
    }
    public static final Logger logger = Logger.getLogger(MJEndpoint.class.getName());

    /** 
     * Save classified text to the defined collection.
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response save(
            @QueryParam("validated") String validated,
            @QueryParam("useInModel") String useInModel,
            @QueryParam("classification") String classification,
            @QueryParam("text") String text) {
        URI uri;

        MJText mjtext = new MJText();
        mjtext.setValidated(Boolean.valueOf(validated));
        mjtext.setUseInModel(Boolean.valueOf(useInModel));
        mjtext.setClassification(classification);
        mjtext.setText(text);
        
        mjTextDao.save(mjtext);
        
        uri = uriInfo.getAbsolutePathBuilder().path(mjtext.getId().toString()).build();
        return Response.created(uri).build();
    }
    
    /**
     * Update the specified text document record.
     */
    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response update(
            @PathParam("id") String id, 
            String mjTextString) throws Exception {
        
        ObjectMapper mapper = new ObjectMapper();
        MJText mjtext = mapper.readValue(mjTextString, MJText.class);
        mjTextDao.update(mjtext);
        
        URI uri = uriInfo.getAbsolutePathBuilder().path(mjtext.getId().toString()).build();
        return Response.created(uri).build();
    }
    
    /**
     * Delete the text document record.
     */
    @DELETE
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void delete(
            @PathParam("id") String id) {
        mjTextDao.delete(id);
    }
    
    /**
     * Get current possible classifications.
     */
    @GET
    @Path("/classifications")
    @Produces(MediaType.APPLICATION_JSON)
    public ClassificationSet getClassifications() {
        return mjTextDao.getClassifications("fna"); // for, neutral, against
    }
    
    /**
     * Get an mjtext using id.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public MJText findById(
            @PathParam("id") String id) {
        return mjTextDao.findById(id);
    }
    
    /**
     * This method is constructed to received parameters from jQuery DataTables
     * calls. It needs to return a JSON object in the following form:
     * {  "sEcho":"1",
     *     "iTotalRecords":97,
     *     "iTotalDisplayRecords":3,
     *     "aaData":[
     *         {"id":"sdvrg947594", "classification":"neutral", "text":"text content", "validated":"false"},
     *         {"id":"mgkt83v8d0","classification":"against","text":"text content","validated":"true"}
     *     ]
     * }
     * 
     * 
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public DataTable getDocuments (
            @DefaultValue("1") @QueryParam("sEcho") String sEcho,
            @DefaultValue("0") @QueryParam("iDisplayStart") Integer offset,
            @DefaultValue("10") @QueryParam("iDisplayLength") Integer limit,
            @QueryParam("sSearch_1") String validated,
            @QueryParam("sSearch_2") String useInModel,
            @QueryParam("sSearch_3") String classification,
            @QueryParam("sSearch_4") String searchString) {
        
        DataTable dataTable = new DataTable();
        dataTable.setsEcho(sEcho);
        dataTable.setiTotalRecords(mjTextDao.count()); // total number of records in db
        
        // Construct the object used to define the search criteria
        BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();
        if (searchString == null || searchString.isEmpty()) {
            searchString =".*?";
        } 
        HashMap mapValues = new HashMap<String, Object>();
        mapValues.put("$regex", searchString);
        mapValues.put("$options", "i"); // case insensitive
        builder.add("text", mapValues);
        
        if (validated != null && !validated.isEmpty()) {
            builder.add("validated", Boolean.valueOf(validated));
        }
        
        if (useInModel != null && !useInModel.isEmpty()) {
            builder.add("useInModel", Boolean.valueOf(useInModel));
        }
        
        if (classification != null && !classification.isEmpty()) {
            builder.add("classification", classification);
        }
        
        List<DataTableRecord> records = new ArrayList<DataTableRecord>();
        
        List<MJText> documents = mjTextDao.findDocuments(limit, offset, builder);
        for (MJText document : documents) {
            DataTableRecord record = new DataTableRecord();
            record.setId(document.getId());
            record.setClassification(document.getClassification());
            record.setValidated(document.getValidated());
            record.setUseInModel(document.getUseInModel());
            String text = document.getText();
            text = text.replaceAll("'", "");
            record.setText(text);
            
            records.add(record);
        }
        
        dataTable.setiTotalDisplayRecords(mjTextDao.getNumberOfMatchingDocuments(builder)); // total records which can be displayed after filtering
        dataTable.setaaData(records);
        
        return dataTable;
    }
}
