/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.idot.dataingest.schedulers.summarizer;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.KeyMatcher;

/**
 *
 */
public class SummarizerScheduler {
    private static final com.idot.dataingest.utilities.Properties properties = new com.idot.dataingest.utilities.Properties();
    private static Integer taskInterval;
    public static final Logger logger = Logger.getLogger(SummarizerScheduler.class.getName());
    
    static {
        
        String taskIntervalStr = properties.getProperty("task.summarizer.interval");
        if (taskIntervalStr == null) {
            taskInterval = 30;
        } else {
            taskInterval = Integer.parseInt(taskIntervalStr);
        }
    }

    public static void main(String[] args) {

        // The state of the job.  
        SummarizerJobContext jobContext = new SummarizerJobContext();
        
        // The value or transfer object provided by Quartz that contains the  
        // state of the job. Save it in JobDetail or Trigger.  
        JobDataMap jobDataMap = new JobDataMap();  
        jobDataMap.put("SummarizerJobContext", jobContext);
        
        // Create an identifier for the job.  
        JobKey jobKey = new JobKey("summarizerJobId", "group1");

        // define the job and tie it to the Job class
        JobDetail job = JobBuilder.newJob(SummarizerTask.class).
                    withIdentity(jobKey).usingJobData(jobDataMap).
                    build();

        // Trigger the job to run now, and then every so many minutes
        Trigger trigger = TriggerBuilder.newTrigger().
                    withIdentity("summarizerTriggerId", "group1").
                    startNow().
                    withSchedule(
                        SimpleScheduleBuilder.simpleSchedule().
                            withIntervalInMinutes(taskInterval).repeatForever()).
                    build();
        
        SummarizerTaskListener taskListener = new SummarizerTaskListener();

        try {
            // Grab the Scheduler instance from the Factory
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            // Tell quartz to schedule the job using our trigger
            scheduler.scheduleJob(job, trigger);
            
            // Tell scheduler to listen for jobs with a particular key.  
            scheduler.getListenerManager().addJobListener(  
                taskListener,  
                KeyMatcher.keyEquals(jobKey)); 

            // and start it off
            scheduler.startDelayed(15);

        } catch (SchedulerException se) {
            logger.log(Level.SEVERE, se.getLocalizedMessage(), se);
        }

        while (true) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }
    }
}
