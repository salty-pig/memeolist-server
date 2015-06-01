/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.aerogear.memolist.service.controller;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.jboss.aerogear.memolist.service.FeedService;
import org.jboss.aerogear.memolist.vo.Post;

/**
 *
 * @author summers
 */
@Stateless
@Path("/api")
public class FeedController {
    
    @Inject
    private FeedService feedService;
    
    @GET
    @Path("/feed")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Post> getFeed() {
        return feedService.getPosts();
    }

    @GET
    @Path("/feed/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Post> getFeed(@PathParam("page") int page) {
        return feedService.getPosts(page);
    }

    
}
