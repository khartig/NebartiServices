/*
 * Nebarti
 * Copyright © 2012-2013. All rights reserved.
 */
package com.idot.dataingest.namedentities.dictionaries;

/**
 *
 *
 */
class FamousPeopleDictionary extends Dictionary {

    public FamousPeopleDictionary(String propertyName) {
        super(propertyName);
        type = DictionaryType.FAMOUS_PEOPLE;
        
        while (scanner.hasNext()) {
            values.add(scanner.next());
        }
    }
    
}
