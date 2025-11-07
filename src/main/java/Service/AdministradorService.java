package Service;


import Entity.Usuarios.Administrador;
import Repository.AdministradorRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Stateless
@Transactional
public class AdministradorService {

    @Inject
    private AdministradorRepository administradorRepository;

    public Administrador crearAdministrador(Administrador admin) {
        // Si el ID es Long y lo genera la BD, no lo tocamos
        if (admin.getId() != null) {
            throw new IllegalArgumentException("El administrador ya tiene un ID asignado");
        }

        admin.setFechaAlta(LocalDateTime.now());
        admin.setActivo(true);
        administradorRepository.crear(admin);
        return admin;
    }

    public Administrador obtenerPorId(Long id) {
        return administradorRepository.buscarPorId(id);
    }

    public List<Administrador> listarAdministradores() {
        return administradorRepository.listarTodos();
    }

    public Administrador actualizarAdministrador(Administrador admin) {
        administradorRepository.actualizar(admin);
        return admin;
    }

    public void eliminarAdministrador(Long id) {
        administradorRepository.eliminar(id);
    }

    public Administrador obtenerPorCedula(String cedula) {
        return administradorRepository.buscarPorCedula(cedula);
    }
}
