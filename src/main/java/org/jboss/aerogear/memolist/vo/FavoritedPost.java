/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.aerogear.memolist.vo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author secondsun
 */
@Entity
public class FavoritedPost implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    private RedHatUser owner;
    @ManyToOne
    private Post post;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date favoritedDate;

    public RedHatUser getOwner() {
        return owner;
    }

    public void setOwner(RedHatUser owner) {
        this.owner = owner;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Date getFavoritedDate() {
        return favoritedDate;
    }

    public void setFavoritedDate(Date favoritedDate) {
        this.favoritedDate = favoritedDate;
    }

    
    
}
