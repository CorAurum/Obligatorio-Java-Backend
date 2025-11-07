package Service;

import Entity.CentroDeSalud;
import Entity.Usuarios.Administrador;
import Entity.Usuarios.ProfesionalDeSalud;
import Repository.AdministradorRepository;
import Repository.CentroDeSaludRepository;
import Repository.ProfesionalDeSaludRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
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

    public ProfesionalDeSalud registrarProfesional(ProfesionalDeSalud p, String centroId, Long adminId) {
        CentroDeSalud centro = centroDeSaludRepository.buscarPorId(centroId);
        Administrador admin = administradorRepository.buscarPorId(adminId);
        if (centro == null)
            throw new IllegalArgumentException("Centro de salud no encontrado");

        if (p.getId() == null)
            p.setId(UUID.randomUUID().toString());

        p.setCentroDeSalud(centro);
        p.setFechaRegistroProfesional(LocalDate.now());
        p.setEstado(ProfesionalDeSalud.EstadoProfesional.ACTIVO);
        p.setHabilitadoPor(admin);

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
