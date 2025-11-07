package Controller;

import Service.IdentificadorUsuarioService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/identificadores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IdentificadorUsuarioController {

    @Inject
    private IdentificadorUsuarioService identificadorUsuarioService;

    /**
     * GET /api/identificadores/usuario/ci/{cedula}
     * Example: /api/identificadores/usuario/ci/4.567.123-4
     *
     * Returns a list of Usuario IDs that match this CI.
     */
    @GET
    @Path("/usuario/ci/{cedula}")
    public Response buscarPorCedula(@PathParam("cedula") String cedula) {
        try {
            List<String> usuarioIds = identificadorUsuarioService.buscarUsuarioIdsPorCedula(cedula);
            if (usuarioIds.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontró ningún usuario con la cédula " + cedula)
                        .build();
            }
            return Response.ok(usuarioIds).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error interno: " + e.getMessage())
                    .build();
        }
    }
}
