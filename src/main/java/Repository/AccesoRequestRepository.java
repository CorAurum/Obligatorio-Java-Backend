package Repository;

import Entity.AccesoRequest;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class AccesoRequestRepository {

    @PersistenceContext(unitName = "defaultPU")
    private EntityManager em;

    // 🔹 Crear nueva solicitud
    public void crear(AccesoRequest req) {
        em.persist(req);
    }

    // 🔹 Buscar por ID
    public AccesoRequest buscarPorId(String id) {
        return em.find(AccesoRequest.class, id);
    }

    // 🔹 Actualizar una solicitud existente
    public void actualizar(AccesoRequest req) {
        em.merge(req);
    }

    // 🔹 Listar todas las solicitudes
    public List<AccesoRequest> listarTodas() {
        return em.createQuery("SELECT a FROM AccesoRequest a ORDER BY a.fechaSolicitud DESC", AccesoRequest.class)
                .getResultList();
    }

    // 🔹 Listar solo las solicitudes pendientes
    public List<AccesoRequest> listarPendientes() {
        return em.createQuery(
                        "SELECT a FROM AccesoRequest a WHERE a.estado = :estado ORDER BY a.fechaSolicitud DESC",
                        AccesoRequest.class)
                .setParameter("estado", AccesoRequest.EstadoRequest.PENDIENTE)
                .getResultList();
    }

    // 🔹 Listar solicitudes por profesional
    public List<AccesoRequest> listarPorProfesional(String profesionalId) {
        return em.createQuery(
                        "SELECT a FROM AccesoRequest a WHERE a.profesionalSolicitante.id = :profId ORDER BY a.fechaSolicitud DESC",
                        AccesoRequest.class)
                .setParameter("profId", profesionalId)
                .getResultList();
    }

    // 🔹 Listar solicitudes por usuario (paciente)
    public List<AccesoRequest> listarPorUsuario(String usuarioId) {
        return em.createQuery(
                        "SELECT a FROM AccesoRequest a WHERE a.usuario.id = :usuarioId ORDER BY a.fechaSolicitud DESC",
                        AccesoRequest.class)
                .setParameter("usuarioId", usuarioId)
                .getResultList();
    }

    // 🔹 Listar solicitudes pendientes para un usuario (por aprobar)
    public List<AccesoRequest> listarPendientesPorUsuario(String usuarioId) {
        return em.createQuery(
                        "SELECT a FROM AccesoRequest a " +
                                "WHERE a.usuario.id = :usuarioId " +
                                "AND a.estado = :estado " +
                                "ORDER BY a.fechaSolicitud DESC",
                        AccesoRequest.class)
                .setParameter("usuarioId", usuarioId)
                .setParameter("estado", AccesoRequest.EstadoRequest.PENDIENTE)
                .getResultList();
    }
}
