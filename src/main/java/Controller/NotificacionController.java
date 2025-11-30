
package Controller;

import Entity.Notificacion;
import Service.NotificacionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Path("/notificaciones")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificacionController {

    @Inject
    NotificacionService service;

    // (A) Polling → obtener no leídas
    @GET
    @Path("/nuevas/{userId}")
    public Response obtenerNoLeidas(@PathParam("userId") String userId) {
        List<Notificacion> res = service.obtenerNoLeidas(userId);
        return Response.ok(res).build();
    }

    // (B) Obtener todas (para panel de notificaciones)
    @GET
    @Path("/todas/{userId}")
    public Response obtenerTodas(@PathParam("userId") String userId) {
        List<Notificacion> res = service.obtenerTodas(userId);
        return Response.ok(res).build();
    }

    // (C) Marcar como leídas
    @POST
    @Path("/marcar-leidas/{userId}")
    public Response marcarComoLeidas(@PathParam("userId") String userId) {
        service.marcarComoLeidas(userId);
        return Response.ok().build();
    }

    // (D) Crear una notificación (debug/testing)
    @POST
    @Path("/crear")
    public Response crear(Notificacion req) {
        Notificacion n = service.crear(req.getUserId(), req.getTitulo(), req.getMensaje());
        return Response.ok(n).build();
    }
}
