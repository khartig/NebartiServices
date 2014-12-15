/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.idot.dataingest.schedulers.dbcleaner;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

/**
 *
 */
public class DBCleanerTaskListener implements JobListener {

    public static final String TRIGGER_NAME = "DBCleanerTrigger";
    public static final Logger logger = Logger.getLogger(DBCleanerTaskListener.class.getName());

    @Override
    public String getName() {
        return TRIGGER_NAME;
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        logger.log(Level.INFO, "Job is going to be executed: {0}", context.getJobDetail().getKey().toString());
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        logger.log(Level.INFO, "Job is vetoed by trigger: {0}", context.getJobDetail().getKey().toString());
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        logger.log(Level.INFO, "Exception thrown by: {0} Exception: {1}", new Object[]{context.getJobDetail().getKey().toString(), jobException.getMessage()});
        logger.log(Level.INFO, jobException.getLocalizedMessage(), jobException);
    }

}
