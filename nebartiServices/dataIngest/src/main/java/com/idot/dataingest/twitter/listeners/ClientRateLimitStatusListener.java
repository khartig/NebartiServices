/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.idot.dataingest.twitter.listeners;

import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.RateLimitStatus;
import twitter4j.RateLimitStatusEvent;
import twitter4j.RateLimitStatusListener;

/**
 *
 * 
 */
public class ClientRateLimitStatusListener implements RateLimitStatusListener {

    public static final Logger logger = Logger.getLogger(ClientRateLimitStatusListener.class.getName());

    @Override
    public void onRateLimitStatus(RateLimitStatusEvent rlse) {
        backoffRequests(rlse);
    }

    @Override
    public void onRateLimitReached(RateLimitStatusEvent rlse) {
        backoffRequests(rlse);
    }

    private void backoffRequests(RateLimitStatusEvent rlse) {
        if (rlse.isAccountRateLimitStatus() || rlse.isIPRateLimitStatus()) {
            logger.log(Level.INFO, "Account limilt = {0} IP limit = {1}", new Object[]{rlse.isAccountRateLimitStatus(), rlse.isIPRateLimitStatus()});
            try {
                RateLimitStatus rls = rlse.getRateLimitStatus();
                Thread.sleep((rls.getSecondsUntilReset() + 10) * 1000);
            } catch (InterruptedException ex) {
                logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            }
        }
    }
}
