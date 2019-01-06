package com.tom;

import javax.jws.WebService;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@WebService
@Path("/users/")
public interface UserService {
    @GET
    @Path("")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    List<User> getUsers();

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response delete(@PathParam("id") int id);

    @POST
    @Path("add")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response insert(User user);

    @PUT
    @Path("update")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response update(User user);

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    User getUser(@PathParam("id") int id);
}