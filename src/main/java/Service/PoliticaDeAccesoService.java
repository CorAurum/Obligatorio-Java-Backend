package Service;

import Entity.CentroDeSalud;
import Entity.Especialidad;
import Entity.PoliticaDeAcceso;
import Entity.Usuarios.ProfesionalDeSalud;
import Entity.Usuarios.Usuario;
import Repository.*;
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
    @Inject private ProfesionalDeSaludRepository profesionalDeSaludRepository;

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

    /* VERIFICAR SI ES NECESARIO
    // Verificar acceso de un profesional
    public boolean verificarAcceso(String usuarioId, String centroId, String especialidadId) {
        return politicaRepository.existeActiva(usuarioId, centroId, especialidadId);
    } */


    // METODO PARA EVALUAR SI UN PROFESIONAL DE SALUD PUEDE ACCEDER A LA HISTORIA DE UN PACIENTE X
    // CONTROLA CENTRO DE SALUD AL QUE PERTENECE EL PROFESIONAL Y LAS ESPECIALIDADES QUE EL USUARIO
    // AUTORIZO DENTRO DE DICHO CENTRO.
    public boolean puedeAcceder(String profesionalId, String usuarioId) {
        ProfesionalDeSalud prof = profesionalDeSaludRepository.buscarPorId(profesionalId);
        if (prof == null) throw new IllegalArgumentException("Profesional no encontrado");

        Usuario usuario = usuarioRepository.buscarPorId(usuarioId);
        if (usuario == null) throw new IllegalArgumentException("Usuario no encontrado");

        // Obtener políticas activas del usuario
        List<PoliticaDeAcceso> politicas = politicaRepository.listarPorUsuario(usuarioId);

        for (PoliticaDeAcceso p : politicas) {
            if (p.getEstado() == PoliticaDeAcceso.EstadoPolitica.ACTIVA
                    && p.getCentroDeSalud().getId().equals(prof.getCentroDeSalud().getId())) {

                // Comparar especialidades del profesional con las habilitadas en la política
                for (Especialidad espProf : prof.getEspecialidades()) {
                    for (Especialidad espPol : p.getEspecialidades()) {
                        if (espProf.getId().equals(espPol.getId())) {
                            return true; // ✅ acceso permitido
                        }
                    }
                }
            }
        }

        return false; // 🚫 acceso denegado
    }

}
