package Repository;


import Entity.Usuarios.ProfesionalDeSalud;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ProfesionalDeSaludRepository {

    @PersistenceContext(unitName = "defaultPU")
    private EntityManager em;

    public void crear(ProfesionalDeSalud profesional) {
        em.persist(profesional);
    }

    public ProfesionalDeSalud buscarPorId(String id) {
        return em.find(ProfesionalDeSalud.class, id);
    }

    public ProfesionalDeSalud buscarPorNumeroRegistro(String numeroRegistro) {
        List<ProfesionalDeSalud> result = em.createQuery(
                "SELECT p FROM ProfesionalDeSalud p WHERE p.numeroRegistro = :nr",
                ProfesionalDeSalud.class
        ).setParameter("nr", numeroRegistro).getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    public List<ProfesionalDeSalud> listarTodos() {
        return em.createQuery("SELECT p FROM ProfesionalDeSalud p", ProfesionalDeSalud.class).getResultList();
    }

    public ProfesionalDeSalud actualizar(ProfesionalDeSalud profesional) {
        return em.merge(profesional);
    }

    public void eliminar(String id) {
        ProfesionalDeSalud p = em.find(ProfesionalDeSalud.class, id);
        if (p != null) em.remove(p);
    }
}
