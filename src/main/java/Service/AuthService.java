package Service;

import Entity.DTO.OidcUserInfo;
import Entity.DTO.TokenResponse;
import Entity.Usuarios.Administrador;
import Entity.UsuarioAuth;
import Repository.UsuarioAuthRepository;
import Util.JwtUtil;
import Config.OidcConfig;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Base64;
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

    @Inject
    private OidcConfig oidcConfig;

    @Inject
    private AdministradorService administradorService;

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

    /**
     * Exchange authorization code for tokens with gub.uy
     */
    public Optional<TokenResponse> exchangeCodeForTokens(String code) {
        try {
            Client client = ClientBuilder.newClient();

            // Create Basic Auth header
            String credentials = oidcConfig.getClientId() + ":" + oidcConfig.getClientSecret();
            String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

            // Prepare form data
            Form form = new Form();
            form.param("grant_type", "authorization_code");
            form.param("code", code);
            form.param("redirect_uri", oidcConfig.getRedirectUri());

            // Debug logging
            System.out.println("=== TOKEN EXCHANGE DEBUG ===");
            System.out.println("Token URL: " + oidcConfig.getTokenUrl());
            System.out.println("Client ID: " + oidcConfig.getClientId());
            System.out.println("Code: " + code.substring(0, Math.min(20, code.length())) + "...");
            System.out.println("Redirect URI: " + oidcConfig.getRedirectUri());

            // Make token request
            Response response = client.target(oidcConfig.getTokenUrl())
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Basic " + encodedCredentials)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .post(Entity.form(form));

            System.out.println("Response status: " + response.getStatus());

            if (response.getStatus() == 200) {
                TokenResponse tokenResponse = response.readEntity(TokenResponse.class);
                System.out.println("Token exchange successful!");
                System.out.println(
                        "Received tokens - ID Token: " + (tokenResponse.getIdToken() != null ? "[PRESENT]" : "null"));
                System.out.println("Access Token: " + (tokenResponse.getAccessToken() != null ? "[PRESENT]" : "null"));
                System.out
                        .println("Refresh Token: " + (tokenResponse.getRefreshToken() != null ? "[PRESENT]" : "null"));
                System.out.println("Token Type: " + tokenResponse.getTokenType());
                System.out.println("Expires In: " + tokenResponse.getExpiresIn());
                System.out.println("=== END TOKEN EXCHANGE DEBUG ===");
                return Optional.of(tokenResponse);
            } else {
                String errorResponse = response.readEntity(String.class);
                System.err.println("Token exchange failed: " + response.getStatus() + " - " + errorResponse);
                System.out.println("=== END TOKEN EXCHANGE DEBUG ===");
                return Optional.empty();
            }

        } catch (Exception e) {
            System.err.println("Error exchanging code for tokens: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Decode JWT token from gub.uy (without signature verification for now)
     */
    public Optional<OidcUserInfo> decodeOidcToken(String token) {
        try {
            // Split JWT into parts
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return Optional.empty();
            }

            // Decode payload (second part)
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));

            System.out.println("=== JWT PAYLOAD DEBUG ===");
            System.out.println("JWT Payload: " + payload);
            System.out.println("==========================");

            // Parse JSON
            OidcUserInfo userInfo = new com.fasterxml.jackson.databind.ObjectMapper()
                    .readValue(payload, OidcUserInfo.class);

            System.out.println("Successfully decoded user info:");
            System.out.println("- Subject: " + userInfo.getSubject());
            System.out.println("- Numero Documento: " + userInfo.getNumeroDocumento());
            System.out.println("- Email: " + userInfo.getEmail());
            System.out.println("- Name: " + userInfo.getName());

            return Optional.of(userInfo);

        } catch (Exception e) {
            System.err.println("Error decoding OIDC token: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Check if user is an admin based on cedula
     */
    public boolean isUserAdmin(String cedula) {
        try {
            Administrador admin = administradorService.obtenerPorCedula(cedula);
            return admin != null && admin.getActivo();
        } catch (Exception e) {
            System.err.println("Error checking admin status: " + e.getMessage());
            return false;
        }
    }

    /**
     * Create or find user and generate our own JWT token
     */
    public Optional<UsuarioAuth> createOrFindUser(OidcUserInfo oidcUserInfo) {
        try {
            // Try to find existing user by cedula (numero_documento)
            String cedula = oidcUserInfo.getNumeroDocumento();
            if (cedula == null || cedula.isEmpty()) {
                return Optional.empty();
            }

            // For now, we'll create a temporary UsuarioAuth for JWT generation
            // In a full implementation, you'd want to sync with your user database
            UsuarioAuth user = new UsuarioAuth();
            user.setUsername(cedula); // Use cedula as username
            user.setEmail(oidcUserInfo.getEmail());
            user.setRole(UsuarioAuth.Role.USUARIO); // Default role
            user.setActivo(true);

            // Set a temporary ID for JWT generation
            user.setId(1L); // This should be handled properly in production

            return Optional.of(user);

        } catch (Exception e) {
            System.err.println("Error creating/finding user: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Generate our own JWT token for the user
     */
    public String generateBackendToken(UsuarioAuth user) {
        return jwtUtil.generateToken(user);
    }

}
