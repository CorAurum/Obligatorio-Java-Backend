package Controller;

import Entity.Usuarios.IdentificadorUsuario;
import Entity.Usuarios.Usuario;
import Entity.Usuarios.UsuarioLocal;
import Service.UsuarioService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/usuarios")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioController {

    @Inject
    private UsuarioService usuarioService;

    @POST
    public Response registrarUsuario(
            Usuario usuario,
            @QueryParam("centroId") String centroId,
            @QueryParam("crearLocal") @DefaultValue("true") boolean crearLocal,
            List<IdentificadorUsuario> identificadores,
            UsuarioLocal usuarioLocal) {
        try {
            Usuario creado = usuarioService.crearOActualizarUsuario(usuario, centroId, identificadores, usuarioLocal);
            return Response.ok(creado).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
