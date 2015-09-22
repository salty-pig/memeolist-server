/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.aerogear.memolist.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.jboss.aerogear.memolist.vo.FavoritedPost;
import org.jboss.aerogear.memolist.vo.Post;
import org.jboss.aerogear.memolist.vo.RedHatUser;

/**
 *
 * @author summers
 */
@Stateless
public class UserService {

    @PersistenceContext
    private EntityManager em;

    public Optional<RedHatUser> lookup(String userName) {
        try {
            return Optional.of(em.createQuery("from RedHatUser u where u.username = :username", RedHatUser.class)
                    .setParameter("username", userName)
                    .getSingleResult());
        } catch (NoResultException ignore) {
            return Optional.empty();
        }

    }

    @TransactionAttribute()
    public RedHatUser createUser(String userName, String displayName, String photoUrl, String emailAddress, byte[] image) {
        RedHatUser user = new RedHatUser();
        user.setUsername(userName);
        user.setEmailAddress(emailAddress);
        user.setBio("There is no bio yet.");
        user.setDisplayName(displayName);
        user.setPhotoUrl(photoUrl);
        user.setImage(image);
        em.persist(user);
        em.flush();
        return lookup(userName).get();
    }
    
    public List<FavoritedPost> getFavoritePosts(String userName) {
        Query query = em.createQuery("from FavoritedPost f where f.owner.username = :username order by f.favoritedDate DESC");
        return query.setParameter("username", userName).getResultList();
    }

    public FavoritedPost addFavorite(FavoritedPost post, RedHatUser redHatUser) {
        post.setOwner(redHatUser);
        post.setFavoritedDate(new Date());
        em.persist(post);
        return post;
    }

    public FavoritedPost removeFavorite(Long favoritePostId, RedHatUser redHatUser) {
        FavoritedPost favoritePost = (FavoritedPost) em.createQuery("select f.post from FavoritedPost f where f.owner.username = :username order and f.id = :id")
                .setParameter("username", redHatUser.getUsername())
                .setParameter("id", favoritePostId)
                .getSingleResult();
        
        em.remove(favoritePost);
        return favoritePost;
    }
            
    
}
