/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.aerogear.memolist.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.aerogear.memolist.vo.Meme;

/**
 *
 * @author summers
 */
@Stateless
public class MemeService {

    @PersistenceContext
    private EntityManager em;

    @TransactionAttribute()
    public Meme save(Meme meme) {
        em.persist(meme);
        em.flush();
        return meme;
    }

    public List<Meme> getMostRecent() {
        return em.createQuery("from Meme order by posted").setMaxResults(10).getResultList();
    }

    public byte[] getImage(long memeId) {
        return em.createQuery("from Meme where id = :id", Meme.class).setParameter("id", memeId).getSingleResult().getImage();
    }
            
    
}
