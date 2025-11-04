package Service;

import Entity.CentroDeSalud;
import Entity.Usuarios.Administrador;
import Repository.AdministradorRepository;
import Repository.CentroDeSaludRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Stateless
@Transactional
public class CentroDeSaludService {

    @Inject
    private CentroDeSaludRepository centroDeSaludRepository;

    @Inject
    private AdministradorRepository administradorRepository;

    public CentroDeSalud crearCentro(CentroDeSalud centro, Long adminId) {
        Administrador admin = administradorRepository.buscarPorId(adminId);
        if (admin == null)
            throw new IllegalArgumentException("Administrador no encontrado");

        if (centro.getId() == null)
            centro.setId(UUID.randomUUID().toString());

        centro.setCreadoPor(admin);
        centro.setEstado(CentroDeSalud.EstadoCentro.HABILITADO);
        centroDeSaludRepository.crear(centro);
        return centro;
    }

    public List<CentroDeSalud> listarCentros() {
        return centroDeSaludRepository.listarTodos();
    }

    public CentroDeSalud obtenerPorId(String id) {
        return centroDeSaludRepository.buscarPorId(id);
    }
}

