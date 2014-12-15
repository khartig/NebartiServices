/*
 * Nebarti
 * Copyright 2013 All rights reserved.
 */
package com.idot.services.endpoints;

import com.idot.dataingest.coreference.CoReferenceSummarizer;
import com.idot.dataingest.namedentities.NamedEntitySummarizer;
import com.idot.dataingest.references.ReferenceSummarizer;
import com.idot.dataingest.wordcount.WordCountSummarizer;
import com.nebarti.dataaccess.dao.ClassificationSetDao;
import com.nebarti.dataaccess.dao.GenericDao;
import com.nebarti.dataaccess.dao.ModelDao;
import com.nebarti.dataaccess.domain.ClassificationSet;
import com.nebarti.dataaccess.domain.ClassifiedText;
import com.nebarti.dataaccess.domain.CoReference;
import com.nebarti.dataaccess.domain.DataTable;
import com.nebarti.dataaccess.domain.DataTableRecord;
import com.nebarti.dataaccess.domain.ImageReference;
import com.nebarti.dataaccess.domain.Model;
import com.nebarti.dataaccess.domain.NamedEntity;
import com.nebarti.dataaccess.domain.ReclassifyModel;
import com.nebarti.dataaccess.domain.Reference;
import com.nebarti.dataaccess.domain.WordCount;
import com.idot.utilities.ModelBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import com.nebarti.dataaccess.domain.comparators.CoreferenceComparator;
import com.nebarti.dataaccess.domain.comparators.NamedEntityComparator;
import java.io.IOException;
import java.net.URI;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
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
import org.bson.types.ObjectId;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.data.mongodb.core.MongoAdmin;
import org.springframework.data.mongodb.core.MongoDbUtils;

/**
 *
 */
@Path("/models")
public class ModelsEndpoint {

    @Context
    private UriInfo uriInfo;
    private static Mongo mongo;
    private static MongoAdmin mongoAdmin;
    private static final ModelDao modelDao = new ModelDao();
    private static final ClassificationSetDao classificationSetDao = new ClassificationSetDao();
    private static final List<String> spamTitles = new ArrayList<String>();
    public static final Logger logger = Logger.getLogger(ModelsEndpoint.class.getName());

