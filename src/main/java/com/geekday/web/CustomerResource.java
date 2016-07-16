package com.geekday.web;


import com.geekday.customer.CustomerService;
import com.geekday.customer.Profile;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/")
public class CustomerResource {
    @POST
    @Path("customer")
    public Response createCustomer(@FormParam("name") String name, @FormParam("address") String address) {
        new CustomerService().newCustomer(name, address);
        return Response.created(URI.create("/profile")).build();
    }

    @GET
    @Path("profile")
    @Produces(MediaType.APPLICATION_JSON)
    public Profile profile() {
        Profile result = new Profile();
        return result;
    }
}