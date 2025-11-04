package Controller;

import Entity.Usuarios.ProfesionalDeSalud;
import Service.ProfesionalDeSaludService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/profesionales")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProfesionalDeSaludController {

    @Inject
    private ProfesionalDeSaludService profesionalDeSaludService;

    @POST
    public Response registrarProfesional(ProfesionalDeSalud profesional,
                                         @QueryParam("centroId") String centroId,
                                         @QueryParam("adminId") Long adminId) {
        try {
            ProfesionalDeSalud creado = profesionalDeSaludService.registrarProfesional(profesional, centroId, adminId);
            return Response.ok(creado).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
