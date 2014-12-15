/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.idot.dataingest.schedulers.dbcleaner;

import com.nebarti.dataaccess.dao.GenericDao;
import com.nebarti.dataaccess.dao.ModelDao;
import com.nebarti.dataaccess.domain.Model;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Check all databases to make sure they are within count limits.
 * For now delete all oldest records more than 20K (value set in GenericDAO) in sentiment collection 
 * for each database.
 */
public class DBCleanerTask implements Job {

    private static final ModelDao modelDao = new ModelDao();
    public static final Logger logger = Logger.getLogger(DBCleanerTask.class.getName());

    private static void cleanDBsToRecordLimit(List<Model> models) {
        for (Model model : models) {
            GenericDao.cleanToRecordLimit(model.getName(), "sentiment");
        }
    }

    @Override
    public void execute(JobExecutionContext executionContext) throws JobExecutionException {
        try {
            // Retrieve the state object.  
            DBCleanerJobContext jobContext = (DBCleanerJobContext) executionContext.getJobDetail().
                    getJobDataMap().get("DBCleanerJobContext");

            // Update state.  
            jobContext.setState(new Date().toString());

            List<Model> models = modelDao.getAll();
            cleanDBsToRecordLimit(models);
            logger.log(Level.INFO, "Finished cleaning databases.");

        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // get and loop through each database
        List<Model> models = modelDao.getAll();
        cleanDBsToRecordLimit(models);
    }
}
