/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.aerogear.memolist.service.controller;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.jboss.aerogear.memolist.security.RedHatUserBean;
import org.jboss.aerogear.memolist.service.UserService;
import org.jboss.aerogear.memolist.vo.FavoritedPost;
import org.jboss.aerogear.memolist.vo.RedHatUser;

/**
 *
 * @author secondsun
 */
@Stateless
@Path("/api")
public class UserController {
    
    @Inject
    private RedHatUserBean rhuBean;

    @Inject 
    private UserService userService;
    
    @GET
    @Path("user")
    @Produces(MediaType.APPLICATION_JSON)
    public RedHatUser getUser() {
        return  rhuBean.getRedHatUser();
    }

    @GET
    @Path("favorite")
    @Produces(MediaType.APPLICATION_JSON)
    public List<FavoritedPost> getAllFavorites() {
        return userService.getFavoritePosts(rhuBean.getRedHatUser().getUsername());
    }

    
    @PUT
    @POST
    @Path("favorite")
    @Produces(MediaType.APPLICATION_JSON)
    public FavoritedPost favoritePost(FavoritedPost post) {
        return userService.addFavorite(post, rhuBean.getRedHatUser());
    }
    
    @DELETE
    @Path("favorite/{favoriteId}")
    @Produces(MediaType.APPLICATION_JSON)
    public FavoritedPost favoritePost(@PathParam("favoriteId") Long favoritePostId) {
        return userService.removeFavorite(favoritePostId, rhuBean.getRedHatUser());
    }
           
    
}
