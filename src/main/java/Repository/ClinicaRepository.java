package Repository;

import Class.centroSalud;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ClinicaRepository {

    @PersistenceContext
    private EntityManager em;

    public void guardar(centroSalud centroSalud) {
        em.persist(centroSalud);
    }

    public centroSalud buscarPorId(Long id) {
        return em.find(centroSalud.class, id);
    }

    public List<centroSalud> listar() {
        return em.createQuery("SELECT c FROM centroSalud c", centroSalud.class).getResultList();
    }

    public void eliminar(Long id) {
        centroSalud centroSalud = buscarPorId(id);
        if (centroSalud != null) {
            em.remove(centroSalud);
        }
    }

    public centroSalud actualizar(centroSalud centroSalud) {
        return em.merge(centroSalud);
    }
}
