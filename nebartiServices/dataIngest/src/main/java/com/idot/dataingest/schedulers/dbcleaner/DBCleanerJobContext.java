/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.idot.dataingest.schedulers.dbcleaner;

import java.io.Serializable;

/**
 *
 */
public class DBCleanerJobContext implements Serializable {
    private static final long serialVersionUID = -5882948920193123292L;
    
    private String state = "Initial state.";

    public String getState() {
        synchronized (state) {
            return state;
        }
    }

    public void setState(String state) {
        synchronized (state) {
            this.state = state;
        }
    }
}
