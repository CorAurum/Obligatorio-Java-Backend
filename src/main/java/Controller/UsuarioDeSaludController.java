package Controller;

import Class.Usuarios.usuarioDeSalud;
import Class.pdi.pdiResponse;
import Service.UsuarioDeSaludService;
import Service.pdiService;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Path("/usuarioSalud")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioDeSaludController {

    @Inject
    private UsuarioDeSaludService usuarioDeSaludService;

    @PersistenceContext
    private EntityManager em;

    // DTO (objeto para recibir los datos del POST)
    public static class UsuarioDTO {
        public String cedulaIdentidad;
        public String email;
        public String nombre;
        public String apellido;
        public String direccion;
        public String telefono;
        public String fechaNacimiento; // formato: yyyy-MM-dd
    }

    @POST // Si no existe devuelve 201, si existe y se actualiza devuelve 200
    @Transactional
    public Response crearOActualizarUsuario(UsuarioDTO req) {
        try {
            if (req.cedulaIdentidad == null || req.cedulaIdentidad.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("El campo 'cedulaIdentidad' es obligatorio.").build();
            }

            // Buscar usuario existente por cedulaIdentidad
            usuarioDeSalud existente = em.find(usuarioDeSalud.class, req.cedulaIdentidad);

            if (existente == null) {
                // 🆕 Crear nuevo usuario
                usuarioDeSalud nuevo = new usuarioDeSalud();
                nuevo.setCi(req.cedulaIdentidad);
                nuevo.setEmail(req.email);
                nuevo.setNombre(req.nombre);
                nuevo.setApellido(req.apellido);
                nuevo.setDireccion(req.direccion);
                nuevo.setTelefono(req.telefono);
                nuevo.setFechaNacimiento(LocalDate.parse(req.fechaNacimiento));
                nuevo.setFechaRegistro(LocalDateTime.now());

                em.persist(nuevo);

                return Response.status(Response.Status.CREATED)
                        .entity(nuevo).build();

            } else {
                // ✏️ Actualizar usuario existente
                existente.setEmail(req.email);
                existente.setNombre(req.nombre);
                existente.setApellido(req.apellido);
                existente.setDireccion(req.direccion);
                existente.setTelefono(req.telefono);
                existente.setFechaNacimiento(LocalDate.parse(req.fechaNacimiento));
                // fechaRegistro se mantiene igual

                em.merge(existente);

                return Response.ok(existente).build();
            }

        } catch (Exception e) {
            return Response.serverError()
                    .entity("Error al crear o actualizar usuario: " + e.getMessage())
                    .build();
        }
    }

    @GET
    public List<usuarioDeSalud> listarUsuarios() {
        return usuarioDeSaludService.listarUsuarios();
    }

    @GET
    @Path("/{cedula}")
    public Response obtenerPorCedula(@PathParam("cedula") String cedula) {
        usuarioDeSalud usuario = usuarioDeSaludService.obtenerPorCedula(cedula);
        if (usuario != null) {
            return Response.ok(usuario).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{cedula}")
    public Response actualizarUsuario(@PathParam("cedula") String cedula, usuarioDeSalud usuarioActualizado) {
        usuarioDeSalud actualizado = usuarioDeSaludService.actualizarUsuario(cedula, usuarioActualizado);
        return Response.ok(actualizado).build();
    }

    @DELETE
    @Path("/{cedula}")
    public Response eliminarUsuario(@PathParam("cedula") String cedula) {
        usuarioDeSaludService.eliminarUsuario(cedula);
        return Response.noContent().build();
    }

    // FIN ABML BASICO

    // ENDPOINTS PARA COMUNICACIONES CON LOS COMPONENTES PERIFERICOS

    @POST
    @Path("/externo") // endpoint expuesto a componentes periféricos
    @Transactional
    public Response crearOActualizarUsuarioDesdePeriferico(UsuarioDTO req) {
        try {
            if (req.cedulaIdentidad == null || req.cedulaIdentidad.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("El campo 'cedulaIdentidad' es obligatorio.").build();
            }

            // Buscar usuario existente por cedulaIdentidad
            usuarioDeSalud existente = em.find(usuarioDeSalud.class, req.cedulaIdentidad);

            if (existente == null) {
                // 🆕 Crear nuevo usuario
                usuarioDeSalud nuevo = new usuarioDeSalud();
                nuevo.setCi(req.cedulaIdentidad);
                if (req.email != null && !req.email.isBlank()) nuevo.setEmail(req.email);
                if (req.nombre != null && !req.nombre.isBlank()) nuevo.setNombre(req.nombre);
                if (req.apellido != null && !req.apellido.isBlank()) nuevo.setApellido(req.apellido);
                if (req.direccion != null && !req.direccion.isBlank()) nuevo.setDireccion(req.direccion);
                if (req.telefono != null && !req.telefono.isBlank()) nuevo.setTelefono(req.telefono);
                if (req.fechaNacimiento != null && !req.fechaNacimiento.isBlank())
                    nuevo.setFechaNacimiento(LocalDate.parse(req.fechaNacimiento));
                nuevo.setFechaRegistro(LocalDateTime.now());

                em.persist(nuevo);

                return Response.status(Response.Status.CREATED)
                        .entity("Usuario creado correctamente (desde componente periférico)").build();

            } else {
                // ✏️ Actualizar usuario existente SOLO si vienen valores nuevos
                if (req.email != null && !req.email.isBlank()) existente.setEmail(req.email);
                if (req.nombre != null && !req.nombre.isBlank()) existente.setNombre(req.nombre);
                if (req.apellido != null && !req.apellido.isBlank()) existente.setApellido(req.apellido);
                if (req.direccion != null && !req.direccion.isBlank()) existente.setDireccion(req.direccion);
                if (req.telefono != null && !req.telefono.isBlank()) existente.setTelefono(req.telefono);
                if (req.fechaNacimiento != null && !req.fechaNacimiento.isBlank())
                    existente.setFechaNacimiento(LocalDate.parse(req.fechaNacimiento));
                // No se toca fechaRegistro (solo se setea al crear)

                em.merge(existente);

                return Response.ok("Usuario actualizado correctamente (desde componente periférico)").build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError()
                    .entity("Error al crear o actualizar usuario: " + e.getMessage())
                    .build();
        }
    }



}

