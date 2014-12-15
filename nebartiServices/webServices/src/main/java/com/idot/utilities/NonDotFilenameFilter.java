/*
 * Nebarti
 * Copyright 2013 All rights reserved.
 */
package com.idot.utilities;

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 */
public class NonDotFilenameFilter implements FilenameFilter {

    @Override
    public boolean accept(File file, String string) {
        boolean accept = false;
        if (!string.contains(".DS_Store")) {
            accept = true;
        }
        
        return accept;
    }
    
}
