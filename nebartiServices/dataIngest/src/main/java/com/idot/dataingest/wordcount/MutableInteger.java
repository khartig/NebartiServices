/*
 * i-Dot Analytics, Inn.
 * Copyright 2011.
 */
package com.idot.dataingest.wordcount;

/** 
 * Utility class to keep int as Object but allow changes (unlike Integer).
 * Java collections hold only Objects, not primitives, but need to update value.
 * The intention is that the public field should be used directly.
 * For a simple value class this is appropriate.
 */
class MutableInteger {
    private int value;

    /** 
     * Constructor 
     * 
     * @param value Initial value. 
     */
    public MutableInteger(int value) { 
        this.value = value;  
    }
    
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    } 
    
    public void increment() {
        value = value + 1;
    }
    
    public void decrement() {
        value = value - 1;
    }

    Integer getInteger() {
        return new Integer(value);
    }
}
