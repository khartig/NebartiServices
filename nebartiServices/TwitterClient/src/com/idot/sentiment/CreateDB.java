/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idot.sentiment;

import com.idot.sentiment.controllers.SearchStringJpaController;
import com.idot.sentiment.controllers.TweetJpaController;
import com.idot.sentiment.entities.SearchString;
import com.idot.sentiment.entities.Tweet;
import java.util.Calendar;
import java.util.HashSet;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author khartig
 */
public class CreateDB {
    private TweetJpaController tweetController;
    private SearchStringJpaController searchStringController;
    private static final Logger logger = Logger.getLogger(CreateDB.class.getName());
    
    public CreateDB() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CreateDBPU");
        EntityManager em = emf.createEntityManager();
        tweetController = new TweetJpaController(em.getTransaction(), emf);
        searchStringController = new SearchStringJpaController(em.getTransaction(), emf);
    }
    
    public void addTweets() {
        HashSet<Tweet> tweets = new HashSet<Tweet>();
        
        Tweet tweet = new Tweet();
        tweet.setTweet("@anyone testing");
        tweet.setTweetedAt(Calendar.getInstance().getTime());
        tweet.setGeolocation("Right there");
        tweet.setSentiment("TRUE");
        tweets.add(tweet);
        
        Tweet tweet2 = new Tweet();
        tweet2.setTweet("@everyone testing");
        tweet2.setTweetedAt(Calendar.getInstance().getTime());
        tweet2.setGeolocation("Over there");
        tweet2.setSentiment("FALSE");
        tweets.add(tweet2);
        
        
        SearchString searchString = new SearchString();
        searchString.setSearchString("there");
        searchString.setTweets(tweets);
        
        searchStringController.create(searchString);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CreateDB creator = new CreateDB();
        creator.addTweets();
    }
}
