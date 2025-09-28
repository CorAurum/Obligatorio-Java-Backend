package Controller;

import Class.Clinica;
import Service.ClinicaService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/clinicas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClinicaController {

    @Inject
    private ClinicaService clinicaService;

    @POST
    public Response crearClinica(Clinica clinica) {
        clinicaService.crearClinica(clinica);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response listarClinicas() {
        List<Clinica> clinicas = clinicaService.listarClinicas();
        return Response.ok(clinicas).build();
    }

    @GET
    @Path("/{id}")
    public Response obtenerClinica(@PathParam("id") Long id) {
        Clinica clinica = clinicaService.obtenerClinica(id);
        if (clinica != null) {
            return Response.ok(clinica).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizarClinica(@PathParam("id") Long id, Clinica clinica) {
        clinica.setIdClinica(id);  // aseguramos que el ID sea el correcto
        Clinica actualizada = clinicaService.actualizarClinica(clinica);
        return Response.ok(actualizada).build();
    }

    @DELETE
    @Path("/{id}")
    public Response eliminarClinica(@PathParam("id") Long id) {
        clinicaService.eliminarClinica(id);
        return Response.noContent().build();
    }
}
