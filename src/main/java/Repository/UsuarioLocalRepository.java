package Repository;

import Entity.Usuarios.UsuarioLocal;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class UsuarioLocalRepository {

    @PersistenceContext(unitName = "defaultPU")
    private EntityManager em;

    public void crear(UsuarioLocal ul) {
        em.persist(ul);
    }

    public UsuarioLocal buscarPorCentroYIdLocal(String centroId, String idLocal) {
        List<UsuarioLocal> res = em.createQuery(
                        "SELECT u FROM UsuarioLocal u WHERE u.centroDeSalud.id = :c AND u.idLocal = :id",
                        UsuarioLocal.class)
                .setParameter("c", centroId)
                .setParameter("id", idLocal)
                .getResultList();
        return res.isEmpty() ? null : res.get(0);
    }

    public List<UsuarioLocal> listarPorCentro(String centroId) {
        return em.createQuery(
                        "SELECT u FROM UsuarioLocal u WHERE u.centroDeSalud.id = :c",
                        UsuarioLocal.class)
                .setParameter("c", centroId)
                .getResultList();
    }

    /**
     * Actualiza los datos de un usuario local ya existente.
     */
    public UsuarioLocal actualizar(UsuarioLocal ul) {
        return em.merge(ul);
    }
}
