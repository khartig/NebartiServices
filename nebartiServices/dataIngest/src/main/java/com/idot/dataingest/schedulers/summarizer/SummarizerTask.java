/*
 * Nebarti
 * Copyright © 2012-2013 All rights reserved.
 */
package com.idot.dataingest.schedulers.summarizer;

import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Pulls text data stored for a topic and extracts entities to be stored in the 
 * model's db collections. The following entities are extracted:
 *     - word counts
 *     - 2 word co-references
 *     - 3 word co-references
 *     - famous people
 *     - images
 *     - popular web sites
 * 
 * The data stored in the collection is to eliminate processing each request from a
 * webapp to calculate the values. This program should be run on a periodic
 * basis for each data set currently ingesting data.
 */
public class SummarizerTask implements Job {

    @Override
    public void execute(JobExecutionContext executionContext) throws JobExecutionException {
        try {
            // Retrieve the state object.  
            SummarizerJobContext jobContext = (SummarizerJobContext) executionContext.getJobDetail().
                    getJobDataMap().get("SummarizerJobContext");

            // Update state.  
            jobContext.setState(new Date().toString());
            
            Summarizer.summarizeAndSave();
            
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }
}
