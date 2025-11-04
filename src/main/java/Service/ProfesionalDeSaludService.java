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
}
