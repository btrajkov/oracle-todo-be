package oracle.todo;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import oracle.todo.service.CategoryService;
import oracle.todo.util.TokenUtils;

@Path("/categories")
public class CategoryResource {
    @Inject
    CategoryService categoryService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ TokenUtils.ROLE_USER })
    public Response getAllCategories() {
        return Response.status(Response.Status.OK).entity(categoryService.getAllCategories()).build();
    }
}