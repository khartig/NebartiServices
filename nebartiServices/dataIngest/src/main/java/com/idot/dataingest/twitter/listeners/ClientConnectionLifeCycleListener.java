/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.idot.dataingest.twitter.listeners;

import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.ConnectionLifeCycleListener;

/**
 *
 */
public class ClientConnectionLifeCycleListener implements ConnectionLifeCycleListener {
    String id; // id that matchess the processor id which should match the classfier id
    public static final Logger logger = Logger.getLogger(ClientConnectionLifeCycleListener.class.getName());
    
    public void setId(String id) {
        this.id = id;
    }
    
    @Override
    public void onConnect() {
        logger.log(Level.INFO, "{0} twitter stream connection listener connected.", id);
    }

    @Override
    public void onDisconnect() {
        logger.log(Level.INFO, "{0} twitter stream connection listener disconnected.", id);
    }

    @Override
    public void onCleanUp() {
        logger.log(Level.INFO, "{0} twitter stream connection listener cleaning up.", id);
    }
    
}
