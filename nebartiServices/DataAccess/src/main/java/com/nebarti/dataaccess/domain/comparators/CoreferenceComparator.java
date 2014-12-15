/*
 * Nebarti
 * Copyright © 2012-2013 All rights reserved.
 */
package com.nebarti.dataaccess.domain.comparators;

import com.nebarti.dataaccess.domain.CoReference;
import java.util.Comparator;

/**
 *
 */
public class CoreferenceComparator implements Comparator {

    public int compare(Object obj1, Object obj2) {
        int result;
        if ( Double.parseDouble( ((CoReference)obj1).getScore())  < Double.parseDouble( ((CoReference)obj2).getScore() ) ) {
            result = 1;
        } else if ( Double.parseDouble( ((CoReference)obj1).getScore()) ==  Double.parseDouble( ((CoReference)obj2).getScore()) )  { 
            result = 0;

        } else {
            result = -1;
        }
        return result;
    }

}
