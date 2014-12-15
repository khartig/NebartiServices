/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.nebarti.dataaccess.domain.comparators;

import com.nebarti.dataaccess.domain.Reference;
import java.util.Comparator;
import java.util.Map;

/**
 *
 */
public class ReferenceComparator implements Comparator {
    Map references;
    
    public ReferenceComparator(Map references) {
        this.references = references;
    }
    
    public int compare(Object key1, Object key2) {
        int result;
        Reference ref1 = (Reference)references.get(key1);
        Reference ref2 = (Reference)references.get(key2);
        if (ref1.getCount() < ref2.getCount()) {
            result = 1;

        } else if (ref1.getCount() == ref2.getCount()) {
            result = 0;

        } else {
            result = -1;
        }
        return result;
    }
}