    static {
        GenericApplicationContext ctx = new GenericApplicationContext();
        BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition("com.idot.jackson.JacksonMapperProvider").setLazyInit(false);
        ctx.registerBeanDefinition("objectMapper", bdb.getBeanDefinition());
        try {
            mongo = new Mongo("127.0.0.1", 27017);
            mongoAdmin = new MongoAdmin(mongo);
        } catch (UnknownHostException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (MongoException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    /** 
     * Create new database and collections for new model data.
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(
            @FormParam("name") String name,
            @FormParam("visibleName") String visibleName,
            @FormParam("category") String category,
            @FormParam("classifier") String classifier,
            @FormParam("owner") String owner) {
        URI uri;

        Model model = new Model();
        model.setName(name);
        model.setVisibleName(visibleName);
        model.setCategory(category);
        model.setClassifier(classifier);
        model.setOwner(owner);
        model.setCreationDate(new Date().toString());
        model.setUpdatedDate(new Date().toString());

        if (!modelDao.exists(name)) {
            modelDao.save(model);
        } else {
            model = modelDao.getByName(name);
        }

        uri = uriInfo.getAbsolutePathBuilder().path(model.getId().toString()).build();

        // create new db for classified data and classifications definition
        mongoAdmin.createDatabase(name);

        DB db = MongoDbUtils.getDB(mongo, name);
        DBObject obj = new BasicDBObject();
//        obj.put("name", classifier);
        if (!db.collectionExists("sentiment")) {
            DBCollection classificationCollection = db.createCollection("sentiment", obj);
        }

        if (!db.collectionExists("classifications")) {
            DBCollection sentimentCollection = db.createCollection("classifications", obj);

            ClassificationSet classificationSet = classificationSetDao.getByName(classifier);
            obj = new BasicDBObject();
            obj.put("name", classificationSet.getName());
            obj.put("visibleName", classificationSet.getVisibleName());
            obj.put("classifications", classificationSet.getClassifications());
            GenericDao.save(name, "classifications", obj);
        }
        
        return Response.created(uri).build();
    }

    /**
     * Delete the database and collections for the model. Also delete the entry in 
     * the config_params/models collection
     */
    @DELETE
    @Path("/{name}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("name") String name) {
        modelDao.delete(name);
        DB db = MongoDbUtils.getDB(mongo, name);
        db.dropDatabase();

        return Response.ok().build();
    }

    /**
     * Rebuild the specified model with all the current classifications which are
     * tagged as Use in Model and Verified. Or reclassify only entries which are 
     * not tagged.
     */
    @PUT
    @Path("/{modelName}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response rebuild(
            @PathParam("modelName") String modelName,
            String reclassifyModelJSON) throws IOException {
        URI uri = null;

        ObjectMapper mapper = new ObjectMapper();
        ReclassifyModel reclassifyModel = mapper.readValue(reclassifyModelJSON, ReclassifyModel.class);
        ModelBuilder modelBuilder = new ModelBuilder();

        if (!reclassifyModel.getReclassify()) {
            modelBuilder.reclassify(modelName);
            logger.log(Level.INFO, "Finished reclassification of model = {0}", modelName);
        } else {
            modelBuilder.build(modelName);
            logger.log(Level.INFO, "Finished rebuild of model = {0}", modelName);
        }

        return Response.created(uri).build();
    }
    
    /**
     * Remove all documents from sentiment collection 
     * in defined model with duplicate text leaving one document with 
     * unique text.
     */
    @DELETE
    @Path("/removeduplicates/{modelName}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void deleteDuplicates(
            @PathParam("modelName") String modelName,
            String modelNameJSON) throws Exception {
        GenericDao.removeDuplicates(modelName, "sentiment");
    }

    /**
     * Get current possible classifications.
     */
    @GET
    @Path("/classifications/{modelName}")
    @Produces(MediaType.APPLICATION_JSON)
    public ClassificationSet getClassifications(@PathParam("modelName") String modelName) {
        ClassificationSet classificationSet = GenericDao.getClassifications(modelName, "classifications");
        return classificationSet;
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
    @Path("/documents/{modelName}")
    @Produces(MediaType.APPLICATION_JSON)
    public DataTable findDocuments(
            @PathParam("modelName") String modelName,
            @DefaultValue("1") @QueryParam("sEcho") String sEcho,
            @DefaultValue("0") @QueryParam("iDisplayStart") Integer offset,
            @DefaultValue("10") @QueryParam("iDisplayLength") Integer limit,
            @QueryParam("sSearch_1") String validated,
            @QueryParam("sSearch_2") String useInModel,
            @QueryParam("sSearch_3") String classification,
            @QueryParam("sSearch_4") String searchString) {

        DataTable dataTable = new DataTable();
        dataTable.setsEcho(sEcho);
        dataTable.setiTotalRecords(GenericDao.getCount(modelName, "sentiment")); // total number of documents in sentiment collection for the model

        // Construct the object used to define the search criteria
        BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();
        if (searchString == null || searchString.isEmpty()) {
            searchString = ".*?";
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

        List<ClassifiedText> documents = GenericDao.findDocuments(modelName, "sentiment", modelName, limit, offset, builder);
        for (ClassifiedText document : documents) {
            DataTableRecord record = new DataTableRecord();
            record.setId(document.getId());
            record.setClassification(document.getClassification());
            record.setScore(document.getScore());
            record.setValidated(document.getValidated());
            record.setUseInModel(document.getUseInModel());
            record.setDate(document.getCreatedDate());
            String text = document.getText();
            text = text.replaceAll("'", "");
            record.setText(text);
            record.setLatitude(document.getLatitude());
            record.setLongitude(document.getLongitude());

            records.add(record);
        }

        dataTable.setiTotalDisplayRecords(GenericDao.getNumberOfMatchingDocuments(modelName, "sentiment", builder)); // total records which can be displayed after filtering
        dataTable.setaaData(records);
        
        return dataTable;
    }
    
    /**
     * Get documents for the ids passed into the method.
     */
    @POST
    @Path("/documents/{modelName}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces(MediaType.APPLICATION_JSON)
    public List<ClassifiedText> getDocumentsById(
            @PathParam("modelName") String modelName,
            List<String> ids) {
        Integer limit = 100;
        Integer offset = 0;
        
        BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();
        
        HashMap mapValues = new HashMap<String, Object>();
        mapValues.put("$in", ids);
        builder.add("_id", mapValues);
        List<ClassifiedText> documents = GenericDao.findDocuments(modelName, "sentiment", modelName, limit, offset, builder);
       
        
        return documents;
    }

    /** 
     * Save classified text to Sentiment collection in model database.
     */
    @POST
    @Path("/classifiedtext")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveClassifiedText(
            @QueryParam("validated") Boolean validated,
            @QueryParam("useInModel") Boolean useInModel,
            @QueryParam("classification") String classification,
            @QueryParam("score") String score,
            @QueryParam("latitude") String latitude,
            @QueryParam("longitude") String longitude,
            @QueryParam("gender") String gender,
            @QueryParam("age") String age,
            @QueryParam("modelName") String modelName,
            @QueryParam("text") String text) {
        URI uri = null;

        if (validated == null) {
            validated = false;
        }
        if (useInModel == null) {
            useInModel = false;
        }
        if (classification == null) {
            classification = "";
        }
        if (score == null) {
            score = "-1000.00";
        }
        if (latitude == null) {
            latitude = "";
        }
        if (longitude == null) {
            longitude = "";
        }
        if (gender == null) {
            gender = "";
        }
        if (age == null) {
            age = "";
        }
        if (text == null) {
            text = "";
        }
        if (modelName == null) {
            return null;
        }

        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = dateFormatGmt.format(new Date());

        BasicDBObject dbo = new BasicDBObject();

        dbo.put("createdDate", date);
        dbo.put("updatedDate", date);
        dbo.put("validated", validated);
        dbo.put("useInModel", useInModel);
        dbo.put("text", text);
        dbo.put("latitude", latitude);
        dbo.put("longitude", longitude);
        dbo.put("classification", classification);
        dbo.put("score", score);
        dbo.put("gender", gender);
        dbo.put("age", age);

        WriteResult result = GenericDao.save(modelName, "sentiment", dbo);

//        uri = uriInfo.getAbsolutePathBuilder().path(dbo.getId().toString()).build();
        return Response.created(uri).build();
    }

    /**
     * Update the specified document.
     */
    @PUT
    @Path("/classifiedtext/{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response update(
            @PathParam("id") String id,
            String classifiedTextJSON) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        ClassifiedText classifiedText = mapper.readValue(classifiedTextJSON, ClassifiedText.class);

        if (classifiedText.getModelName() == null) {
            return null; // need model name to match to db name
        }
        BasicDBObject dbo = createClassifiedTextDbo(
                new ObjectId(id),
                classifiedText.getValidated(),
                classifiedText.getUseInModel(),
                classifiedText.getClassification(),
                classifiedText.getScore(),
                classifiedText.getLatitude(),
                classifiedText.getLongitude(),
                classifiedText.getGender(),
                classifiedText.getAge(),
                classifiedText.getText());

        DBObject updatedObject = GenericDao.update(classifiedText.getModelName(), "sentiment", dbo);

        URI uri = uriInfo.getAbsolutePathBuilder().build();
        return Response.created(uri).build();
    }

    /**
     * Delete the specified document.
     */
    @DELETE
    @Path("/classifiedtext/{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void delete(
            @PathParam("id") String id,
            String modelNameJSON) throws Exception {
        
        ObjectMapper mapper = new ObjectMapper();
        ModelName modelName = mapper.readValue(modelNameJSON, ModelName.class);
        GenericDao.delete(modelName.modelName, "sentiment", id);
    }

    @GET
    @Path("/wordcount/{modelName}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<WordCount> getWordCount(
            @PathParam("modelName") String modelName,
            @DefaultValue("0") @QueryParam("offset") Integer offset,
            @DefaultValue("0") @QueryParam("limit") Integer limit,
            @DefaultValue("false") @QueryParam("usePrecalculated") String usePrecalculatedStr) {

        BasicDBObjectBuilder builder;
        List<WordCount> wordCounts = new ArrayList<WordCount>();
        Boolean usePrecalculated = Boolean.valueOf(usePrecalculatedStr);

        if (usePrecalculated) {
            builder = BasicDBObjectBuilder.start();
            wordCounts = GenericDao.getWordCounts(modelName, "wordcounts", offset, limit, builder);
        } else {
            // calculate word counts, save to database and get results back
            wordCounts = WordCountSummarizer.saveWordCount(modelName, limit);
        }

        return wordCounts;
    }

    @GET
    @Path("/coreferences/{modelName}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CoReference> getCoreferences(
            @PathParam("modelName") String modelName,
            @DefaultValue("0") @QueryParam("offset") Integer offset,
            @DefaultValue("0") @QueryParam("limit") Integer limit,
            @DefaultValue("2") @QueryParam("count") Integer wordCount,
            @DefaultValue("false") @QueryParam("usePrecalculated") String usePrecalculatedStr,
            @DefaultValue("all") @QueryParam("classification") String classification) {

        if (wordCount < 2) {
            wordCount = 2;
        } else if (wordCount > 3) {
            wordCount = 3;
        }

        GenericDao genericDao;
        BasicDBObjectBuilder builder;
        
        List<CoReference> coreferences = new ArrayList<CoReference>();
        CoreferenceComparator comparator = new CoreferenceComparator();
        TreeSet<CoReference> sorted_set = new TreeSet(comparator);
        
        Boolean usePrecalculated = Boolean.valueOf(usePrecalculatedStr);

        if (usePrecalculated) {
            builder = BasicDBObjectBuilder.start();
            builder.add("count", wordCount);
            coreferences = GenericDao.getCoreferences(modelName, "coreferences", offset, limit, builder);
        } else {           
            // assumption for now is that 2 word corefs is called first which requires 
            // db collection be rebuilt, 3 word coref is called second and no rebuilt.
            // ToDo: add this as parameter on web service call.
            Boolean rebuild = true;
            if (wordCount == 3) {
                rebuild = false; 
            }
            
            coreferences = CoReferenceSummarizer.saveCoReferences(modelName, limit, wordCount, rebuild);         
        }

        return coreferences;
    }

    @GET
    @Path("/entities/{modelName}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<NamedEntity> getNamedEntities(
            @PathParam("modelName") String modelName,
            @DefaultValue("0") @QueryParam("offset") Integer offset,
            @DefaultValue("0") @QueryParam("limit") Integer limit,
            @DefaultValue("all") @QueryParam("type") String entityType,
            @DefaultValue("false") @QueryParam("usePrecalculated") String usePrecalculatedStr,
            @DefaultValue("all") @QueryParam("classification") String classification) {

        BasicDBObjectBuilder builder;

        List entitySet = new ArrayList<NamedEntity>();
        HashMap<String, NamedEntity> namedEntities = new HashMap<String, NamedEntity>();
        NamedEntityComparator comparator = new NamedEntityComparator(namedEntities);
        TreeMap<String, NamedEntity> sorted_map = new TreeMap(comparator);

        Boolean usePrecalculated = Boolean.valueOf(usePrecalculatedStr);

        if (usePrecalculated) {
            builder = BasicDBObjectBuilder.start();
            if (!entityType.equals("all")) {
                builder.add("type", entityType);
            }
            entitySet = GenericDao.getNamedEntities(modelName, "namedentities", offset, limit, builder);
            
            return entitySet;
        } else {
            // calculate and save entities in db collection. then return values
            entitySet = NamedEntitySummarizer.saveEntities(modelName, limit);            
            return entitySet;
        }

    }
    
    /**
     * Update URL and image references.
     */
    @PUT
    @Path("/updatereferences/{modelName}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response updateReferences(
            @PathParam("modelName") String modelName,
            @DefaultValue("0") @QueryParam("limit") Integer limit) {
        
        // Calculates and saves web page and image urls extracted from tweets.
        // Both are done together so only one pass through document is made.
        // ToDo: maybe split web and image url reference calculation out as separate calls.
        ReferenceSummarizer.saveReferences(modelName, limit);

        URI uri = uriInfo.getAbsolutePathBuilder().build();
        return Response.created(uri).build();
    }

    @GET
    @Path("/references/{modelName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Reference> getReferences(
            @PathParam("modelName") String modelName,
            @DefaultValue("0") @QueryParam("offset") Integer offset,
            @DefaultValue("0") @QueryParam("limit") Integer limit,
            @DefaultValue("all") @QueryParam("classification") String classification) {

        BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();

        Set<Reference> references;
        builder = BasicDBObjectBuilder.start();
        references = GenericDao.getReferences(modelName, "references", offset, limit, builder);
        
        return references;
    }

    @GET
    @Path("/images/{modelName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<ImageReference> getImages(
            @PathParam("modelName") String modelName,
            @DefaultValue("0") @QueryParam("offset") Integer offset,
            @DefaultValue("0") @QueryParam("limit") Integer limit,
            @DefaultValue("all") @QueryParam("classification") String classification) {
        BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();

        List<ImageReference> images;
        builder = BasicDBObjectBuilder.start();
        images = GenericDao.getImages(modelName, "images", offset, limit, builder);
        
        return images;
    }

    private BasicDBObject createClassifiedTextDbo(
            ObjectId id,
            Boolean validated, Boolean useInModel,
            String classification, String score,
            String latitude, String longitude,
            String gender, String age, String text) {

        if (validated == null) {
            validated = false;
        }
        if (useInModel == null) {
            useInModel = false;
        }
        if (classification == null) {
            classification = "";
        }
        if (score == null) {
            score = "0.0";
        }
        if (latitude == null) {
            latitude = "";
        }
        if (longitude == null) {
            longitude = "";
        }
        if (gender == null) {
            gender = "";
        }
        if (age == null) {
            age = "";
        }
        if (text == null) {
            text = "";
        }

        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = dateFormatGmt.format(new Date());

        BasicDBObject dbo = new BasicDBObject();

        dbo.put("_id", id);
        dbo.put("updatedDate", date);
        dbo.put("validated", validated);
        dbo.put("useInModel", useInModel);
        dbo.put("text", text);
        dbo.put("latitude", latitude);
        dbo.put("longitude", longitude);
        dbo.put("classification", classification);
        dbo.put("score", score);
        dbo.put("gender", gender);
        dbo.put("age", age);

        return dbo;
    }
}

class ModelName {
    public String modelName;
}
