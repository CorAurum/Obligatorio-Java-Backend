package Repository;

import Entity.Especialidad;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class EspecialidadRepository {

    @PersistenceContext(unitName = "defaultPU")
    private EntityManager em;

    public void crear(Especialidad especialidad) {
        em.persist(especialidad);
    }

    public Especialidad buscarPorId(String id) {
        return em.find(Especialidad.class, id);
    }

    public Especialidad buscarPorNombre(String nombre) {
        List<Especialidad> result = em.createQuery(
                "SELECT e FROM Especialidad e WHERE LOWER(e.nombre) = LOWER(:n)",
                Especialidad.class
        ).setParameter("n", nombre).getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    public List<Especialidad> listarTodas() {
        return em.createQuery("SELECT e FROM Especialidad e", Especialidad.class).getResultList();
    }
}
