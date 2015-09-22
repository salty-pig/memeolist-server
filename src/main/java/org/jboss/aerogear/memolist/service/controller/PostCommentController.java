/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.aerogear.memolist.service.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.jboss.aerogear.memolist.security.RedHatUserBean;
import org.jboss.aerogear.memolist.service.PostService;
import org.jboss.aerogear.memolist.vo.PostComment;

/**
 *
 * @author secondsun
 */
@Stateless
@Path("/api")
public class PostCommentController {
    
    @Inject
    private RedHatUserBean rhuBean;

    @Inject 
    private PostService postService;
    

    @GET
    @Path("comments")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PostComment> getAllComments(@QueryParam("postId") Long postId) {
        return postService.getPostComments(postId).stream()
                .map(comment -> {return comment.detach();})
                .collect(Collectors.toList());
    }

    
    @PUT
    @POST
    @Path("comments")
    @Produces(MediaType.APPLICATION_JSON)
    public PostComment favoritePost(@QueryParam("postId") Long postId, PostComment comment) {
        return postService.addComment(postId, comment, rhuBean.getRedHatUser());
    }
    
}
