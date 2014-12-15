/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.idot.dataingest.schedulers.summarizer;

import java.io.Serializable;

/**
 * 
 */
public class SummarizerJobContext implements Serializable {
    private static final long serialVersionUID = 4170353928501693420L;
    
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
