/*
 * 
 */
package com.idot.twitter;

import java.io.InputStream;
import java.io.IOException;
import java.util.Set;

/**
 * @author 
 */
public interface TwitterStreamProcessor {

    /**
     * Processes the twitter stream until it's interrupted or gets an
     * IOException.  This method should expect to be interrupted, and
     * throw an InterruptedException or InterruptedIOException.
     *
     * @param is the stream to process
     * @param credentials the credentials used to create the stream,
     *   for logging purposes
     * @param ids the twitter ids this stream is following
     */
    void processTwitterStream(InputStream is, String credentials,
            Set<String> ids)
            throws InterruptedException, IOException;

    /**
     * Returns true if this processor consumes a delimited stream.
     */
    boolean consumesDelimitedStream();
}