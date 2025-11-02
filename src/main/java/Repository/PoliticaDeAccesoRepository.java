package Repository;

import Entity.PoliticaDeAcceso;
import Entity.Usuarios.Usuario;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Stateless
public class PoliticaDeAccesoRepository {

    @PersistenceContext(unitName = "defaultPU")
    private EntityManager em;

    public void crear(PoliticaDeAcceso politica) {
        em.persist(politica);
    }

    public void actualizar(PoliticaDeAcceso politica) {
        em.merge(politica);
    }

    public PoliticaDeAcceso buscarPorId(String id) {
        return em.find(PoliticaDeAcceso.class, id);
    }

    // 🆕 Metodo para listar todas las políticas de un usuario específico
    public List<PoliticaDeAcceso> listarPorUsuario(String usuarioId) {
        return em.createQuery(
                        "SELECT p FROM PoliticaDeAcceso p " +
                                "LEFT JOIN FETCH p.centroDeSalud " +
                                "LEFT JOIN FETCH p.especialidades " +
                                "WHERE p.usuario.id = :usuarioId", PoliticaDeAcceso.class)
                .setParameter("usuarioId", usuarioId)
                .getResultList();
    }

    public List<PoliticaDeAcceso> buscarPorUsuario(String usuarioId) {
        return em.createQuery(
                        "SELECT p FROM PoliticaDeAcceso p WHERE p.usuario.id = :uid",
                        PoliticaDeAcceso.class)
                .setParameter("uid", usuarioId)
                .getResultList();
    }

    public List<PoliticaDeAcceso> buscarActivasPorUsuario(String usuarioId) {
        return em.createQuery(
                        "SELECT p FROM PoliticaDeAcceso p WHERE p.usuario.id = :uid " +
                                "AND p.estado = 'ACTIVA' AND (p.vigenciaHasta IS NULL OR p.vigenciaHasta >= :hoy)",
                        PoliticaDeAcceso.class)
                .setParameter("uid", usuarioId)
                .setParameter("hoy", LocalDate.now())
                .getResultList();
    }

    public boolean existeActiva(String usuarioId, String centroId, String especialidadId) {
        Long count = em.createQuery(
                        "SELECT COUNT(p) FROM PoliticaDeAcceso p JOIN p.especialidades e " +
                                "WHERE p.usuario.id = :uid AND p.centroDeSalud.id = :cid " +
                                "AND e.id = :eid AND p.estado = 'ACTIVA' " +
                                "AND (p.vigenciaHasta IS NULL OR p.vigenciaHasta >= :hoy)",
                        Long.class)
                .setParameter("uid", usuarioId)
                .setParameter("cid", centroId)
                .setParameter("eid", especialidadId)
                .setParameter("hoy", LocalDate.now())
                .getSingleResult();
        return count > 0;
    }
}
