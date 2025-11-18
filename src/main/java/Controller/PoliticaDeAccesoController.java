package Controller;

import Entity.DTO.DocumentoClinicoDTO;
import Entity.DTO.PoliticaDeAccesoDTO;
import Entity.Especialidad;
import Entity.PoliticaDeAcceso;
import Entity.Usuarios.ProfesionalDeSalud;
import Entity.Usuarios.Usuario;
import Repository.DocumentoClinicoRepository;
import Repository.PoliticaDeAccesoRepository;
import Repository.ProfesionalDeSaludRepository;
import Repository.UsuarioRepository;
import Service.AccesoLogService;
import Service.DocumentoClinicoService;
import Service.PoliticaDeAccesoService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Path("/politicas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PoliticaDeAccesoController {

    @Inject
    private PoliticaDeAccesoService politicaService;

    @Inject
    private PoliticaDeAccesoRepository politicaDeAccesoRepository;
    @Inject
    private ProfesionalDeSaludRepository profesionalDeSaludRepository;
    @Inject
    private UsuarioRepository usuarioRepository;
    @Inject
    private DocumentoClinicoService documentoClinicoService;
    @Inject
    private AccesoLogService accesoLogService;

    // Crear nueva política
    @POST
    public Response crear(Map<String, Object> payload) {
        try {
            String usuarioId = (String) payload.get("usuarioId");
            String centroId = (String) payload.get("centroId");
            List<String> especialidades = (List<String>) payload.get("especialidades");

            LocalDate vigenciaHasta = null;
            if (payload.get("vigenciaHasta") != null) {
                vigenciaHasta = LocalDate.parse((String) payload.get("vigenciaHasta"));
            }

            PoliticaDeAcceso creada = politicaService.crearPolitica(usuarioId, centroId, especialidades, vigenciaHasta);
            return Response.ok(creada).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // Listar políticas por usuario
    @GET
    @Path("/usuario/{usuarioId}")
    public Response listarPorUsuario(@PathParam("usuarioId") String usuarioId) {
        try {
            List<PoliticaDeAcceso> politicas = politicaDeAccesoRepository.listarPorUsuario(usuarioId);

            List<PoliticaDeAccesoDTO> resultado = politicas.stream().map(p -> {
                PoliticaDeAccesoDTO dto = new PoliticaDeAccesoDTO();
                dto.id = p.getId();
                dto.fechaCreacion = p.getFechaCreacion();
                dto.vigenciaHasta = p.getVigenciaHasta();
                dto.estado = p.getEstado().name();

                if (p.getCentroDeSalud() != null) {
                    dto.centroId = p.getCentroDeSalud().getId();
                    dto.centroNombre = p.getCentroDeSalud().getNombre();
                }

                dto.especialidades = p.getEspecialidades().stream().map(e -> {
                    PoliticaDeAccesoDTO.EspecialidadDto eDto = new PoliticaDeAccesoDTO.EspecialidadDto();
                    eDto.id = e.getId();
                    eDto.nombre = e.getNombre();
                    return eDto;
                }).toList();

                return dto;
            }).toList();

            return Response.ok(resultado).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    // Revocar política
    @PATCH
    @Path("/{id}/revocar")
    public Response revocar(@PathParam("id") String politicaId) {
        try {
            politicaService.revocarPolitica(politicaId);
            return Response.ok(Map.of("status", "revocada")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /* VER SI ES REALMENTE NECESARIA
    // Verificar acceso (para profesionales)
    @GET
    @Path("/verificar")
    public Response verificar(@QueryParam("usuarioId") String usuarioId,
                              @QueryParam("centroId") String centroId,
                              @QueryParam("especialidadId") String especialidadId) {
        try {
            boolean permitido = politicaService.verificarAcceso(usuarioId, centroId, especialidadId);
            return Response.ok(Map.of("permitido", permitido)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    } */

    @GET
    @Path("/historiaClinica")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response obtenerHistoria(@QueryParam("profesionalId") String profesionalId,
                                    @QueryParam("usuarioId") String usuarioId) {
        boolean permitido = politicaService.puedeAcceder(profesionalId, usuarioId);

        // Registrar intento en AccesoLog
        accesoLogService.registrarIntento(profesionalId, usuarioId, permitido);

        if (!permitido) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Acceso denegado: el profesional no tiene permisos para este usuario.")
                    .build();
        }

        // Si está permitido, devolver los documentos
        List<DocumentoClinicoDTO> docs = documentoClinicoService.listarPorUsuarioDTO(usuarioId);
        return Response.ok(docs).build();
    }



}

