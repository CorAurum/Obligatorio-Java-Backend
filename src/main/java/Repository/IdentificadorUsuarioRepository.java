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

    /**
     * Busca un identificador por tipo y valor, sin distinguir mayúsculas.
     * Esto es útil para garantizar unicidad lógica (CI, pasaporte, etc.).
     */
    public IdentificadorUsuario buscarPorTipoYValor(String tipo, String valor) {
        List<IdentificadorUsuario> res = em.createQuery(
                        "SELECT i FROM IdentificadorUsuario i " +
                                "WHERE LOWER(i.tipo) = LOWER(:t) AND i.valor = :v",
                        IdentificadorUsuario.class)
                .setParameter("t", tipo)
                .setParameter("v", valor)
                .getResultList();
        return res.isEmpty() ? null : res.get(0);
    }


    public List<IdentificadorUsuario> buscarTodosPorTipoYValor(String tipo, String valor) {
        return em.createQuery(
                        "SELECT i FROM IdentificadorUsuario i WHERE i.tipo = :tipo AND i.valor = :valor",
                        IdentificadorUsuario.class)
                .setParameter("tipo", tipo)
                .setParameter("valor", valor)
                .getResultList();
    }


}

