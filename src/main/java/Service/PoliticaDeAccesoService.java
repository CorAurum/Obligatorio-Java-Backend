package Service;

import Entity.CentroDeSalud;
import Entity.Especialidad;
import Entity.PoliticaDeAcceso;
import Entity.Usuarios.Usuario;
import Repository.CentroDeSaludRepository;
import Repository.EspecialidadRepository;
import Repository.PoliticaDeAccesoRepository;
import Repository.UsuarioRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Stateless
@Transactional
public class PoliticaDeAccesoService {

    @Inject private PoliticaDeAccesoRepository politicaRepository;
    @Inject private UsuarioRepository usuarioRepository;
    @Inject private CentroDeSaludRepository centroRepository;
    @Inject private EspecialidadRepository especialidadRepository;

    // Crear nueva política de acceso
    public PoliticaDeAcceso crearPolitica(String usuarioId, String centroId, List<String> especialidadesIds, LocalDate vigenciaHasta) {
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId);
        if (usuario == null) throw new IllegalArgumentException("Usuario no encontrado");

        CentroDeSalud centro = centroRepository.buscarPorId(centroId);
        if (centro == null) throw new IllegalArgumentException("Centro de salud no encontrado");

        PoliticaDeAcceso politica = new PoliticaDeAcceso();
        politica.setId(UUID.randomUUID().toString());
        politica.setUsuario(usuario);
        politica.setCentroDeSalud(centro);
        politica.setFechaCreacion(LocalDateTime.now());
        politica.setVigenciaHasta(vigenciaHasta);
        politica.setEstado(PoliticaDeAcceso.EstadoPolitica.ACTIVA);

        // Agregar especialidades
        for (String espId : especialidadesIds) {
            Especialidad esp = especialidadRepository.buscarPorId(espId);
            if (esp != null) politica.getEspecialidades().add(esp);
        }

        politicaRepository.crear(politica);
        return politica;
    }

    // Revocar una política
    public void revocarPolitica(String politicaId) {
        PoliticaDeAcceso p = politicaRepository.buscarPorId(politicaId);
        if (p == null) throw new IllegalArgumentException("Política no encontrada");
        p.setEstado(PoliticaDeAcceso.EstadoPolitica.REVOCADA);
        politicaRepository.actualizar(p);
    }

    // Listar políticas por usuario
    public List<PoliticaDeAcceso> listarPoliticas(String usuarioId) {
        return politicaRepository.buscarPorUsuario(usuarioId);
    }

    // Verificar acceso de un profesional
    public boolean verificarAcceso(String usuarioId, String centroId, String especialidadId) {
        return politicaRepository.existeActiva(usuarioId, centroId, especialidadId);
    }
}
