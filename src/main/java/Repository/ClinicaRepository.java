package Repository;

import Class.Clinica;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ClinicaRepository {

    @PersistenceContext
    private EntityManager em;

    public void guardar(Clinica clinica) {
        em.persist(clinica);
    }

    public Clinica buscarPorId(Long id) {
        return em.find(Clinica.class, id);
    }

    public List<Clinica> listar() {
        return em.createQuery("SELECT c FROM Clinica c", Clinica.class).getResultList();
    }

    public void eliminar(Long id) {
        Clinica clinica = buscarPorId(id);
        if (clinica != null) {
            em.remove(clinica);
        }
    }

    public Clinica actualizar(Clinica clinica) {
        return em.merge(clinica);
    }
}
