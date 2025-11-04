package Repository;

import Entity.CentroDeSalud;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class CentroDeSaludRepository {

    @PersistenceContext(unitName = "defaultPU")
    private EntityManager em;

    public void crear(CentroDeSalud centro) {
        em.persist(centro);
    }

    public CentroDeSalud buscarPorId(String id) {
        return em.find(CentroDeSalud.class, id);
    }

    public List<CentroDeSalud> listarTodos() {
        return em.createQuery("SELECT c FROM CentroDeSalud c", CentroDeSalud.class).getResultList();
    }

    public void actualizar(CentroDeSalud centro) {
        em.merge(centro);
    }

    public void eliminar(String id) {
        CentroDeSalud c = em.find(CentroDeSalud.class, id);
        if (c != null) em.remove(c);
    }
}
