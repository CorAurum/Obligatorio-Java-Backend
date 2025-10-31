package Controller;

import Entity.Usuarios.UsuarioLocal;
import Entity.DocumentoClinico;
import Entity.CentroDeSalud;
import Entity.Usuarios.profesionalDeSalud;
import Entity.politicaDeAcceso;
import Service.DocumentoClinicoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
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
            DocumentoClinico nuevo = new DocumentoClinico();
            nuevo.setTitulo(req.titulo);
            nuevo.setTipoDocumento(req.tipoDocumento);
            nuevo.setContenido(req.contenido);
            nuevo.setEstado(req.estado);
            nuevo.setFechaCreacion(LocalDateTime.now());

            if (req.centroSaludId != null)
                nuevo.setCentroSalud(em.find(CentroDeSalud.class, req.centroSaludId));
            if (req.profesionalDeSaludId != null)
                nuevo.setProfesionalDeSalud(em.find(profesionalDeSalud.class, req.profesionalDeSaludId));
            if (req.politicaDeAccesoId != null)
                nuevo.setPoliticaDeAcceso(em.find(politicaDeAcceso.class, req.politicaDeAccesoId));
            if (req.usuarioDeSaludId != null)
                nuevo.setUsuarioDeSalud(em.find(UsuarioLocal.class, req.usuarioDeSaludId));

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
    public List<DocumentoClinico> listarDocumentos() {
        return documentoClinicoService.listarDocumentos();
    }

    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") Long idDocumento) {
        DocumentoClinico doc = documentoClinicoService.obtenerPorId(idDocumento);
        if (doc != null) {
            return Response.ok(doc).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizarDocumento(@PathParam("id") Long idDocumento, DocumentoClinico documentoActualizado) {
        DocumentoClinico actualizado = documentoClinicoService.actualizarDocumento(idDocumento, documentoActualizado);
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
    public Response crearOActualizarDocumentoDesdePeriferico(String rawJson) {
        System.out.println("📩 JSON crudo recibido: " + rawJson);

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();

            // Adaptamos el DTO a los nombres del JSON actual
            class DocumentoMetadatoDto {
                public Long idDocumento;
                public Long idProfesional;
                public Long idUsuario;
                public Long idClinica;
                public String area;
                public String cedulaUsuario;
            }

            DocumentoMetadatoDto req = mapper.readValue(rawJson, DocumentoMetadatoDto.class);

            if (req.idDocumento == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("El campo 'idDocumento' es obligatorio.").build();
            }

            DocumentoClinico existente = em.createQuery(
                            "SELECT d FROM DocumentoClinico d WHERE d.idDocumentoOrigen = :idOrigen",
                            DocumentoClinico.class)
                    .setParameter("idOrigen", req.idDocumento)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (existente == null) {
                System.out.println("🆕 Creando nuevo documento desde periférico...");
                DocumentoClinico nuevo = new DocumentoClinico();
                nuevo.setIdDocumentoOrigen(req.idDocumento);
                nuevo.setArea(req.area);
                nuevo.setEstado(true);
                nuevo.setFechaCreacion(LocalDateTime.now());

                // Buscar profesional por ID
                if (req.idProfesional != null) {
                    profesionalDeSalud profesional = em.find(profesionalDeSalud.class, req.idProfesional);
                    nuevo.setProfesionalDeSalud(profesional);
                }

                // Buscar clínica
                if (req.idClinica != null) {
                    CentroDeSalud clinicaEntidad = em.find(CentroDeSalud.class, req.idClinica);
                    nuevo.setCentroSalud(clinicaEntidad);
                }

                // Buscar usuario por cédula
                if (req.cedulaUsuario != null && !req.cedulaUsuario.isBlank()) {
                    UsuarioLocal usuario = em.find(UsuarioLocal.class, req.cedulaUsuario);
                    nuevo.setUsuarioDeSalud(usuario);
                }

                em.persist(nuevo);
                System.out.println("✅ Documento clínico creado correctamente (desde periférico)");
                return Response.status(Response.Status.CREATED)
                        .entity("Documento clínico creado correctamente (desde periférico)").build();

            } else {
                System.out.println("✏️ Actualizando documento existente desde periférico...");

                if (req.area != null && !req.area.isBlank())
                    existente.setArea(req.area);

                if (req.idProfesional != null) {
                    profesionalDeSalud profesional = em.find(profesionalDeSalud.class, req.idProfesional);
                    existente.setProfesionalDeSalud(profesional);
                }

                em.merge(existente);
                System.out.println("✅ Documento clínico actualizado correctamente (desde periférico)");
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


