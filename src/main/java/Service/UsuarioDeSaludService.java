//package Service;
//
//import Entity.Usuarios.UsuarioLocal;
//import Repository.UsuarioDeSaludRepository;
//import jakarta.ejb.Stateless;
//import jakarta.inject.Inject;
//import java.util.List;
//
//@Stateless
//public class UsuarioDeSaludService {
//
//    @Inject
//    private UsuarioDeSaludRepository repository;
//
//    public void crearUsuario(UsuarioLocal usuario) {
//        repository.guardar(usuario);
//    }
//
//    public UsuarioLocal obtenerPorCedula(String cedula) {
//        return repository.buscarPorCedula(cedula);
//    }
//
//    public List<UsuarioLocal> listarUsuarios() {
//        return repository.listarTodos();
//    }
//
//    public UsuarioLocal actualizarUsuario(String cedula, UsuarioLocal usuarioNuevo) {
//        UsuarioLocal existente = repository.buscarPorCedula(cedula);
//        if (existente != null) {
//            existente.setNombre(usuarioNuevo.getNombre());
//            existente.setApellido(usuarioNuevo.getApellido());
//            existente.setEmail(usuarioNuevo.getEmail());
//            existente.setTelefono(usuarioNuevo.getTelefono());
//            existente.setFechaNacimiento(usuarioNuevo.getFechaNacimiento());
//            existente.setFechaRegistro(usuarioNuevo.getFechaRegistro());
//            // etc...
//            return repository.actualizar(existente);
//        } else {
//            usuarioNuevo.setCi(cedula);
//            repository.guardar(usuarioNuevo);
//            return usuarioNuevo;
//        }
//    }
//
//    public void eliminarUsuario(String cedula) {
//        repository.eliminar(cedula);
//    }
//}
