/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.aerogear.memolist.security;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.jboss.aerogear.memolist.service.UserService;
import org.jboss.aerogear.memolist.vo.RedHatUser;
import org.keycloak.KeycloakPrincipal;

/**
 *
 * @author summers
 */
@RequestScoped
public class RedHatUserBean {
    
    @Inject
    private HttpServletRequest request;
    
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
            org.keycloak.KeycloakSecurityContext context = (org.keycloak.KeycloakSecurityContext) request.getAttribute("org.keycloak.KeycloakSecurityContext");
            String displayName =  context.getToken().getGivenName() + " " + context.getToken().getFamilyName();
            String photoUrl = context.getToken().getPicture();
            if (photoUrl == null || photoUrl == "") {
                photoUrl = "http://static5.businessinsider.com/image/511d104a69bedd1f7c000012/grumpy-cat-definitely-did-not-make-100-million.jpg";
            }
            byte[] image = load(photoUrl);
            this.redHatUser = userService.createUser(userName, displayName, photoUrl, image);
        }
    }
    
    public RedHatUser getRedHatUser() {
        return redHatUser;
    }

    private byte[] load(String photoUrl) {
        try {
            URL url = new URL(photoUrl);
            return IOUtils.toByteArray(url);
        } catch (Throwable t) {
            Logger.getLogger("Error").log(Level.SEVERE, t.getMessage(), t);
            try {
                return IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("grumpycat.jpg"));
            } catch (IOException ex) {
                Logger.getLogger("Error").log(Level.SEVERE, t.getMessage(), t);
                return null;
            }
        }
    }
    
}
