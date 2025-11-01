package Repository;

import Entity.Usuarios.UsuarioLocal;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class UsuarioLocalRepository {

    @PersistenceContext(unitName = "defaultPU")
    private EntityManager em;

    public void crear(UsuarioLocal ul) {
        em.persist(ul);
    }

    public UsuarioLocal buscarPorIdLocal(String idLocal) {
        return em.find(UsuarioLocal.class, idLocal);
    }
}
