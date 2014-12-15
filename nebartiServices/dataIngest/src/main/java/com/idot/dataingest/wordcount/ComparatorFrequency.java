/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.idot.dataingest.wordcount;

import java.util.Comparator;
import java.util.Map;


/** 
 * Order words from least to most frequent, put ties in alphabetical order. 
 */
class ComparatorFrequency implements Comparator<Map.Entry<String, MutableInteger>> {

    @Override
    public int compare(Map.Entry<String, MutableInteger> obj1, Map.Entry<String, MutableInteger> obj2) {
        int result;
        int count1 = obj1.getValue().getValue();
        int count2 = obj2.getValue().getValue();
        if (count1 < count2) {
            result = -1;

        } else if (count1 > count2) {
            result = 1;

        } else {
            //... If counts are equal, compare keys alphabetically.
            result = obj1.getKey().compareTo(obj2.getKey());
        }
        return result;
    }
}
