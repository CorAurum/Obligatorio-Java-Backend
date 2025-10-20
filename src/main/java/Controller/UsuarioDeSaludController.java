package Controller;

import Class.Usuarios.usuarioDeSalud;
import Service.UsuarioDeSaludService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioDeSaludController {

    @Inject
    private UsuarioDeSaludService service;

    @POST
    public Response crearUsuario(usuarioDeSalud usuario) {
        service.crearUsuario(usuario);
        return Response.status(Response.Status.CREATED).entity(usuario).build();
    }

    @GET
    public List<usuarioDeSalud> listarUsuarios() {
        return service.listarUsuarios();
    }

    @GET
    @Path("/{cedula}")
    public Response obtenerPorCedula(@PathParam("cedula") String cedula) {
        usuarioDeSalud usuario = service.obtenerPorCedula(cedula);
        if (usuario != null) {
            return Response.ok(usuario).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{cedula}")
    public Response actualizarUsuario(@PathParam("cedula") String cedula, usuarioDeSalud usuarioActualizado) {
        usuarioDeSalud actualizado = service.actualizarUsuario(cedula, usuarioActualizado);
        return Response.ok(actualizado).build();
    }

    @DELETE
    @Path("/{cedula}")
    public Response eliminarUsuario(@PathParam("cedula") String cedula) {
        service.eliminarUsuario(cedula);
        return Response.noContent().build();
    }
}
