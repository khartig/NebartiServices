/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.idot.dataingest.exceptions;

/**
 *
 * 
 */
public class ModelCreationException extends Exception {

    /**
     * Creates a new instance of <code>ModelCreationException</code> without detail message.
     */
    public ModelCreationException() {
    }

    /**
     * Constructs an instance of <code>ModelCreationException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ModelCreationException(String msg) {
        super(msg);
    }
}
