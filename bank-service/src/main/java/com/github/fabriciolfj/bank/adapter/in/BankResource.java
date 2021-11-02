package com.github.fabriciolfj.bank.adapter.in;

import com.github.fabriciolfj.bank.infra.BankSupportConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Optional;

@Path("banks")
public class BankResource {

    @Value("${bank.name}")
    String name;

    @Value("${db.username}")
    String username;

    @Value("${db.password}")
    String password;

    @Value("${app.mobileBanking}")
    Optional<Boolean> mobileBanking;

    @Autowired
    BankSupportConfig supportConfig;

    @GET
    @Path("name")
    public String getName() {
        return name;
    }

    @GET
    @Path("secrets")
    public HashMap<String, String> getSecrets() {
        final var map = new HashMap<String, String>();
        map.put("username", username);
        map.put("password", password);

        return map;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/mobilebanking")
    public Boolean getMobileBanking() {
        return mobileBanking.orElse(false);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/support")
    public HashMap<String, String> getSupport() {
        HashMap<String,String> map = new HashMap<>();

        map.put("email", supportConfig.getEmail());
        map.put("phone", supportConfig.getPhone());

        return map;
    }
}
