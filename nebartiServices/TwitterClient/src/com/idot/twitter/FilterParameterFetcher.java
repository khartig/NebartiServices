/*
 * 
 */
package com.idot.twitter;

import java.util.Collection;

/**
 * Fetches filter parameters to pass to twitter when creating the stream.
 *
 * @author 
 */
public interface FilterParameterFetcher {
    /**
     * @return a collection of twitter ids to follow, or null for no
     *   id filtering.
     */
    Collection<String> getFollowIds();

    /**
     * @return a collection of keywords to track, or null for no
     *   keyword filtering.
     */
    Collection<String> getTrackKeywords();
}
