package Controller;


import Entity.CentroDeSalud;
import Service.CentroDeSaludService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/CentroDeSalud")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CentroDeSaludController {

    @Inject
    private CentroDeSaludService centroDeSaludService;

    @POST
    public Response crearCentro(CentroDeSalud centro, @QueryParam("adminId") Long adminId) {
        try {
            CentroDeSalud creado = centroDeSaludService.crearCentro(centro, adminId);
            return Response.ok(creado).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    public List<CentroDeSalud> listar() {
        return centroDeSaludService.listarCentros();
    }


    @PUT
    @Path("/{id}/inhabilitar")
    public Response inhabilitarCentro(@PathParam("id") String id) {
        try {
            CentroDeSalud actualizado = centroDeSaludService.inhabilitarCentro(id);
            return Response.ok(actualizado).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

}

