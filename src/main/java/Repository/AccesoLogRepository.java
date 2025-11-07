package Repository;

import Entity.AccesoLog;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@ApplicationScoped
public class AccesoLogRepository {

    @PersistenceContext
    private EntityManager em;

    public void crear(AccesoLog log) {
        em.persist(log);
    }

    public List<AccesoLog> listarPorUsuario(String usuarioId) {
        return em.createQuery(
                        "SELECT a FROM AccesoLog a WHERE a.usuario.id = :usuarioId ORDER BY a.fechaAcceso DESC",
                        AccesoLog.class)
                .setParameter("usuarioId", usuarioId)
                .getResultList();
    }

    public List<AccesoLog> listarPorProfesional(String profesionalId) {
        return em.createQuery(
                        "SELECT a FROM AccesoLog a WHERE a.usuarioSolicitante = :profesionalId ORDER BY a.fechaAcceso DESC",
                        AccesoLog.class)
                .setParameter("profesionalId", profesionalId)
                .getResultList();
    }
}
