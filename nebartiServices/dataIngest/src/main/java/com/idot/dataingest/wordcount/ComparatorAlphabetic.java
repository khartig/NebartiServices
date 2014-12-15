/*
 * i-Dot Analytics, Inn.
 * Copyright 2011.
 */
package com.idot.dataingest.wordcount;

import java.util.Comparator;
import java.util.Map;

/** 
 * Order words alphabetically. 
 */
class ComparatorAlphabetic implements Comparator<Map.Entry<String, MutableInteger>> {

    @Override
    public int compare(Map.Entry<String, MutableInteger> entry1, Map.Entry<String, MutableInteger> entry2) {
        return entry1.getKey().compareTo(entry2.getKey());
    }
}
