/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.idot.dataingest.twitter;

import com.idot.dataingest.processors.Processor;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 *
 */
public class TwitterProcessorExecutorService {
    private static final int CORE_POOL_SIZE = 1;
    private static final int MAX_POOL_SIZE = 3;
    private static final int QUEUE_CAPACITY = 3;
    private static final int KEEP_ALIVE_TIME = 30;
    private ProcessorThreadPoolExecutor executor;
    public static final Logger logger = Logger.getLogger(TwitterProcessorExecutorService.class.getName());

    public TwitterProcessorExecutorService() {
        executor = new ProcessorThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(QUEUE_CAPACITY),
                new RejectedTaskExecutionHandler());
    }

    public void startProcessor(Processor processor) {
        executor.submit(processor);
    }
    
    public void stopProcessor(String id) {
        executor.cancel(id);
    }

    public void shutdown() {
        executor.shutdown();
    }
 
    public boolean isProcessorRunning(String id) {
        Map<String, Processor> processorMap = executor.getProcessorMap();
        return processorMap.containsKey(id);
    }
}
