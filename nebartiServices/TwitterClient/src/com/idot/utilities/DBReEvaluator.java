/*
 * 
 */
package com.idot.utilities;

import com.idot.classifiers.PolarityHierarchical;
import com.idot.sentiment.controllers.TweetJpaController;
import com.idot.sentiment.controllers.exceptions.NonexistentEntityException;
import com.idot.sentiment.entities.Tweet;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author khartig
 */
public class DBReEvaluator {
    
    EntityManagerFactory emf;
    EntityManager em;
    TweetJpaController tweetController;
    public static final Logger logger = Logger.getLogger(DBReEvaluator.class.getName());

    public DBReEvaluator() {
        emf = Persistence.createEntityManagerFactory("CreateDBPU");
        em = emf.createEntityManager();
        tweetController = new TweetJpaController(em.getTransaction(), emf);
    }

    public void evaluate() {
        List<Tweet> tweets = tweetController.findTweetEntities();

        try {
            PolarityHierarchical polarityHierarchical = new PolarityHierarchical();
            for (Tweet tweet : tweets) {
                tweet.setSentiment(polarityHierarchical.evaluate(tweet.getTweet()).toString());
                tweetController.edit(tweet);
            }
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } catch (NonexistentEntityException e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DBReEvaluator evaluator = new DBReEvaluator();
        evaluator.evaluate();
    }
}
