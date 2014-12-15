/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.idot.dataingest;

import com.idot.dataingest.tasks.StateChangeWatcher;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Monitors classifier controller data in Mongo for change in data feed switch setting.
 * If the switch is true a processor is started, if it's false the process is canceled. 
 * This should really be event driven on state change.
 */
public class DataSourceManager {

    Thread watcherThread;
    public static final Logger logger = Logger.getLogger(DataSourceManager.class.getName());

    public DataSourceManager() {
    }

    private void startMonitoring() {
        StateChangeWatcher watcher = new StateChangeWatcher();
        watcherThread = new Thread(watcher);
        watcherThread.setName("State Change Watcher Thread");
        watcherThread.start();

        while (true) {
            try {
                Thread.sleep(15000);
            } catch (InterruptedException ex) {
                logger.log(Level.SEVERE, null, ex);
                watcherThread.interrupt();
            }
        }
    }

    public static void main(String... args) {
        DataSourceManager manager = new DataSourceManager();
        manager.startMonitoring();
    }
}
