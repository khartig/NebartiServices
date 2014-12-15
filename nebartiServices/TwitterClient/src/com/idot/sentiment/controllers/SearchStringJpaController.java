/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idot.sentiment.controllers;

import com.idot.sentiment.controllers.exceptions.NonexistentEntityException;
import com.idot.sentiment.entities.SearchString;
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
public class SearchStringJpaController implements Serializable {

    public SearchStringJpaController(EntityTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private EntityTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SearchString searchString) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(searchString);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SearchString searchString) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            searchString = em.merge(searchString);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = searchString.getId();
                if (findSearchString(id) == null) {
                    throw new NonexistentEntityException("The searchString with id " + id + " no longer exists.");
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
            SearchString searchString;
            try {
                searchString = em.getReference(SearchString.class, id);
                searchString.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The searchString with id " + id + " no longer exists.", enfe);
            }
            em.remove(searchString);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SearchString> findSearchStringEntities() {
        return findSearchStringEntities(true, -1, -1);
    }

    public List<SearchString> findSearchStringEntities(int maxResults, int firstResult) {
        return findSearchStringEntities(false, maxResults, firstResult);
    }

    private List<SearchString> findSearchStringEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from SearchString as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public SearchString findSearchString(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SearchString.class, id);
        } finally {
            em.close();
        }
    }

    public int getSearchStringCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from SearchString as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
