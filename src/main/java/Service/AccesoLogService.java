package Service;

import Entity.AccesoLog;
import Entity.DTO.AccesoLogDTO;
import Entity.Usuarios.Usuario;
import Entity.DocumentoClinico;
import Repository.AccesoLogRepository;
import Repository.ProfesionalDeSaludRepository;
import Repository.UsuarioRepository;
import Repository.DocumentoClinicoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@ApplicationScoped
public class AccesoLogService {

    @Inject
    private AccesoLogRepository accesoLogRepository;

    @Inject
    private UsuarioRepository usuarioRepository;

    @Inject
    private DocumentoClinicoRepository documentoClinicoRepository;

    @Inject
    private ProfesionalDeSaludRepository profesionalDeSaludRepository;


    /**
     * Registra un intento de acceso (exitoso o fallido) a los datos de un usuario.
     *
     * @param profesionalId ID del profesional que intenta acceder
     * @param usuarioId ID del usuario (paciente)
     * @param resultado true si el acceso fue permitido, false si fue denegado
     */
    public void registrarIntento(String profesionalId, String usuarioId, boolean resultado) {
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId);
        if (usuario == null) throw new IllegalArgumentException("Usuario no encontrado");

        AccesoLog log = new AccesoLog();
        log.setId(UUID.randomUUID().toString());
        log.setUsuarioSolicitante(profesionalId);
        log.setUsuario(usuario);
        log.setFechaAcceso(LocalDateTime.now());
        log.setResultado(resultado);
        log.setMotivo(resultado ? "Acceso autorizado por política de acceso" : "Acceso denegado por falta de política");

        accesoLogRepository.crear(log);
    }

    /**
     * Lista todos los accesos registrados a un usuario (para auditoría).
     */
    public List<AccesoLog> listarPorUsuario(String usuarioId) {
        return accesoLogRepository.listarPorUsuario(usuarioId);
    }

    /**
     * Lista todos los accesos realizados por un profesional.
     */
    public List<AccesoLog> listarPorProfesional(String profesionalId) {
        return accesoLogRepository.listarPorProfesional(profesionalId);
    }


    public List<AccesoLogDTO> listarDTOporUsuario(String usuarioId) {
        List<AccesoLog> logs = accesoLogRepository.listarPorUsuario(usuarioId);

        return logs.stream().map(log -> {

            var profesional = profesionalDeSaludRepository.buscarPorId(log.getUsuarioSolicitante());

            String nombre = profesional != null ? profesional.getNombres() : "Desconocido";
            String apellido = profesional != null ? profesional.getApellidos() : "";
            String centro = profesional != null && profesional.getCentroDeSalud() != null
                    ? profesional.getCentroDeSalud().getNombre()
                    : "No asignado";

            return new AccesoLogDTO(
                    log.getUsuarioSolicitante(),
                    nombre,
                    apellido,
                    centro,
                    log.getFechaAcceso(),
                    log.getResultado(),
                    log.getMotivo()
            );
        }).toList();
    }


}
