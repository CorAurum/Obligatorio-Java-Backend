package Controller;


import Entity.Usuarios.Administrador;
import Service.AdministradorService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/administradores")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdministradorController {

    @Inject
    private AdministradorService administradorService;

    // POST /administradores
    @POST
    public Response crearAdministrador(Administrador admin) {
        try {
            Administrador creado = administradorService.crearAdministrador(admin);
            return Response.status(Response.Status.CREATED).entity(creado).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al crear administrador: " + e.getMessage())
                    .build();
        }
    }

    // GET /administradores
    @GET
    public Response listarAdministradores() {
        List<Administrador> lista = administradorService.listarAdministradores();
        return Response.ok(lista).build();
    }

    // GET /administradores/{id}
    @GET
    @Path("/{id}")
    public Response obtenerAdministrador(@PathParam("id") Long id) {
        Administrador admin = administradorService.obtenerPorId(id);
        if (admin == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Administrador no encontrado").build();
        }
        return Response.ok(admin).build();
    }

    // PUT /administradores/{id}
    @PUT
    @Path("/{id}")
    public Response actualizarAdministrador(@PathParam("id") Long id, Administrador admin) {
        try {
            admin.setId(id);
            Administrador actualizado = administradorService.actualizarAdministrador(admin);
            return Response.ok(actualizado).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al actualizar: " + e.getMessage())
                    .build();
        }
    }

    // DELETE /administradores/{id}
    @PUT
    @Path("/{id}/inhabilitar")
    public Response AjustaAdministrador(@PathParam("id") Long id) {
        try {
            administradorService.AjustarAdministrador(id);
            return Response.ok("Administrador eliminado").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al eliminar: " + e.getMessage())
                    .build();
        }
    }

    // GET /administradores/cedula/{cedula}
    @GET
    @Path("/cedula/{cedula}")
    public Response obtenerPorCedula(@PathParam("cedula") String cedula) {
        try {
            Administrador admin = administradorService.obtenerPorCedula(cedula);
            if (admin == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontró administrador con cédula " + cedula)
                        .build();
            }
            return Response.ok(admin).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al buscar administrador: " + e.getMessage())
                    .build();
        }
    }
}
