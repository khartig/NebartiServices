/*
 * Nebarti 
 * Copyright © 2012-2013. All rights reserved.
 */
package com.idot.dataingest.namedentities.dictionaries;

/**
 *
 *
 */
public class CityDictionary extends Dictionary {
    
    public CityDictionary(String propertyName) {
        super(propertyName);
        type = DictionaryType.CITY;
        
        while (scanner.hasNext()) {
            values.add(scanner.next());
        }
    }
}
