//package Repository;
//
//import Entity.Usuarios.UsuarioLocal;
//import jakarta.ejb.Stateless;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import java.util.List;
//
//@Stateless
//public class UsuarioDeSaludRepository {
//
//    @PersistenceContext(unitName = "defaultPU")
//    private EntityManager em;
//
//    public void guardar(UsuarioLocal usuario) {
//        em.persist(usuario);
//    }
//
//    public UsuarioLocal actualizar(UsuarioLocal usuario) {
//        return em.merge(usuario);
//    }
//
//    public UsuarioLocal buscarPorCedula(String cedula) {
//        return em.find(UsuarioLocal.class, cedula);
//    }
//
//    public List<UsuarioLocal> listarTodos() {
//        return em.createQuery("SELECT u FROM UsuarioLocal u", UsuarioLocal.class).getResultList();
//    }
//
//    public void eliminar(String cedula) {
//        UsuarioLocal usuario = em.find(UsuarioLocal.class, cedula);
//        if (usuario != null) {
//            em.remove(usuario);
//        }
//    }
//}
