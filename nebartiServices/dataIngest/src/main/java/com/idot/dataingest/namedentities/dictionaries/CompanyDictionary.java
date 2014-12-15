/*
 * Nebarti 
 * Copyright © 2012-2013. All rights reserved.
 */
package com.idot.dataingest.namedentities.dictionaries;

/**
 *
 * 
 */
public class CompanyDictionary extends Dictionary {
    
    public CompanyDictionary(String propertyName) {
        super(propertyName);
        type = DictionaryType.COMPANY;
        
        while (scanner.hasNext()) {
            values.add(scanner.next());
        }
    }
}
