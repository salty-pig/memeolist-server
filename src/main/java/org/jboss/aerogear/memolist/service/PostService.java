/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.aerogear.memolist.service;

import java.util.Date;
import java.util.List;
import java.util.jar.Pack200;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.aerogear.memolist.vo.Post;
import org.jboss.aerogear.memolist.vo.PostComment;
import org.jboss.aerogear.memolist.vo.RedHatUser;

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

    public List<PostComment> getPostComments(Long postId) {
        return em.createQuery("from PostComment where post.id = :postId order by postedDate").setParameter("postId", postId).getResultList();
    }

    public PostComment addComment(PostComment comment, RedHatUser redHatUser) {
        PostComment newComment = new PostComment();
        Post post = findPost(comment.getPostId());
        
        if (post == null) {
            throw new IllegalArgumentException("Post does not exist");
        }
        
        newComment.setAuthor(redHatUser);
        newComment.setComment(comment.getComment());
        newComment.setPostId(comment.getPostId());
        newComment.setPostedDate(new Date());
        
        em.persist(newComment);
        return newComment;
    }

    private Post findPost(Long postId) {
        return (Post) em.createQuery("from Post where id = :postId").setParameter("postId", postId).getSingleResult();
    }
            
    
}
