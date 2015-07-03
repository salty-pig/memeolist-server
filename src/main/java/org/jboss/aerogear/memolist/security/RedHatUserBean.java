/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.aerogear.memolist.security;

import java.security.Principal;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.jboss.aerogear.memolist.service.UserService;
import org.jboss.aerogear.memolist.vo.RedHatUser;

/**
 *
 * @author summers
 */
@RequestScoped
public class RedHatUserBean {
    
    @Inject
    private Principal principal;
    
    @Inject
    private UserService userService;
    
    private RedHatUser redHatUser;
    
    @PostConstruct
    public void init() {
        String userName = principal.getName();
        Optional<RedHatUser> user = userService.lookup(userName);
        if (user.isPresent()) {
            this.redHatUser = user.get();
        } else {
            this.redHatUser = userService.createUser(userName);
        }
    }

    public RedHatUser getRedHatUser() {
        return redHatUser;
    }
    
    
}
