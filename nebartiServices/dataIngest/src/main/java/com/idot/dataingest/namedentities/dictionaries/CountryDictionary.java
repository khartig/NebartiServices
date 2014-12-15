/*
 * Nebarti 
 * Copyright © 2012-2013. All rights reserved.
 */
package com.idot.dataingest.namedentities.dictionaries;

/**
 *
 *
 */
public class CountryDictionary extends Dictionary {
    
    public CountryDictionary(String propertyName) {
        super(propertyName);
        type = DictionaryType.COUNTRY;
        
        while (scanner.hasNext()) {
            values.add(scanner.next());
        }
    }
}
