package Repository;

import Class.UsuarioDeSalud;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class UsuarioDeSaludRepository {

    @PersistenceContext(unitName = "defaultPU")
    private EntityManager em;

    public void guardar(UsuarioDeSalud usuario) {
        em.persist(usuario);
    }

    public UsuarioDeSalud actualizar(UsuarioDeSalud usuario) {
        return em.merge(usuario);
    }

    public UsuarioDeSalud buscarPorCedula(String cedula) {
        return em.find(UsuarioDeSalud.class, cedula);
    }

    public List<UsuarioDeSalud> listarTodos() {
        return em.createQuery("SELECT u FROM UsuarioDeSalud u", UsuarioDeSalud.class).getResultList();
    }

    public void eliminar(String cedula) {
        UsuarioDeSalud usuario = em.find(UsuarioDeSalud.class, cedula);
        if (usuario != null) {
            em.remove(usuario);
        }
    }
}
