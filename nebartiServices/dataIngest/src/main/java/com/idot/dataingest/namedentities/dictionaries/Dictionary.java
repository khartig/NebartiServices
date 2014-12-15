/*
 * Nebarti 
 * Copyright © 2012-2013. All rights reserved.
 */
package com.idot.dataingest.namedentities.dictionaries;

import com.idot.dataingest.utilities.Properties;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Logger;

/**
 *
 * 
 */
public abstract class Dictionary {

    protected Set<String> values = new HashSet<String>();
    protected DictionaryType type;
    protected final Scanner scanner;

    public static enum DictionaryType {

        COMPANY("COMPANY"), PERSON("PERSON"), STATE("STATE"), COUNTRY("COUNTRY"),
        FAMOUS_PEOPLE("FAMOUS_PEOPLE"), CITY("CITY");
        private final String name;

        private DictionaryType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
    public static final Logger logger = Logger.getLogger(Dictionary.class.getName());

    public Dictionary(String propertyName) {

        Properties properties = new Properties();
        String property = properties.getProperty(propertyName);
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(properties.getProperty(propertyName));

        scanner = new Scanner(inputStream);
        scanner.useDelimiter("\\n");

    }

    public Collection<String> getValues() {
        return values;
    }

    public DictionaryType getType() {
        return type;
    }
    
}
