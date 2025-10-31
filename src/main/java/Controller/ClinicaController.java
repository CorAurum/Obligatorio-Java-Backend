package Controller;

import Entity.CentroDeSalud;
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
    public Response crearClinica(CentroDeSalud centroSalud) {
        clinicaService.crearClinica(centroSalud);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response listarClinicas() {
        List<CentroDeSalud> centroSaluds = clinicaService.listarClinicas();
        return Response.ok(centroSaluds).build();
    }

    @GET
    @Path("/{id}")
    public Response obtenerClinica(@PathParam("id") Long id) {
        CentroDeSalud centroSalud = clinicaService.obtenerClinica(id);
        if (centroSalud != null) {
            return Response.ok(centroSalud).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizarClinica(@PathParam("id") Long id, CentroDeSalud centroSalud) {
        centroSalud.setId(id);  // aseguramos que el ID sea el correcto
        CentroDeSalud actualizada = clinicaService.actualizarClinica(centroSalud);
        return Response.ok(actualizada).build();
    }

    @DELETE
    @Path("/{id}")
    public Response eliminarClinica(@PathParam("id") Long id) {
        clinicaService.eliminarClinica(id);
        return Response.noContent().build();
    }
}
