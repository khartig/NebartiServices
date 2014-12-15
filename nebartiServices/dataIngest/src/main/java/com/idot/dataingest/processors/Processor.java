/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.idot.dataingest.processors;

/**
 *
 */
public abstract class Processor implements Runnable {

    String name = "unnamedProcessor";
    String id = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        Thread.currentThread().setName(id);
    }

    public abstract boolean cancel(boolean canInterrupt);
}
