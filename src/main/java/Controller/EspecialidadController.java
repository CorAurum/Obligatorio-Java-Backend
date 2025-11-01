package Controller;

import Entity.Especialidad;
import Service.EspecialidadService;
import Repository.EspecialidadRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/especialidades")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EspecialidadController {

    @Inject
    private EspecialidadRepository especialidadRepository;

    @Inject
    private EspecialidadService especialidadService;

    // 🔹 POST /especialidades → Crear especialidad
    @POST
    public Response crearEspecialidad(Especialidad especialidad) {
        try {
            if (especialidad.getNombre() == null || especialidad.getNombre().isBlank()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("El nombre de la especialidad es obligatorio.")
                        .build();
            }

            Especialidad creada = especialidadService.crearSiNoExiste(
                    especialidad.getNombre(),
                    especialidad.getDescripcion()
            );
            return Response.status(Response.Status.CREATED).entity(creada).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al crear especialidad: " + e.getMessage())
                    .build();
        }
    }

    // 🔹 GET /especialidades → Listar todas las especialidades
    @GET
    public Response listarEspecialidades() {
        try {
            List<Especialidad> lista = especialidadRepository.listarTodos();
            return Response.ok(lista).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar especialidades: " + e.getMessage())
                    .build();
        }
    }

    // 🔹 GET /especialidades/{id} → Obtener una especialidad por ID
    @GET
    @Path("/{id}")
    public Response obtenerEspecialidad(@PathParam("id") String id) {
        Especialidad esp = especialidadRepository.buscarPorId(id);
        if (esp == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Especialidad no encontrada con ID: " + id)
                    .build();
        }
        return Response.ok(esp).build();
    }

    // 🔹 PUT /especialidades/{id} → Actualizar una especialidad existente
    @PUT
    @Path("/{id}")
    public Response actualizarEspecialidad(@PathParam("id") String id, Especialidad especialidad) {
        try {
            Especialidad existente = especialidadRepository.buscarPorId(id);
            if (existente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Especialidad no encontrada con ID: " + id)
                        .build();
            }

            existente.setNombre(especialidad.getNombre());
            existente.setDescripcion(especialidad.getDescripcion());
            especialidadRepository.actualizar(existente);
            return Response.ok(existente).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al actualizar especialidad: " + e.getMessage())
                    .build();
        }
    }

    // 🔹 DELETE /especialidades/{id} → Eliminar especialidad
    @DELETE
    @Path("/{id}")
    public Response eliminarEspecialidad(@PathParam("id") String id) {
        try {
            Especialidad existente = especialidadRepository.buscarPorId(id);
            if (existente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Especialidad no encontrada con ID: " + id)
                        .build();
            }

            especialidadRepository.eliminar(existente);
            return Response.ok("Especialidad eliminada correctamente.").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al eliminar especialidad: " + e.getMessage())
                    .build();
        }
    }
}
