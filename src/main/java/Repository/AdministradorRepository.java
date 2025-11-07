package Repository;

import Entity.Usuarios.Administrador;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class AdministradorRepository {

    @PersistenceContext(unitName = "defaultPU")
    private EntityManager em;

    public void crear(Administrador admin) {
        em.persist(admin);
    }

    public Administrador buscarPorId(Long id) {
        return em.find(Administrador.class, id);
    }

    public List<Administrador> listarTodos() {
        return em.createQuery("SELECT a FROM Administrador a", Administrador.class).getResultList();
    }

    public void actualizar(Administrador admin) {
        em.merge(admin);
    }

    public void eliminar(Long id) {
        Administrador a = em.find(Administrador.class, id);
        if (a != null) em.remove(a);
    }

    public Administrador buscarPorCedula(String cedula) {
        List<Administrador> resultado = em.createQuery(
                "SELECT a FROM Administrador a WHERE a.cedula = :cedula AND a.activo = true",
                Administrador.class)
            .setParameter("cedula", cedula)
            .getResultList();
        return resultado.isEmpty() ? null : resultado.get(0);
    }
}