/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.idot.dataingest.twitter.listeners;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

/**
 *
 */
public class ClientStatusListener implements StatusListener {
    private static final com.idot.dataingest.utilities.Properties properties = new com.idot.dataingest.utilities.Properties();
    private static final String tweetQueueSizeStr;
    private static final Integer tweetQueueSize;
    static {
        tweetQueueSizeStr = properties.getProperty("twitter.tweet.queue.size");
        if (tweetQueueSizeStr != null) {
            tweetQueueSize = Integer.parseInt(tweetQueueSizeStr);
        } else {
            tweetQueueSize = 2000;
        }
        
    }
    public static LinkedBlockingQueue<Status> tweetQueue = new LinkedBlockingQueue<Status>(tweetQueueSize);
    public static final Logger logger = Logger.getLogger(ClientStatusListener.class.getName());

    @Override
    public void onStatus(Status status) {
        //        logger.log(Level.INFO, "{0} : {1}", new Object[]{status.getUser().getScreenName(), status.getText()});
        // tweets are pulled from tweetQueue in 
        // com.idot.dataingest.processors.TweetProcessor from this queue object 
        boolean offer = tweetQueue.offer(status);       
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice sdn) {
        logger.log(Level.INFO, "Got a status deletion notice id:{0}", sdn.getStatusId());
    }

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        logger.log(Level.INFO, "Got track limitation notice:{0}", numberOfLimitedStatuses);
    }

    @Override
    public void onScrubGeo(long userId, long upToStatusId) {
        logger.log(Level.INFO, "Got scrub_geo event userId:{0} upToStatusId:{1}", new Object[]{userId, upToStatusId});
    }

    @Override
    public void onException(Exception exception) {
        logger.log(Level.WARNING, exception.getLocalizedMessage(), exception);
    }

    @Override
    public void onStallWarning(StallWarning warning) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
