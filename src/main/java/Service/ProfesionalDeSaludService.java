package Service;

import Entity.CentroDeSalud;
import Entity.DTO.ProfesionalCentralDTO;
import Entity.Especialidad;
import Entity.Usuarios.Administrador;
import Entity.Usuarios.ProfesionalDeSalud;
import Repository.AdministradorRepository;
import Repository.CentroDeSaludRepository;
import Repository.EspecialidadRepository;
import Repository.ProfesionalDeSaludRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Stateless
@Transactional
public class ProfesionalDeSaludService {

    @Inject
    private ProfesionalDeSaludRepository profesionalDeSaludRepository;

    @Inject
    private CentroDeSaludRepository centroDeSaludRepository;

    @Inject
    private AdministradorRepository administradorRepository;

    @Inject
    private EspecialidadRepository especialidadRepository;

//    public ProfesionalDeSalud registrarProfesional(ProfesionalDeSalud p, String centroId) {
//        CentroDeSalud centro = centroDeSaludRepository.buscarPorId(centroId);
//      //  Administrador admin = administradorRepository.buscarPorId(adminId);
//        if (centro == null)
//            throw new IllegalArgumentException("Centro de salud no encontrado");
//
//        if (p.getId() == null)
//            p.setId(UUID.randomUUID().toString());
//
//        p.setCentroDeSalud(centro);
//        p.setFechaRegistroProfesional(LocalDate.now());
//        p.setEstado(ProfesionalDeSalud.EstadoProfesional.ACTIVO);
//      //  p.setHabilitadoPor(admin);
//
//        profesionalDeSaludRepository.crear(p);
//        return p;
//    }

    public ProfesionalDeSalud registrarProfesionalDesdePeriferico(ProfesionalCentralDTO dto, String centroId) {
        if (centroId == null || centroId.isBlank()) {
            throw new IllegalArgumentException("centroId es obligatorio");
        }

        CentroDeSalud centro = centroDeSaludRepository.buscarPorId(centroId);
        if (centro == null) {
            throw new IllegalArgumentException("Centro de salud no encontrado con id: " + centroId);
        }

        ProfesionalDeSalud p = new ProfesionalDeSalud();

        // Si el periférico mando id, lo usamos; si no, generamos uno nuevo
        if (dto.getId() == null || dto.getId().isBlank()) {
            p.setId(UUID.randomUUID().toString());
        } else {
            p.setId(dto.getId());
        }

        p.setNumeroRegistro(dto.getNumeroRegistro()); // "Lo borramos?"
        p.setNombres(dto.getNombres());
        p.setApellidos(dto.getApellidos());
        p.setEmail(dto.getEmail());
        p.setTelefono(dto.getTelefono());
        p.setFechaRegistroProfesional(LocalDate.now());
        p.setEstado(ProfesionalDeSalud.EstadoProfesional.ACTIVO);
        p.setCentroDeSalud(centro);

        // Asociar especialidades por IDs (se asume que ya existen en central con mismo id)
        List<Especialidad> especialidades = new ArrayList<>();
        if (dto.getEspecialidadesIds() != null) {
            for (String espId : dto.getEspecialidadesIds()) {
                Especialidad esp = especialidadRepository.buscarPorId(espId);
                if (esp == null) {
                    throw new IllegalArgumentException("Especialidad no encontrada en central con id: " + espId);
                }
                especialidades.add(esp);
            }
        }
        p.setEspecialidades(especialidades);

        profesionalDeSaludRepository.crear(p);
        return p;
    }

    public java.util.List<ProfesionalDeSalud> listarProfesionales() {
        return profesionalDeSaludRepository.listarTodos();
    }

    public ProfesionalDeSalud obtenerPorId(String id) {
        return profesionalDeSaludRepository.buscarPorId(id);
    }

    public ProfesionalDeSalud actualizarProfesional(String id, ProfesionalDeSalud profesional) {
        ProfesionalDeSalud existente = profesionalDeSaludRepository.buscarPorId(id);
        if (existente == null) {
            throw new IllegalArgumentException("Profesional no encontrado");
        }

        if (profesional.getNombres() != null) existente.setNombres(profesional.getNombres());
        if (profesional.getApellidos() != null) existente.setApellidos(profesional.getApellidos());
        if (profesional.getEmail() != null) existente.setEmail(profesional.getEmail());
        if (profesional.getTelefono() != null) existente.setTelefono(profesional.getTelefono());
        if (profesional.getNumeroRegistro() != null) existente.setNumeroRegistro(profesional.getNumeroRegistro());
        if (profesional.getEstado() != null) existente.setEstado(profesional.getEstado());

        return profesionalDeSaludRepository.actualizar(existente);
    }
}
