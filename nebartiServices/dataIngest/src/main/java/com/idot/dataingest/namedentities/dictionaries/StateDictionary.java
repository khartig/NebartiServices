/*
 * Nebarti 
 * Copyright © 2012-2013. All rights reserved.
 */
package com.idot.dataingest.namedentities.dictionaries;

/**
 *
 * 
 */
class StateDictionary extends Dictionary {

    public StateDictionary(String propertyName) {
        super(propertyName);
        type = DictionaryType.STATE;
        
        while (scanner.hasNext()) {
            values.add(scanner.next());
        }
    }
    
}
