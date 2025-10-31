package Repository;

import Entity.CentroDeSalud;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ClinicaRepository {

    @PersistenceContext
    private EntityManager em;

    public void guardar(CentroDeSalud centroSalud) {
        em.persist(centroSalud);
    }

    public CentroDeSalud buscarPorId(Long id) {
        return em.find(CentroDeSalud.class, id);
    }

    public List<CentroDeSalud> listar() {
        return em.createQuery("SELECT c FROM CentroDeSalud c", CentroDeSalud.class).getResultList();
    }

    public void eliminar(Long id) {
        CentroDeSalud centroSalud = buscarPorId(id);
        if (centroSalud != null) {
            em.remove(centroSalud);
        }
    }

    public CentroDeSalud actualizar(CentroDeSalud centroSalud) {
        return em.merge(centroSalud);
    }
}
