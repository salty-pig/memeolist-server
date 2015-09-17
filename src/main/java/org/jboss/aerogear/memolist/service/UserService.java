/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.aerogear.memolist.service;

import java.util.Optional;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
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
    public RedHatUser createUser(String userName, String displayName, String photoUrl, byte[] image) {
        RedHatUser user = new RedHatUser();
        user.setUsername(userName);
        user.setDisplayName(displayName);
        user.setPhotoUrl(photoUrl);
        user.setImage(image);
        em.persist(user);
        em.flush();
        return lookup(userName).get();
    }
}
