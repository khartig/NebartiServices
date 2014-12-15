/*
 * Nebarti
 * Copyright © 2012-2013 All rights reserved.
 */
package com.nebarti.dataaccess.domain.comparators;

import com.nebarti.dataaccess.domain.NamedEntity;
import java.util.Comparator;
import java.util.Map;

/**
 *
 */
public class NamedEntityComparator implements Comparator {
    Map namedEntities;
    
    public NamedEntityComparator(Map namedEntities) {
        this.namedEntities = namedEntities;
    }
    
    public int compare(Object key1, Object key2) {
        int result;
        NamedEntity entity1 = (NamedEntity)namedEntities.get(key1);
        NamedEntity entity2 = (NamedEntity)namedEntities.get(key2);
        if (entity1.getFrequency() < entity2.getFrequency()) {
            result = 1;

        } else if (entity1.getFrequency() == entity2.getFrequency()) {
            result = 0;

        } else {
            result = -1;
        }
        return result;
    }    
}
