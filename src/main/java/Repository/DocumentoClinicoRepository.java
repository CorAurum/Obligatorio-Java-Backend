package Repository;

import Class.documentoClinico;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class DocumentoClinicoRepository {

    @PersistenceContext(unitName = "defaultPU")
    private EntityManager em;

    public void guardar(documentoClinico documento) {
        em.persist(documento);
    }

    public documentoClinico actualizar(documentoClinico documento) {
        return em.merge(documento);
    }

    public documentoClinico buscarPorId(Long idDocumento) {
        return em.find(documentoClinico.class, idDocumento);
    }

    public List<documentoClinico> listarTodos() {
        return em.createQuery("SELECT d FROM documentoClinico d", documentoClinico.class)
                .getResultList();
    }

    public void eliminar(Long idDocumento) {
        documentoClinico documento = em.find(documentoClinico.class, idDocumento);
        if (documento != null) {
            em.remove(documento);
        }
    }
}
