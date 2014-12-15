/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idot.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author khartig
 */
public class Properties {
    private static java.util.Properties properties = null;
    public static final Logger logger = Logger.getLogger(Properties.class.getName());

    private static void loadProperties() {
        if (properties == null) {
            properties = new java.util.Properties();
            try {
                properties.load(new FileInputStream("../config/application.properties"));
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }
    }
    
    public static String getProperty(String name) {
        loadProperties();
        return properties.getProperty(name);
    }
}
