/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idot.sentiment.controllers;

import com.idot.sentiment.controllers.exceptions.NonexistentEntityException;
import com.idot.sentiment.entities.Tweet;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;

/**
 *
 * @author khartig
 */
public class TweetJpaController implements Serializable {

    public TweetJpaController(EntityTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private EntityTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tweet tweet) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(tweet);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tweet tweet) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            tweet = em.merge(tweet);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = tweet.getId();
                if (findTweet(id) == null) {
                    throw new NonexistentEntityException("The tweet with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tweet tweet;
            try {
                tweet = em.getReference(Tweet.class, id);
                tweet.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tweet with id " + id + " no longer exists.", enfe);
            }
            em.remove(tweet);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tweet> findTweetEntities() {
        return findTweetEntities(true, -1, -1);
    }

    public List<Tweet> findTweetEntities(int maxResults, int firstResult) {
        return findTweetEntities(false, maxResults, firstResult);
    }

    private List<Tweet> findTweetEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Tweet as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Tweet findTweet(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tweet.class, id);
        } finally {
            em.close();
        }
    }

    public int getTweetCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Tweet as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
