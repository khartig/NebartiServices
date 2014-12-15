/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.idot.dataingest.twitter;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class RejectedTaskExecutionHandler implements RejectedExecutionHandler {

    public static final Logger logger = Logger.getLogger(RejectedTaskExecutionHandler.class.getName());

    @Override
    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
        logger.log(Level.INFO, "{0} : has been rejected", runnable.toString());
    }
}
