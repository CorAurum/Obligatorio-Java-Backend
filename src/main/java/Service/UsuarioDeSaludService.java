package Service;

import Class.Usuarios.usuarioDeSalud;
import Repository.UsuarioDeSaludRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;

@Stateless
public class UsuarioDeSaludService {

    @Inject
    private UsuarioDeSaludRepository repository;

    public void crearUsuario(usuarioDeSalud usuario) {
        repository.guardar(usuario);
    }

    public usuarioDeSalud obtenerPorCedula(String cedula) {
        return repository.buscarPorCedula(cedula);
    }

    public List<usuarioDeSalud> listarUsuarios() {
        return repository.listarTodos();
    }

    public usuarioDeSalud actualizarUsuario(String cedula, usuarioDeSalud usuarioNuevo) {
        usuarioDeSalud existente = repository.buscarPorCedula(cedula);
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
            usuarioNuevo.setCi(cedula);
            repository.guardar(usuarioNuevo);
            return usuarioNuevo;
        }
    }

    public void eliminarUsuario(String cedula) {
        repository.eliminar(cedula);
    }
}
