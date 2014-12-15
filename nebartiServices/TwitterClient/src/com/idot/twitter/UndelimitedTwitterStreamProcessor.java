/*
 *
 */
package com.idot.twitter;

/**
 * @author
 */
public abstract class UndelimitedTwitterStreamProcessor
        implements TwitterStreamProcessor {

    @Override
    public boolean consumesDelimitedStream() {
        return false;
    }
}