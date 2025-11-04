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

    // 🔹 Crear una especialidad
    public void crear(Especialidad especialidad) {
        em.persist(especialidad);
    }

    // 🔹 Buscar por ID
    public Especialidad buscarPorId(String id) {
        return em.find(Especialidad.class, id);
    }

    // 🔹 Buscar por nombre (case-insensitive)
    public Especialidad buscarPorNombre(String nombre) {
        List<Especialidad> result = em.createQuery(
                        "SELECT e FROM Especialidad e WHERE LOWER(e.nombre) = LOWER(:n)",
                        Especialidad.class
                ).setParameter("n", nombre)
                .getResultList();

        return result.isEmpty() ? null : result.get(0);
    }

    // 🔹 Listar todas las especialidades
    public List<Especialidad> listarTodos() {
        return em.createQuery("SELECT e FROM Especialidad e", Especialidad.class)
                .getResultList();
    }

    // 🔹 Actualizar una especialidad existente
    public Especialidad actualizar(Especialidad especialidad) {
        return em.merge(especialidad);
    }

    // 🔹 Eliminar una especialidad
    public void eliminar(Especialidad especialidad) {
        Especialidad ref = em.contains(especialidad)
                ? especialidad
                : em.merge(especialidad);
        em.remove(ref);
    }
}
