package Repository;

import Entity.UsuarioAuth;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

@Stateless
public class UsuarioAuthRepository {

    @PersistenceContext(unitName = "defaultPU")
    private EntityManager em;

    public UsuarioAuth save(UsuarioAuth usuario) {
        if (usuario.getId() == null) {
            em.persist(usuario);
            return usuario;
        } else {
            return em.merge(usuario);
        }
    }

    public Optional<UsuarioAuth> findById(Long id) {
        UsuarioAuth usuario = em.find(UsuarioAuth.class, id);
        return Optional.ofNullable(usuario);
    }

    public Optional<UsuarioAuth> findByUsername(String username) {
        try {
            UsuarioAuth usuario = em.createQuery(
                "SELECT u FROM UsuarioAuth u WHERE u.username = :username",
                UsuarioAuth.class)
                .setParameter("username", username)
                .getSingleResult();
            return Optional.of(usuario);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<UsuarioAuth> findByEmail(String email) {
        try {
            UsuarioAuth usuario = em.createQuery(
                "SELECT u FROM UsuarioAuth u WHERE u.email = :email",
                UsuarioAuth.class)
                .setParameter("email", email)
                .getSingleResult();
            return Optional.of(usuario);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public List<UsuarioAuth> findAll() {
        return em.createQuery("SELECT u FROM UsuarioAuth u", UsuarioAuth.class)
                .getResultList();
    }

    public boolean existsByUsername(String username) {
        Long count = em.createQuery(
            "SELECT COUNT(u) FROM UsuarioAuth u WHERE u.username = :username",
            Long.class)
            .setParameter("username", username)
            .getSingleResult();
        return count > 0;
    }

    public boolean existsByEmail(String email) {
        Long count = em.createQuery(
            "SELECT COUNT(u) FROM UsuarioAuth u WHERE u.email = :email",
            Long.class)
            .setParameter("email", email)
            .getSingleResult();
        return count > 0;
    }

    public void delete(UsuarioAuth usuario) {
        if (em.contains(usuario)) {
            em.remove(usuario);
        } else {
            em.remove(em.merge(usuario));
        }
    }
}
