package Service;

import Entity.CentroDeSalud;
import Entity.Usuarios.ProfesionalDeSalud;
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

    public ProfesionalDeSalud registrarProfesional(ProfesionalDeSalud p, String centroId) {
        CentroDeSalud centro = centroDeSaludRepository.buscarPorId(centroId);
        if (centro == null)
            throw new IllegalArgumentException("Centro de salud no encontrado");

        if (p.getId() == null)
            p.setId(UUID.randomUUID().toString());

        p.setCentroDeSalud(centro);
        p.setFechaRegistroProfesional(LocalDate.now());
        p.setEstado(ProfesionalDeSalud.EstadoProfesional.ACTIVO);

        profesionalDeSaludRepository.crear(p);
        return p;
    }
}
