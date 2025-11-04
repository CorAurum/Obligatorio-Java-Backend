package Service;

import Entity.UsuarioAuth;
import Repository.UsuarioAuthRepository;
import Util.JwtUtil;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Optional;

/**
 * AuthService - Servicio de autenticación
 *
 * NOTA: Este servicio NO maneja login/registro local ya que la autenticación
 * se realiza externamente con gub.uy. Solo se encarga de validar tokens JWT.
 */
@Stateless
@Transactional
public class AuthService {

    @Inject
    private UsuarioAuthRepository usuarioAuthRepository;

    @Inject
    private JwtUtil jwtUtil;

    /**
     * Validate JWT token and get user
     */
    public Optional<UsuarioAuth> validateToken(String token) {
        try {
            if (!jwtUtil.validateToken(token)) {
                return Optional.empty();
            }

            String username = jwtUtil.extractUsername(token);
            return usuarioAuthRepository.findByUsername(username);
        } catch (Exception e) {
            System.err.println("Token validation error: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Get user by username
     */
    public Optional<UsuarioAuth> getUserByUsername(String username) {
        return usuarioAuthRepository.findByUsername(username);
    }

    /**
     * Get user by ID
     */
    public Optional<UsuarioAuth> getUserById(Long id) {
        return usuarioAuthRepository.findById(id);
    }

}
