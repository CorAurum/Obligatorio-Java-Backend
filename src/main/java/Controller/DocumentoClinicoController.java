package Controller;

import Class.Usuarios.usuarioDeSalud;
import Class.documentoClinico;
import Class.centroSalud;
import Class.Usuarios.profesionalDeSalud;
import Class.politicaDeAcceso;
import Service.DocumentoClinicoService;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Path("/documentoClinico")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DocumentoClinicoController {

    @Inject
    private DocumentoClinicoService documentoClinicoService;

    @PersistenceContext
    private EntityManager em;

    // DTO para recibir datos del POST
    public static class DocumentoClinicoDTO {
        public String titulo;
        public String idDocumentoOrigen;
        public String tipoDocumento;
        public String contenido;
        public boolean estado;
        public Long centroSaludId;
        public Long profesionalDeSaludId;
        public Long politicaDeAccesoId;
        public String usuarioDeSaludId;
        public String area;
        public String nombreProfesional;
        public String fechaCreacion;
    }

    // ================== ENDPOINTS CRUD ==================

    @POST
    @Transactional
    public Response crearDocumento(DocumentoClinicoDTO req) {
        try {
            documentoClinico nuevo = new documentoClinico();
            nuevo.setTitulo(req.titulo);
            nuevo.setTipoDocumento(req.tipoDocumento);
            nuevo.setContenido(req.contenido);
            nuevo.setEstado(req.estado);
            nuevo.setFechaCreacion(LocalDateTime.now());

            if (req.centroSaludId != null)
                nuevo.setCentroSalud(em.find(centroSalud.class, req.centroSaludId));
            if (req.profesionalDeSaludId != null)
                nuevo.setProfesionalDeSalud(em.find(profesionalDeSalud.class, req.profesionalDeSaludId));
            if (req.politicaDeAccesoId != null)
                nuevo.setPoliticaDeAcceso(em.find(politicaDeAcceso.class, req.politicaDeAccesoId));
            if (req.usuarioDeSaludId != null)
                nuevo.setUsuarioDeSalud(em.find(usuarioDeSalud.class, req.usuarioDeSaludId));

            em.persist(nuevo);
            return Response.status(Response.Status.CREATED).entity(nuevo).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError()
                    .entity("Error al crear documento clínico: " + e.getMessage())
                    .build();
        }
    }

    @GET
    public List<documentoClinico> listarDocumentos() {
        return documentoClinicoService.listarDocumentos();
    }

    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") Long idDocumento) {
        documentoClinico doc = documentoClinicoService.obtenerPorId(idDocumento);
        if (doc != null) {
            return Response.ok(doc).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizarDocumento(@PathParam("id") Long idDocumento, documentoClinico documentoActualizado) {
        documentoClinico actualizado = documentoClinicoService.actualizarDocumento(idDocumento, documentoActualizado);
        return Response.ok(actualizado).build();
    }

    @DELETE
    @Path("/{id}")
    public Response eliminarDocumento(@PathParam("id") Long idDocumento) {
        documentoClinicoService.eliminarDocumento(idDocumento);
        return Response.noContent().build();
    }

    // ================== ENDPOINT EXTERNO ==================

    @POST
    @Path("/externo")
    @Transactional
    public Response crearOActualizarDocumentoDesdePeriferico(DocumentoClinicoDTO req) {
        try {
            if (req.idDocumentoOrigen == null || req.idDocumentoOrigen.isBlank()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("El campo 'idDocumentoOrigen' es obligatorio.").build();
            }

            // Buscar si ya existe un documento con ese id de origen
            documentoClinico existente = em.createQuery(
                            "SELECT d FROM documentoClinico d WHERE d.idDocumentoOrigen = :idOrigen",
                            documentoClinico.class)
                    .setParameter("idOrigen", req.idDocumentoOrigen)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (existente == null) {
                // Crear nuevo documento
                documentoClinico nuevo = new documentoClinico();
                nuevo.setIdDocumentoOrigen(Long.valueOf(req.idDocumentoOrigen));
                nuevo.setArea(req.area);
                nuevo.setEstado(true);

                // Manejo de la fecha
                if (req.fechaCreacion != null && !req.fechaCreacion.trim().isEmpty()) {
                    try {
                        nuevo.setFechaCreacion(LocalDateTime.parse(req.fechaCreacion.trim(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    } catch (Exception e) {
                        System.out.println("⚠️ Fecha inválida, se usará la actual: " + req.fechaCreacion);
                        nuevo.setFechaCreacion(LocalDateTime.now());
                    }
                } else {
                    nuevo.setFechaCreacion(LocalDateTime.now());
                }

                // Buscar profesional por nombre
                if (req.nombreProfesional != null && !req.nombreProfesional.isBlank()) {
                    profesionalDeSalud profesional = em.createQuery(
                                    "SELECT p FROM profesionalDeSalud p WHERE p.nombre = :nombre",
                                    profesionalDeSalud.class)
                            .setParameter("nombre", req.nombreProfesional)
                            .getResultStream()
                            .findFirst()
                            .orElse(null);

                    nuevo.setProfesionalDeSalud(profesional);
                }

                // Buscar clínica por ID
                if (req.centroSaludId != null) {
                    centroSalud clinicaEntidad = em.find(centroSalud.class, req.centroSaludId);
                    nuevo.setCentroSalud(clinicaEntidad);
                }

                // Buscar usuario de salud por cédula
                if (req.usuarioDeSaludId != null) {
                    usuarioDeSalud usuario = em.find(usuarioDeSalud.class, req.usuarioDeSaludId);
                    nuevo.setUsuarioDeSalud(usuario);
                }

                em.persist(nuevo);
                return Response.status(Response.Status.CREATED)
                        .entity("Documento clínico creado correctamente (desde periférico)").build();

            } else {
                // Actualizar documento existente
                if (req.area != null && !req.area.isBlank())
                    existente.setArea(req.area);

                // Actualizar profesional si aplica
                if (req.nombreProfesional != null && !req.nombreProfesional.isBlank()) {
                    profesionalDeSalud profesional = em.createQuery(
                                    "SELECT p FROM profesionalDeSalud p WHERE p.nombre = :nombre",
                                    profesionalDeSalud.class)
                            .setParameter("nombre", req.nombreProfesional)
                            .getResultStream()
                            .findFirst()
                            .orElse(null);

                    existente.setProfesionalDeSalud(profesional);
                }

                em.merge(existente);
                return Response.ok("Documento clínico actualizado correctamente (desde periférico)").build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError()
                    .entity("Error al crear o actualizar documento clínico: " + e.getMessage())
                    .build();
        }
    }


}
