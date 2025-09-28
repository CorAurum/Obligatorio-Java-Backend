package Service;

import Class.UsuarioDeSalud;
import Repository.UsuarioDeSaludRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;

@Stateless
public class UsuarioDeSaludService {

    @Inject
    private UsuarioDeSaludRepository repository;

    public void crearUsuario(UsuarioDeSalud usuario) {
        repository.guardar(usuario);
    }

    public UsuarioDeSalud obtenerPorCedula(String cedula) {
        return repository.buscarPorCedula(cedula);
    }

    public List<UsuarioDeSalud> listarUsuarios() {
        return repository.listarTodos();
    }

    public UsuarioDeSalud actualizarUsuario(String cedula, UsuarioDeSalud usuarioNuevo) {
        UsuarioDeSalud existente = repository.buscarPorCedula(cedula);
        if (existente != null) {
            existente.setNombre(usuarioNuevo.getNombre());
            existente.setApellido(usuarioNuevo.getApellido());
            existente.setEmail(usuarioNuevo.getEmail());
            existente.setTelefono(usuarioNuevo.getTelefono());
            existente.setFechaNacimiento(usuarioNuevo.getFechaNacimiento());
            existente.setFechaRegistro(usuarioNuevo.getFechaRegistro());
            // etc...
            return repository.actualizar(existente);
        } else {
            usuarioNuevo.setCedulaIdentidad(cedula);
            repository.guardar(usuarioNuevo);
            return usuarioNuevo;
        }
    }

    public void eliminarUsuario(String cedula) {
        repository.eliminar(cedula);
    }
}
