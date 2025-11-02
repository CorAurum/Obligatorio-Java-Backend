package Service;

import Entity.CentroDeSalud;
import Entity.DTO.DocumentoClinicoDTO;
import Entity.DocumentoClinico;
import Entity.DTO.DocumentoClinicoPayload;
import Entity.Usuarios.ProfesionalDeSalud;
import Entity.Usuarios.Usuario;
import Entity.Usuarios.UsuarioLocal;
import Repository.*;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

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

    public static class ResultadoRegistro {
        public boolean success;
        public String message;
        public String documentoId;
        public String usuarioIdGolden;
    }

    public ResultadoRegistro registrarDesdePeriferico(DocumentoClinicoPayload dto) {
        ResultadoRegistro out = new ResultadoRegistro();

        // 🔹 Validaciones básicas
        if (dto.centroId == null || dto.profesionalId == null || dto.usuarioLocalId == null) {
            out.success = false;
            out.message = "centroId, profesionalId y usuarioLocalId son obligatorios.";
            return out;
        }

        // 🔹 Verificar duplicado
        DocumentoClinico existente = documentoClinicoRepository.buscarPorIdOrigenYCentro(dto.idOrigen, dto.centroId);
        if (existente != null) {
            out.success = false;
            out.message = "Documento ya registrado con idOrigen " + dto.idOrigen;
            return out;
        }

        // 🔹 Buscar centro
        CentroDeSalud centro = centroDeSaludRepository.buscarPorId(dto.centroId);
        if (centro == null) {
            out.success = false;
            out.message = "Centro de salud no encontrado: " + dto.centroId;
            return out;
        }

        // Buscar ProfesionalDeSalud
        ProfesionalDeSalud Profesional = profesionalDeSaludRepository.buscarPorId(dto.profesionalId);
        if (Profesional == null) {
            out.success = false;
            out.message = "Profesional de salud no encontrado: " + dto.profesionalId;
            return out;
        }

        // 🔹 Resolver usuario desde UsuarioLocal
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

        // 🔹 Crear documento
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
        doc.setAutorProfesional(Profesional);
        doc.setArea(dto.area);
        doc.setUrlAlojamiento(dto.urlAlojamiento);

        documentoClinicoRepository.crear(doc);

        out.success = true;
        out.documentoId = doc.getId();
        out.usuarioIdGolden = usuarioGolden.getId();
        out.message = "Documento registrado correctamente.";
        return out;
    }

    public DocumentoClinico buscarPorId(String id) {
        return documentoClinicoRepository.buscarPorId(id);
    }

    public java.util.List<DocumentoClinico> listarPorUsuario(String usuarioId) {
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

    public java.util.List<DocumentoClinico> listarTodos() {
        return documentoClinicoRepository.listarTodos();
    }
}
