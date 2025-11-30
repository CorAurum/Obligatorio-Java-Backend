package Controller;

import Entity.AccesoRequest;
import Entity.Usuarios.ProfesionalDeSalud;
import Service.AccesoRequestService;
import Service.NotificacionService;
import Service.ProfesionalDeSaludService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/acceso")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccesoRequestController {

    @Inject
    private AccesoRequestService accesoRequestService;

    @Inject
    private ProfesionalDeSaludService profesionalDeSaludService;

    @Inject
    private NotificacionService notificacionService;

    @POST
    @Path("/solicitud")
    public Response solicitarAcceso(@QueryParam("profesionalId") String profesionalId,
            @QueryParam("usuarioId") String usuarioId,
            @QueryParam("motivo") String motivo) {
        AccesoRequest req = accesoRequestService.crearSolicitud(profesionalId, usuarioId, motivo);

        ProfesionalDeSalud prof = profesionalDeSaludService.obtenerPorId(profesionalId);

        notificacionService.crear(
                usuarioId,
                "Nueva solicitud de acceso",
                "El profesional " + prof.getNombres() + prof.getApellidos() +
                        "De la clinica" + prof.getCentroDeSalud().getNombre() + " solicitó acceso a tu historia clínica."
        );


        return Response.ok(req).build();
    }

    @PUT
    @Path("/solicitud/{id}/aprobar")
    public Response aprobar(@PathParam("id") String id) {
        accesoRequestService.aprobarSolicitud(id);
        AccesoRequest req = accesoRequestService.buscarPorId(id);

        // No creo usarlo
//        notificacionService.crear(
//                req.getProfesionalSolicitante().getId(),
//                "Solicitud aprobada",
//                "Tu solicitud para acceder al usuario " + req.getUsuario().getNombres() + req.getUsuario().getApellidos() + " fue aprobada."
//        );

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
