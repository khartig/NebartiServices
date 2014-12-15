/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.idot.dataingest.processors;

import com.idot.dataingest.twitter.CredentialHandler;
import com.idot.dataingest.twitter.listeners.ClientConnectionLifeCycleListener;
import com.idot.dataingest.twitter.listeners.ClientStatusListener;
import com.idot.dataingest.utilities.Properties;
import com.nebarti.dataaccess.domain.Classifier;
import com.nebarti.dataaccess.domain.TwitterAccount;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.PropertyConfiguration;

/**
 *
 * 
 */
public class TwitterProcessor extends Processor {
    PropertyConfiguration propertyConfguration;
    TwitterStream twitterStream;
    ClientStatusListener clientStatusListener = new ClientStatusListener();
    boolean keepRunning = false;
    Set<Classifier> currentFilteringClassifiers = new HashSet<Classifier>();
    private TweetProcessor tweetProcessor = new TweetProcessor();
    private static final Properties properties = new Properties();
    private static final String TWITTER_USER_ID = properties.getProperty("twitter.account.name");
    public static final Logger logger = Logger.getLogger(TwitterProcessor.class.getName());

    public TwitterProcessor(String id, String name) {
        this.id = id;
        this.name = name;
//        twitterStream = new TwitterStreamFactory(new ConfigurationBuilder().setJSONStoreEnabled(true).build()).getInstance();
        twitterStream = TwitterStreamFactory.getSingleton();
        
        TwitterAccount account = CredentialHandler.get(TWITTER_USER_ID);
        
        twitterStream.setOAuthConsumer(account.getConsumerKey(), account.getConsumerSecret());
        twitterStream.setOAuthAccessToken(new AccessToken(account.getAccessToken(), account.getAccessTokenSecret()));

        twitterStream.addListener(clientStatusListener);
        
        ClientConnectionLifeCycleListener clientConnectionLifeCycleListener = new ClientConnectionLifeCycleListener();
        clientConnectionLifeCycleListener.setId(id);
        twitterStream.addConnectionLifeCycleListener(clientConnectionLifeCycleListener);
        
        // start tweet status processing thread
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.submit(tweetProcessor);
        
    }
    
    public FilterQuery addFilter(Classifier classifier) {
        currentFilteringClassifiers.add(classifier);
//        String[] searchTerms = {classifier.getFilter()};
        
        tweetProcessor.addFilterQuery(classifier);
        FilterQuery filterQuery = updateFilterQuery();
        twitterStream.filter(filterQuery);
        
        return filterQuery;
    }
    
    public void removeFilter(Classifier classifier) {
        tweetProcessor.removeFilterQuery(classifier);      
        currentFilteringClassifiers.remove(classifier);
        FilterQuery filterQuery = updateFilterQuery();
        if (!currentFilteringClassifiers.isEmpty()) {
            twitterStream.filter(filterQuery);
        }
    }
    
    public Boolean isFiltering(Classifier classifier) {
        return currentFilteringClassifiers.contains(classifier);
    }
    
    private FilterQuery updateFilterQuery() {
        List<String> words = new ArrayList<String>();
        Iterator itr = currentFilteringClassifiers.iterator();

        Classifier classifier;
        while (itr.hasNext()) {
            String[] tokens;
            classifier = (Classifier) itr.next();
            if (classifier.getFilter().contains("\"")) {
                tokens = new String[] { classifier.getFilter().replace("\"", "") };
            } else {
                tokens = classifier.getFilter().split(" ");
            }
            List<String> tokenList = Arrays.asList(tokens);
            words.addAll(tokenList);
        }
        
        FilterQuery filterQuery = new FilterQuery();
        filterQuery.track( words.toArray(new String[words.size()]) );
        
        return filterQuery;
    }

    @Override
    public void run() {
        keepRunning = true;

        while (keepRunning) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                logger.log(Level.SEVERE, null, ex);
                twitterStream.cleanUp();
                twitterStream.shutdown();
            }
        }

        twitterStream.cleanUp();
        twitterStream.shutdown();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        keepRunning = false;
        return mayInterruptIfRunning;
    }

    @Override
    public String toString() {
        return "TwitterProcessor{" + getName() + "}";
    }

}
