package com.github.fabriciolfj.bank.adapter.in;

import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashSet;
import java.util.Set;

public class TokenResource {

    @Inject
    JsonWebToken accessToken;

    @GET
    @Path("/tokeninfo")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<String> token() {
        final var set = new HashSet<String>();
        for (String t : accessToken.getClaimNames()) {
            set.add(t + "=" + accessToken.getClaim(t));
        }

        return set;
    }
}
