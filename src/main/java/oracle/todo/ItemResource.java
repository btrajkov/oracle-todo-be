package oracle.todo;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import oracle.todo.model.json.ItemJSON;
import oracle.todo.service.ItemService;
import oracle.todo.util.TokenUtils;

@Path("/items")
public class ItemResource {
    @Inject
    ItemService itemService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ TokenUtils.ROLE_USER })
    public Response getAllItems() {
        return Response.status(Response.Status.OK).entity(itemService.getAllItems()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ TokenUtils.ROLE_USER })
    public Response getItem(@PathParam("id") Long id) {
        ItemJSON itemJSON = null;
        try {
            itemJSON = itemService.getItem(id);
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.OK).entity(itemJSON).build();
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed({ TokenUtils.ROLE_USER })
    public Response saveItem(ItemJSON itemJSON) {
        try {
            itemService.saveItem(itemJSON);
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.CREATED).entity("Item successfully saved.").build();
    }

    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed({ TokenUtils.ROLE_USER })
    public Response updateItem(ItemJSON itemJSON) {
        try {
            itemService.updateItem(itemJSON);
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.OK).entity("Item successfully updated.").build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed({ TokenUtils.ROLE_USER })
    public Response deleteItem(@PathParam("id") Long id) {
        try {
            itemService.deleteItem(id);
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.OK).entity("Item successfully deleted.").build();
    }
}
