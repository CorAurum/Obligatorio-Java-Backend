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

    public void guardar(DocumentoClinico documento) {
        em.persist(documento);
    }

    public DocumentoClinico actualizar(DocumentoClinico documento) {
        return em.merge(documento);
    }

    public DocumentoClinico buscarPorId(Long idDocumento) {
        return em.find(DocumentoClinico.class, idDocumento);
    }

    public List<DocumentoClinico> listarTodos() {
        return em.createQuery("SELECT d FROM DocumentoClinico d", DocumentoClinico.class)
                .getResultList();
    }

    public void eliminar(Long idDocumento) {
        DocumentoClinico documento = em.find(DocumentoClinico.class, idDocumento);
        if (documento != null) {
            em.remove(documento);
        }
    }
}
