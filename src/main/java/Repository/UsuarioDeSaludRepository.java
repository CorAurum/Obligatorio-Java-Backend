package Repository;

import Class.Usuarios.usuarioDeSalud;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class UsuarioDeSaludRepository {

    @PersistenceContext(unitName = "defaultPU")
    private EntityManager em;

    public void guardar(usuarioDeSalud usuario) {
        em.persist(usuario);
    }

    public usuarioDeSalud actualizar(usuarioDeSalud usuario) {
        return em.merge(usuario);
    }

    public usuarioDeSalud buscarPorCedula(String cedula) {
        return em.find(usuarioDeSalud.class, cedula);
    }

    public List<usuarioDeSalud> listarTodos() {
        return em.createQuery("SELECT u FROM usuarioDeSalud u", usuarioDeSalud.class).getResultList();
    }

    public void eliminar(String cedula) {
        usuarioDeSalud usuario = em.find(usuarioDeSalud.class, cedula);
        if (usuario != null) {
            em.remove(usuario);
        }
    }
}
