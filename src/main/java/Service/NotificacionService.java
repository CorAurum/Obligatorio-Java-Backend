package Service;

import Entity.Notificacion;
import Entity.Usuarios.Usuario;
import Repository.NotificacionRepository;
import Repository.UsuarioRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@Stateless
@Transactional
public class NotificacionService {

    @Inject
    NotificacionRepository repo;
    @Inject
    UsuarioRepository usuarioRepository;
    @Inject
    FirebaseService firebaseService;

    public List<Notificacion> obtenerTodas(String userId) {
        return repo.findAll(userId);
    }

    public List<Notificacion> obtenerNoLeidas(String userId) {
        return repo.findUnread(userId);
    }

    public void marcarComoLeidas(String userId) {
        repo.markAllAsRead(userId);
    }

    public Notificacion crear(String userId, String titulo, String mensaje) {
        Notificacion n = new Notificacion();
        n.setUserId(userId);
        n.setTitulo(titulo);
        n.setMensaje(mensaje);
        repo.save(n);

        Usuario u = usuarioRepository.buscarPorId(userId);
        if (u != null && u.getFirebaseToken() != null) {
            firebaseService.enviarNotificacion(
                    u.getFirebaseToken(),
                    titulo,
                    mensaje
            );
        }


        return n;



    }

}
