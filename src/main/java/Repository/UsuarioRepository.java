package Repository;


import Entity.Usuarios.Usuario;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class UsuarioRepository {

    @PersistenceContext(unitName = "defaultPU")
    private EntityManager em;

    public void crear(Usuario usuario) {
        em.persist(usuario);
    }

    public Usuario buscarPorId(String id) {
        return em.find(Usuario.class, id);
    }

    public Usuario buscarPorEmail(String email) {
        List<Usuario> res = em.createQuery(
                        "SELECT u FROM Usuario u WHERE u.emailPrincipal = :email", Usuario.class)
                .setParameter("email", email)
                .getResultList();
        return res.isEmpty() ? null : res.get(0);
    }

    public List<Usuario> listarTodos() {
        return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
    }
}
