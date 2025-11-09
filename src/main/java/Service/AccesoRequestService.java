package Service;

import Entity.AccesoRequest;
import Entity.PoliticaDeAcceso;
import Entity.Usuarios.ProfesionalDeSalud;
import Entity.Usuarios.Usuario;
import Repository.AccesoRequestRepository;
import Repository.PoliticaDeAccesoRepository;
import Repository.ProfesionalDeSaludRepository;
import Repository.UsuarioRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Stateless
public class AccesoRequestService {

    @Inject private AccesoRequestRepository accesoRequestRepository;
    @Inject private ProfesionalDeSaludRepository profesionalRepository;
    @Inject private UsuarioRepository usuarioRepository;
    @Inject private PoliticaDeAccesoRepository politicaRepository;

    public AccesoRequest crearSolicitud(String profesionalId, String usuarioId, String motivo) {
        ProfesionalDeSalud prof = profesionalRepository.buscarPorId(profesionalId);
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId);
        if (prof == null || usuario == null)
            throw new IllegalArgumentException("Profesional o Usuario no encontrado");

        AccesoRequest req = new AccesoRequest();
        req.setId(UUID.randomUUID().toString());
        req.setProfesionalSolicitante(prof);
        req.setUsuario(usuario);
        req.setMotivo(motivo);
        req.setFechaSolicitud(LocalDateTime.now());
        req.setEstado(AccesoRequest.EstadoRequest.PENDIENTE);

        accesoRequestRepository.crear(req);
        return req;
    }

    public void aprobarSolicitud(String solicitudId) {
        AccesoRequest req = accesoRequestRepository.buscarPorId(solicitudId);
        if (req == null) throw new IllegalArgumentException("Solicitud no encontrada");

        req.setEstado(AccesoRequest.EstadoRequest.APROBADO);

        // ✅ Crear política automática
        PoliticaDeAcceso politica = new PoliticaDeAcceso();
        politica.setId(UUID.randomUUID().toString());
        politica.setUsuario(req.getUsuario());
        politica.setCentroDeSalud(req.getProfesionalSolicitante().getCentroDeSalud());
        politica.setFechaCreacion(LocalDateTime.now());
        politica.setVigenciaHasta(LocalDate.now().plusDays(30));
        politica.setEstado(PoliticaDeAcceso.EstadoPolitica.ACTIVA);

        politica.getEspecialidades().addAll(req.getProfesionalSolicitante().getEspecialidades());

        politicaRepository.crear(politica);
    }

    public void rechazarSolicitud(String solicitudId) {
        AccesoRequest req = accesoRequestRepository.buscarPorId(solicitudId);
        if (req == null) throw new IllegalArgumentException("Solicitud no encontrada");
        req.setEstado(AccesoRequest.EstadoRequest.RECHAZADO);
    }

    public List<AccesoRequest> listarPendientes() {
        return accesoRequestRepository.listarPendientes();
    }

    public List<AccesoRequest> listarPendientesPorUsuario(String usuarioId) {
        return accesoRequestRepository.listarPendientesPorUsuario(usuarioId);
    }

}
