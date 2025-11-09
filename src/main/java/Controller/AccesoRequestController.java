package Controller;


import Entity.AccesoRequest;
import Entity.DTO.DocumentoClinicoDTO;
import Entity.DTO.PoliticaDeAccesoDTO;
import Entity.Especialidad;
import Entity.PoliticaDeAcceso;
import Entity.Usuarios.ProfesionalDeSalud;
import Entity.Usuarios.Usuario;
import Repository.DocumentoClinicoRepository;
import Repository.PoliticaDeAccesoRepository;
import Repository.ProfesionalDeSaludRepository;
import Repository.UsuarioRepository;
import Service.AccesoLogService;
import Service.AccesoRequestService;
import Service.DocumentoClinicoService;
import Service.PoliticaDeAccesoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@Path("/acceso")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON) public class AccesoRequestController {

    @Inject private AccesoRequestService accesoRequestService;

    @POST
    @Path("/solicitud")
    public Response solicitarAcceso(@QueryParam("profesionalId") String profesionalId,
                                    @QueryParam("usuarioId") String usuarioId,
                                    @QueryParam("motivo") String motivo) {
        AccesoRequest req = accesoRequestService.crearSolicitud(profesionalId, usuarioId, motivo);
        return Response.ok(req).build();
    }

    @PUT
    @Path("/solicitud/{id}/aprobar")
    public Response aprobar(@PathParam("id") String id) {
        accesoRequestService.aprobarSolicitud(id);
        return Response.ok("Solicitud aprobada").build();
    }

    @PUT
    @Path("/solicitud/{id}/rechazar")
    public Response rechazar(@PathParam("id") String id) {
        accesoRequestService.rechazarSolicitud(id);
        return Response.ok("Solicitud rechazada").build();
    }

    @GET
    @Path("/solicitudes/pendientes")
    public Response listarPendientes() {
        return Response.ok(accesoRequestService.listarPendientes()).build();
    }

    @GET
    @Path("/solicitudes/pendientes/{id}")
    public Response listarPendientesPorUsuario(@PathParam("id") String id) {
        return Response.ok(accesoRequestService.listarPendientesPorUsuario(id)).build();
}
}


