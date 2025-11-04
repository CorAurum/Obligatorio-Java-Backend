package Repository;

import Entity.DocumentoClinico;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class DocumentoClinicoRepository {

    @PersistenceContext(unitName = "defaultPU")
    private EntityManager em;

    public void crear(DocumentoClinico doc) {
        em.persist(doc);
    }

    public DocumentoClinico buscarPorId(String id) {
        return em.find(DocumentoClinico.class, id);
    }

    public DocumentoClinico buscarPorIdOrigenYCentro(String idOrigen, String centroId) {
        List<DocumentoClinico> res = em.createQuery(
                        "SELECT d FROM DocumentoClinico d WHERE d.idOrigen = :idOrigen AND d.centroDeSalud.id = :centroId",
                        DocumentoClinico.class)
                .setParameter("idOrigen", idOrigen)
                .setParameter("centroId", centroId)
                .getResultList();
        return res.isEmpty() ? null : res.get(0);
    }

    public List<DocumentoClinico> listarPorUsuario(String usuarioId) {
        return em.createQuery(
                        "SELECT d FROM DocumentoClinico d WHERE d.usuario.id = :usuarioId ORDER BY d.fechaCreacion DESC",
                        DocumentoClinico.class)
                .setParameter("usuarioId", usuarioId)
                .getResultList();
    }

    public List<DocumentoClinico> listarTodos() {
        return em.createQuery("SELECT d FROM DocumentoClinico d", DocumentoClinico.class)
                .getResultList();
    }
}
