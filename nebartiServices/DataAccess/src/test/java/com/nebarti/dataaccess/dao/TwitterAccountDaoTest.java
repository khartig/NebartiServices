/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.nebarti.dataaccess.dao;

import com.nebarti.dataaccess.domain.TwitterAccount;
import junit.framework.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 */
public class TwitterAccountDaoTest {
    TwitterAccountDao dao;
    
    @BeforeClass
    public void setUp() {
        dao = new TwitterAccountDao();
    }
    
    @Test
    public void saveTest() {
//        TwitterAccount account = new TwitterAccount();
//        account.setUserID("IdotAnalytics");
//        account.setAccessToken("817984039-A8jLkhGhVglJpG6vV1loAJTfeHn6i8XM9ackZB0J");
//        account.setAccessTokenSecret("0awx4kwsj7eVQcgVvb3IODz5DeGVIgk5x6EAEzqpa4");
//        account.setConsumerKey("FKxlUP1jQwGLGKr0V3gGcQ");
//        account.setConsumerSecret("bpEtK4MQxXmpFhGrtC9rXCGht4WD3NpIXslL6zmI8Q");
//        
//        Assert.assertTrue( dao.save(account) );
    }
    
    @Test
    public void getByUserIDTest() {
//        TwitterAccount account = dao.getByUserID("Nebarti");
//        Assert.assertEquals("Nebarti", "Nebarti");
    }
    
    @AfterClass
    public void cleanUp() {
        // code that will be invoked after this test ends
    }
}
