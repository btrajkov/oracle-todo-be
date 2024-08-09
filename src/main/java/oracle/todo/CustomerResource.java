package oracle.todo;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import oracle.todo.model.json.MessageJSON;
import oracle.todo.model.json.CustomerJSON;
import oracle.todo.service.CustomerService;

@Path("/users")
public class CustomerResource {
    @Inject
    CustomerService customerService;

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response register(CustomerJSON userJSON) {
        try {
            customerService.saveCustomer(userJSON);
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.CREATED).entity("User successfully registered.").build();
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response login(CustomerJSON userJSON) {
        CustomerJSON result = null;
        try {
            result = customerService.login(userJSON);
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.OK).entity(result).build();
    }
}
