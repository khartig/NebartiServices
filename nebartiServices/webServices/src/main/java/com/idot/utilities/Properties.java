/*
 * Nebarti
 * Copyright 2013 All rights reserved.
 */
package com.idot.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * 
 */
public class Properties {

    private static java.util.Properties properties = null;
    public static final Logger logger = Logger.getLogger(Properties.class.getName());

    private void loadProperties() {
        if (properties == null) {
            try {
                InputStream inputStream = this.getClass().getClassLoader()
                        .getResourceAsStream("nebarti-default.properties");
                properties = new java.util.Properties();
                properties.load(inputStream);
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }
    }

    public String getProperty(String name) {
        loadProperties();
        return properties.getProperty(name);
    }
}
