package Repository;


import Entity.Usuarios.IdentificadorUsuario;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class IdentificadorUsuarioRepository {

    @PersistenceContext(unitName = "defaultPU")
    private EntityManager em;

    public void crear(IdentificadorUsuario idu) {
        em.persist(idu);
    }

    public IdentificadorUsuario buscarPorValor(String valor) {
        List<IdentificadorUsuario> res = em.createQuery(
                        "SELECT i FROM IdentificadorUsuario i WHERE i.valor = :v", IdentificadorUsuario.class)
                .setParameter("v", valor)
                .getResultList();
        return res.isEmpty() ? null : res.get(0);
    }
}
