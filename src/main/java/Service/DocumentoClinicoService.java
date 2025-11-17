package Service;

import Entity.CentroDeSalud;
import Entity.DTO.DocumentoClinicoDTO;
import Entity.DTO.DocumentoClinicoParaUsuarioDTO;
import Entity.DocumentoClinico;
import Entity.DTO.DocumentoClinicoPayload;
import Entity.Usuarios.ProfesionalDeSalud;
import Entity.Usuarios.Usuario;
import Entity.Usuarios.UsuarioLocal;
import Repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;   // ⬅ NECESARIO!
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Stateless
@Transactional
public class DocumentoClinicoService {

    @Inject private DocumentoClinicoRepository documentoClinicoRepository;
    @Inject private CentroDeSaludRepository centroDeSaludRepository;
    @Inject private UsuarioLocalRepository usuarioLocalRepository;
    @Inject private UsuarioRepository usuarioRepository;
    @Inject private ProfesionalDeSaludRepository profesionalDeSaludRepository;

    // 🔹 Jackson personalizado (soporta LocalDateTime)
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 🔹 HttpClient para llamadas al periférico
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @PostConstruct
    private void init() {
        // ⬅️ Habilita soporte para LocalDate y LocalDateTime
        objectMapper.registerModule(new JavaTimeModule());
    }


    public static class ResultadoRegistro {
        public boolean success;
        public String message;
        public String documentoId;
        public String usuarioIdGolden;
    }

    // ----------------------------------------------------------------------------------------------------
    //                                         REGISTRAR DOCUMENTO
    // ----------------------------------------------------------------------------------------------------

    public ResultadoRegistro registrarDesdePeriferico(DocumentoClinicoPayload dto) {
        ResultadoRegistro out = new ResultadoRegistro();

        if (dto.centroId == null || dto.profesionalId == null || dto.usuarioLocalId == null) {
            out.success = false;
            out.message = "centroId, profesionalId y usuarioLocalId son obligatorios.";
            return out;
        }

        DocumentoClinico existente = documentoClinicoRepository.buscarPorIdOrigenYCentro(dto.idOrigen, dto.centroId);
        if (existente != null) {
            out.success = false;
            out.message = "Documento ya registrado con idOrigen " + dto.idOrigen;
            return out;
        }

        CentroDeSalud centro = centroDeSaludRepository.buscarPorId(dto.centroId);
        if (centro == null) {
            out.success = false;
            out.message = "Centro de salud no encontrado: " + dto.centroId;
            return out;
        }

        ProfesionalDeSalud profesional = profesionalDeSaludRepository.buscarPorId(dto.profesionalId);
        if (profesional == null) {
            out.success = false;
            out.message = "Profesional de salud no encontrado: " + dto.profesionalId;
            return out;
        }

        UsuarioLocal usuarioLocal = usuarioLocalRepository.buscarPorCentroYIdLocal(dto.centroId, dto.usuarioLocalId);
        if (usuarioLocal == null) {
            out.success = false;
            out.message = "No se encontró un UsuarioLocal con idLocal " + dto.usuarioLocalId +
                    " en el centro " + dto.centroId;
            return out;
        }

        Usuario usuarioGolden = usuarioRepository.buscarPorId(usuarioLocal.getUsuarioId());
        if (usuarioGolden == null) {
            out.success = false;
            out.message = "El UsuarioLocal no tiene un usuario Golden asociado.";
            return out;
        }

        DocumentoClinico doc = new DocumentoClinico();
        doc.setId(UUID.randomUUID().toString());
        doc.setIdOrigen(dto.idOrigen);
        doc.setCentroDeSalud(centro);
        doc.setUsuario(usuarioGolden);
        doc.setFechaCreacion(dto.fechaCreacion != null ? dto.fechaCreacion : LocalDateTime.now());
        doc.setEstado(DocumentoClinico.EstadoDocumento.ACTIVO);
        doc.setTitulo(dto.titulo);
        doc.setDescripcion(dto.descripcion);
        doc.setTipoDocumento(dto.tipoDocumento);
        doc.setAutorProfesional(profesional);
        doc.setArea(dto.area);
        doc.setUrlAlojamiento(dto.urlAlojamiento);

        documentoClinicoRepository.crear(doc);

        out.success = true;
        out.documentoId = doc.getId();
        out.usuarioIdGolden = usuarioGolden.getId();
        out.message = "Documento registrado correctamente.";
        return out;
    }


    // ----------------------------------------------------------------------------------------------------
    //                       OBTENER DOCUMENTO COMPLETO DESDE PERIFÉRICO
    // ----------------------------------------------------------------------------------------------------

    /**
     * Llama al periférico usando la URL almacenada (urlAlojamiento),
     * recibe el JSON original y lo transforma a DocumentoClinicoParaUsuarioDTO.
     */
    public DocumentoClinicoParaUsuarioDTO obtenerDocumentoCompleto(String documentoId) {

        DocumentoClinico metadata = documentoClinicoRepository.buscarPorId(documentoId);
        if (metadata == null) {
            throw new RuntimeException("Documento no encontrado en el Central");
        }

        String urlDetalle = metadata.getUrlAlojamiento();

        if (urlDetalle == null) {
            throw new RuntimeException("El documento no tiene URL de alojamiento registrada");
        }

        try {
            // GET al periférico
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(urlDetalle))
                    .GET()
                    .build();

            HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            String json = res.body();

            // Mostrar JSON crudo (para debug)
            System.out.println("\n📥 JSON RECIBIDO DESDE PERIFÉRICO:");
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                    objectMapper.readTree(json)
            ));

            // Convertir JSON → DTO con soporte LocalDateTime
            return objectMapper.readValue(json, DocumentoClinicoParaUsuarioDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("Error al recuperar documento desde periférico: " + e.getMessage(), e);
        }
    }


    // ----------------------------------------------------------------------------------------------------
    //                                          LISTADOS
    // ----------------------------------------------------------------------------------------------------

    public DocumentoClinico buscarPorId(String id) {
        return documentoClinicoRepository.buscarPorId(id);
    }

    public List<DocumentoClinico> listarPorUsuario(String usuarioId) {
        return documentoClinicoRepository.listarPorUsuario(usuarioId);
    }

    public List<DocumentoClinicoDTO> listarPorUsuarioDTO(String usuarioId) {
        List<DocumentoClinico> docs = documentoClinicoRepository.listarPorUsuario(usuarioId);
        return docs.stream()
                .map(d -> new DocumentoClinicoDTO(
                        d.getFechaCreacion(),
                        d.getArea(),
                        d.getDescripcion(),
                        d.getUrlAlojamiento(),
                        d.getAutorProfesional() != null ? d.getAutorProfesional().getNombres() : null,
                        d.getAutorProfesional() != null ? d.getAutorProfesional().getApellidos() : null
                ))
                .toList();
    }

    public List<DocumentoClinico> listarTodos() {
        return documentoClinicoRepository.listarTodos();
    }
}
