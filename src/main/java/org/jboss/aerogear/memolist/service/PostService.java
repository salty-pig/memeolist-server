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
import org.jboss.aerogear.memolist.vo.Post;

/**
 *
 * @author summers
 */
@Stateless
public class PostService {

    @PersistenceContext
    private EntityManager em;

    @TransactionAttribute()
    public Post save(Post post) {
        em.persist(post);
        em.flush();
        return post;
    }

    public List<Post> getMostRecent() {
        return em.createQuery("from Post order by posted").setMaxResults(10).getResultList();
    }
            
    
}
