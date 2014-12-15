/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.idot.dataingest.twitter;

import com.idot.dataingest.processors.Processor;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class ProcessorThreadPoolExecutor extends ThreadPoolExecutor {

    private Map<String, Processor> processorMap = new HashMap<String, Processor>();
    public static final Logger logger = Logger.getLogger(ProcessorThreadPoolExecutor.class.getName());

    public ProcessorThreadPoolExecutor(
            int corePoolSize, int maximumPoolSize, long keepAliveTime,
            TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public Map<String, Processor> getProcessorMap() {
        return processorMap;
    }
    
    public void submit(Processor processor) {
        Future<?> future = super.submit(processor);
        processorMap.put(processor.getName(), processor);
    }
    
    public void cancel(String name) {
        Processor processor = processorMap.get(name);
        if (processor != null) {
            processor.cancel(true);
            processorMap.remove(name);
            purge();
        } else {
            logger.log(Level.WARNING, "Processor name {0} doesn't exist.", name);
        }
    }
    
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
    }
}
