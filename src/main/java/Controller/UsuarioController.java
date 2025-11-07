package Controller;

import Entity.DTO.UsuarioLocalPayload;
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
    @Path("/externo")
    public Response crearOActualizarDesdePeriferico(UsuarioLocalPayload payload,
                                                    @QueryParam("forceMerge") @DefaultValue("false") boolean forceMerge) {
        try {
            UsuarioService.ResultadoSync res = usuarioService.syncFromPeriferico(payload, forceMerge);
            if (res.conflict) {
                return Response.status(Response.Status.CONFLICT).entity(res).build();
            }
            return Response.ok(res).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    public Response listarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.listarUsuarios();
            return Response.ok(usuarios).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response obtenerUsuario(@PathParam("id") String id) {
        try {
            Usuario usuario = usuarioService.buscarPorId(id);
            if (usuario == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Usuario no encontrado").build();
            }
            return Response.ok(usuario).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage()).build();
        }
    }


}

