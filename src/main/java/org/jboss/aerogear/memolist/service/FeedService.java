/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.aerogear.memolist.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.aerogear.memolist.security.RedHatUserBean;
import org.jboss.aerogear.memolist.vo.Post;

/**
 *
 * @author summers
 */
@Stateless
@Named("feedService")
public class FeedService {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public List<Post> getPosts() {
        return entityManager.createQuery("from Post order by posted desc", Post.class)
                .setMaxResults(10)
                .getResultList();
    }
    
    public List<Post> getPosts(int page) {
        return entityManager.createQuery("from Post order by posted desc", Post.class)
                .setFirstResult(page * 10)
                .setMaxResults(10)
                .getResultList();
    }
}
