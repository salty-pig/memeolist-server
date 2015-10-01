package org.jboss.aerogear.memolist.vo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlTransient;

@Entity
public class PostComment implements Serializable, Comparable<PostComment> {

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
    @XmlTransient
    private RedHatUser author;
    
    private Long  postId;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date postedDate;

    @Lob
    private String comment;

    public RedHatUser getAuthor() {
        return author;
    }

    public void setAuthor(RedHatUser author) {
        this.author = author;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public PostComment detach() {
        PostComment newComment = new PostComment();
        newComment.setComment(comment);
        newComment.setPostedDate(postedDate);
        return newComment;
    }

    @Override
    public int compareTo(PostComment t) {
        if (postedDate == null && t == null) {
            return 0;
        } else if (postedDate == null && t != null) {
            return -1;
        } else if (t == null) {
            return 1;
        }
        return postedDate.compareTo(t.postedDate);
    }

}
