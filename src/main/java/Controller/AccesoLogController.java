package Controller;


import Service.AccesoLogService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

// /accesos?usuarioId=abc123

@Path("/accesos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccesoLogController {

    @Inject
    AccesoLogService accesoLogService;

    @GET
    public Response listar(@QueryParam("usuarioId") String usuarioId) {
        if (usuarioId == null || usuarioId.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Debe proporcionar usuarioId")
                    .build();
        }

        var lista = accesoLogService.listarDTOporUsuario(usuarioId);
        return Response.ok(lista).build();
    }
}
