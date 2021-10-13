package com.github.fabriciolfj.bank.adapter.in;

import com.github.fabriciolfj.bank.infra.BankSupportConfig;
import com.github.fabriciolfj.bank.infra.BankSupportConfigMapping;
import io.smallrye.config.ConfigMapping;
import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Path("banks")
public class BankResource {

    @ConfigProperty(name = "bank.name", defaultValue = "Bank of default")
    String name;

    @ConfigProperty(name = "db.username", defaultValue = "")
    String username;

    @ConfigProperty(name = "db.password", defaultValue = "")
    String password;

    @ConfigProperty(name="app.mobileBanking")
    Optional<Boolean> mobileBanking;

    @ConfigProperties(prefix = "bank-support")
    BankSupportConfig supportConfig;

    @Inject
    BankSupportConfigMapping configMapping;

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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/supportmapping")
    public Map<String, String> getSupportMapping() {
        HashMap<String,String> map = getSupport();

        map.put("business.email", configMapping.business().email());
        map.put("business.phone", configMapping.business().phone());

        return map;
    }
}
