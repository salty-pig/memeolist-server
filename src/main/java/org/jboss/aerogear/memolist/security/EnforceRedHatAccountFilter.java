/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.aerogear.memolist.security;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author summers
 */
@PreMatching()
public class EnforceRedHatAccountFilter implements ContainerRequestFilter {

    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        final SecurityContext securityContext =
                    requestContext.getSecurityContext();
        if (!securityContext.getUserPrincipal().getName().endsWith("redhat.com")) {
            requestContext.abortWith(Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"RedHat Account is required\"}")
                    .build());
        }
    }
    
}
