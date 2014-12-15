/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.idot.dataingest.twitter;

import com.nebarti.dataaccess.dao.TwitterAccountDao;
import com.nebarti.dataaccess.domain.TwitterAccount;
import java.util.logging.Logger;

/**
 * 
 */
public class CredentialHandler {
    public static final Logger logger = Logger.getLogger(CredentialHandler.class.getName());
    
    public static TwitterAccount get(String userID) {
        TwitterAccountDao dao = new TwitterAccountDao();
        return dao.getByUserID(userID);
    }

}
