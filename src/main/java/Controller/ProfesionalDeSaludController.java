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

    @GET
    public Response listarProfesionales() {
        try {
            return Response.ok(profesionalDeSaludService.listarProfesionales()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") String id) {
        try {
            ProfesionalDeSalud profesional = profesionalDeSaludService.obtenerPorId(id);
            if (profesional == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Profesional no encontrado").build();
            }
            return Response.ok(profesional).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizarProfesional(@PathParam("id") String id, ProfesionalDeSalud profesional) {
        try {
            ProfesionalDeSalud actualizado = profesionalDeSaludService.actualizarProfesional(id, profesional);
            return Response.ok(actualizado).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
