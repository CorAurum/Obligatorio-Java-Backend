package Repository;

import Entity.Notificacion;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class NotificacionRepository {

    @PersistenceContext(unitName = "defaultPU")
    private EntityManager em;

    public void save(Notificacion notification) {
        em.persist(notification);
    }

    public List<Notificacion> findAll(String userId) {
        return em.createQuery(
                "SELECT n FROM Notificacion n WHERE n.userId = :uid ORDER BY n.fechaCreacion DESC",
                Notificacion.class
        ).setParameter("uid", userId).getResultList();
    }

    public List<Notificacion> findUnread(String userId) {
        return em.createQuery(
                "SELECT n FROM Notificacion n WHERE n.userId = :uid AND n.leida = false ORDER BY n.fechaCreacion DESC",
                Notificacion.class
        ).setParameter("uid", userId).getResultList();
    }

    public void markAllAsRead(String userId) {
        em.createQuery("UPDATE Notificacion n SET n.leida = true WHERE n.userId = :uid AND n.leida = false")
                .setParameter("uid", userId)
                .executeUpdate();
    }
}
