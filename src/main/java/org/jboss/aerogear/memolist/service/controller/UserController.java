/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.aerogear.memolist.service.controller;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.jboss.aerogear.memolist.security.RedHatUserBean;
import org.jboss.aerogear.memolist.vo.Meme;
import org.jboss.aerogear.memolist.vo.Post;
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

    @GET
    @Path("user")
    @Produces(MediaType.APPLICATION_JSON)
    public RedHatUser mostRecent() {
        return  rhuBean.getRedHatUser();
    }
    
}
