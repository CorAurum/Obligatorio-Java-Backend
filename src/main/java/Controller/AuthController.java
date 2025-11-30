package Controller;

import Annotation.Secured;
import Entity.DTO.AuthCallbackResponse;
import Entity.DTO.OidcUserInfo;
import Entity.DTO.TokenResponse;
import Entity.Usuarios.Administrador;
import Entity.UsuarioAuth;
import Service.AuthService;
import Service.AdministradorService;
import Config.OidcConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.inject.Inject;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

/**
 * AuthController - Validación de tokens JWT
 *
 * NOTA: Este sistema NO maneja login/registro ya que la autenticación
 * se realiza externamente con gub.uy. Este controlador solo valida
 * los tokens JWT enviados desde el frontend.
 */
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Autenticación", description = "Endpoints para validación de tokens JWT (autenticación externa con gub.uy)")
public class AuthController {

    @Inject
    private AuthService authService;

    @Inject
    private AdministradorService administradorService;

    @Inject
    private OidcConfig oidcConfig;

    // Redirect URLs loaded from environment variables
    private final String frontendBaseUrl;
    private final String mobileRedirectUrl;

    public AuthController() {
        // Load redirect URLs from environment variables or properties file
        this.frontendBaseUrl = loadConfigValue("FRONTEND_BASE_URL", "http://hcen-central.vercel.app");
        this.mobileRedirectUrl = loadConfigValue("MOBILE_REDIRECT", "myapp://auth/callback");
    }

    /**
     * Load configuration value with priority: Environment Variable > Properties
     * File > Default
     */
    private String loadConfigValue(String key, String defaultValue) {
        // First check environment variable
        String envValue = System.getenv(key);
        if (envValue != null && !envValue.isEmpty()) {
            return envValue;
        }

        // Then check properties file
        try {
            java.io.InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties");
            if (input != null) {
                java.util.Properties props = new java.util.Properties();
                props.load(input);
                String propValue = props.getProperty(key);
                if (propValue != null && !propValue.isEmpty()) {
                    // Remove quotes if present (some property files have quoted values)
                    propValue = propValue.replaceAll("^\"|\"$", "").trim();
                    return propValue;
                }
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not load application.properties for " + key + ": " + e.getMessage());
        }

        // Return default
        return defaultValue;
    }

    /**
     * Initiate login - redirect to gub.uy OIDC
     * GET /api/auth/login?portal=admin|usuario
     */
    @GET
    @Path("/login")
    @Produces(MediaType.TEXT_HTML)
    @Operation(summary = "Iniciar login OIDC", description = "Redirige al usuario a gub.uy para autenticación OIDC")
    public Response initiateLogin(@QueryParam("portal") @DefaultValue("usuario") String portal) {
        try {
            System.out.println("=== LOGIN FIRST DEBUG  ===");
            System.out.println("Requested portal: " + portal);
            System.out.println("==========================");

            // Validate portal parameter
            if (!portal.equals("admin") && !portal.equals("usuario")) {
                portal = "usuario";
            }

            System.out.println("=== LOGIN SECOND DEBUG ===");
            System.out.println("Requested portal: " + portal);
            System.out.println("==========================");

            // Check if OIDC is properly configured
            if (!isOidcConfigured()) {
                System.err.println("OIDC not properly configured:");
                System.err.println("ClientId: '" + oidcConfig.getClientId() + "'");
                System.err.println("ClientSecret: '" + oidcConfig.getClientSecret() + "'");
                System.err.println("AuthorizeUrl: '" + oidcConfig.getAuthorizeUrl() + "'");
                // Return HTML error page instead of JSON
                String htmlError = "<!DOCTYPE html>\n" +
                        "<html lang=\"es\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Error de Configuración</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <h1>Servicio de Autenticación no disponible</h1>\n" +
                        "    <p>El servicio de autenticación no está configurado correctamente. Por favor, contacta al administrador.</p>\n"
                        +
                        "    <p>Error: Configuración OIDC incompleta</p>\n" +
                        "    <p><a href=\"/\">Volver a la página principal</a></p>\n" +
                        "</body>\n" +
                        "</html>";
                return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity(htmlError)
                        .type(MediaType.TEXT_HTML)
                        .build();
            }

            // Generate state parameter for CSRF protection
            String state = UUID.randomUUID().toString();

            // Build authorization URL
            String authUrl = buildAuthorizationUrl(portal, state);

            System.out.println("=== LOGIN THIRD DEBUG ===");
            System.out.println("Auth URL: " + authUrl);
            System.out.println("==========================");

            // Redirect to gub.uy
            return Response.temporaryRedirect(URI.create(authUrl)).build();

        } catch (Exception e) {
            System.err.println("Login initiation error: " + e.getMessage());
            e.printStackTrace();
            // Return HTML error page instead of JSON ErrorResponse
            String htmlError = "<!DOCTYPE html>\n" +
                    "<html lang=\"es\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <title>Error al iniciar el login</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <h1>Error al iniciar el login</h1>\n" +
                    "    <p>Ocurrió un error al iniciar el login. Por favor, inténtalo nuevamente más tarde.</p>\n" +
                    "    <p><a href=\"/\">Volver a la página principal</a></p>\n" +
                    "</body>\n" +
                    "</html>";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(htmlError)
                    .build();
        }
    }

    /**
     * Validate token and get current user info (requires authentication)
     * GET /api/auth/validate
     *
     * El frontend envía el token en el header: Authorization: Bearer {token}
     * Este endpoint valida el token y devuelve la información del usuario
     */
    @GET
    @Path("/validate")
    @Secured
    @Operation(summary = "Validar token JWT", description = "Valida un token JWT y retorna la información del usuario autenticado. "
            +
            "El token debe ser obtenido previamente mediante autenticación con gub.uy.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token válido - Retorna información del usuario", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UserInfoResponse.class))),
            @ApiResponse(responseCode = "401", description = "Token inválido, expirado o no proporcionado", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Response validateToken(@Context ContainerRequestContext requestContext) {
        try {
            // El usuario ya fue validado por el JwtAuthenticationFilter
            // La información está disponible en el requestContext
            Long userId = (Long) requestContext.getProperty("userId");
            String username = (String) requestContext.getProperty("username");
            var userRole = requestContext.getProperty("userRole");

            // Build user info response
            var userInfo = new UserInfoResponse(
                    userId,
                    username,
                    userRole != null ? userRole.toString() : "UNKNOWN");

            return Response.ok(userInfo).build();

        } catch (Exception e) {
            System.err.println("Validate token error: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("An error occurred while validating token"))
                    .build();
        }
    }

    /**
     * OIDC Callback - Handle the full authentication flow
     * GET /api/auth/callback/web?code=...&state=...
     * Note: Portal is now determined by admin lookup, not query parameter
     */
    @GET
    @Path("/callback/web")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Callback OIDC completo", description = "Maneja el callback completo de gub.uy, intercambia tokens, valida usuario y redirige apropiadamente")
    public Response callbackWeb(@QueryParam("code") String code,
            @QueryParam("state") String state,
            @QueryParam("error") String error,
            @QueryParam("portal") String portal, // Portal parameter no longer used for determination
            @Context jakarta.ws.rs.core.HttpHeaders headers) {
        try {
            System.out.println("=== CALLBACK FIRST DEBUG ===");
            System.out.println("Code: " + code);
            System.out.println("State: " + state);
            System.out.println("Error: " + error);
            System.out.println("Portal: " + portal);
            System.out.println("==========================");

            // Check for OAuth error
            if (error != null) {
                System.err.println("OAuth error: " + error);
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new AuthCallbackResponse("OAuth error: " + error))
                        .build();
            }

            // Validate required parameters
            if (code == null || code.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new AuthCallbackResponse("Missing authorization code"))
                        .build();
            }

            System.out.println("=== CALLBACK SECOND DEBUG ===");
            System.out.println("Portal parameter received: " + portal + " (no longer used for determination)");
            System.out.println("==========================");

            // 1. Exchange code for tokens
            Optional<TokenResponse> tokenResponse = authService.exchangeCodeForTokens(code);
            if (!tokenResponse.isPresent()) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(new AuthCallbackResponse("Failed to exchange code for tokens"))
                        .build();
            }

            // 2. Decode id_token to get user info
            Optional<OidcUserInfo> userInfo = authService.decodeOidcToken(tokenResponse.get().getIdToken());
            if (!userInfo.isPresent()) {
                System.out.println("=== CALLBACK THIRD DEBUG ===");
                System.out.println("User info: " + userInfo.get().getNumeroDocumento());
                System.out.println("==========================");
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(new AuthCallbackResponse("Invalid id_token"))
                        .build();
            }

            // 3. Check admin status based on user data and determine effective portal/role
            String cedula = userInfo.get().getNumeroDocumento();
            if (cedula == null || cedula.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new AuthCallbackResponse("No cedula found in user info"))
                        .build();
            }

            // Check if user is an admin by looking them up in the administrador table
            Administrador admin = administradorService.obtenerPorCedula(cedula);
            boolean isAdmin = admin != null;

            // Determine effective portal and role based on admin status
            String effectivePortal;
            UsuarioAuth.Role effectiveRole;

            if (isAdmin) {
                effectivePortal = "admin";
                effectiveRole = UsuarioAuth.Role.ADMIN;
            } else {
                effectivePortal = "usuario";
                effectiveRole = UsuarioAuth.Role.USUARIO;
            }

            System.out.println("=== CALLBACK THIRD DEBUG ===");
            System.out.println("User cedula: " + cedula);
            System.out.println("Is admin: " + isAdmin);
            System.out.println("Effective portal: " + effectivePortal);
            System.out.println("Effective role: " + effectiveRole);
            System.out.println("==========================");

            // 4. Create or find user with appropriate role and generate our JWT
            Optional<Entity.UsuarioAuth> user = authService.createOrFindUserWithRole(userInfo.get(), effectiveRole);
            String backendToken = null;
            if (user.isPresent()) {
                backendToken = authService.generateBackendToken(user.get());
            }

            // 5. Determine redirect URL based on effective portal
            String redirectUrl = getRedirectUrlForPortal(effectivePortal);

            // 6. Check if this is a browser request or API request
            String acceptHeader = headers.getHeaderString("Accept");
            boolean isBrowserRequest = acceptHeader != null && acceptHeader.contains("text/html");

            // 7. Return appropriate response
            if (isBrowserRequest) {
                // Browser request - return HTML page that sets auth data and redirects
                // Since frontend and backend are on different domains, we can't set cookies
                // directly
                // Instead, we'll redirect with URL parameters that the frontend can use
                String authParams = "?auth_success=true&portal=" + effectivePortal +
                        "&user_id=" + userInfo.get().getNumeroDocumento() +
                        "&backend_token=" + java.net.URLEncoder.encode(backendToken, "UTF-8");

                String finalRedirectUrl = redirectUrl + authParams;

                String htmlRedirect = "<!DOCTYPE html>\n" +
                        "<html lang=\"es\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Autenticación exitosa</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <div style=\"text-align: center; padding: 50px;\">\n" +
                        "        <h2>✅ Autenticación exitosa!</h2>\n" +
                        "        <p>Redirigiendo a tu portal...</p>\n" +
                        "        <div style=\"margin: 20px 0;\">\n" +
                        "            <div style=\"display: inline-block; width: 40px; height: 40px; border: 4px solid #f3f3f3; border-top: 4px solid #3498db; border-radius: 50%; animation: spin 2s linear infinite;\"></div>\n"
                        +
                        "        </div>\n" +
                        "        <p><a href=\"" + finalRedirectUrl
                        + "\">Cliquea aquí si no se redirige automáticamente.</a></p>\n" +
                        "    </div>\n" +
                        "    <style>\n" +
                        "        @keyframes spin { 0% { transform: rotate(0deg); } 100% { transform: rotate(360deg); } }\n"
                        +
                        "    </style>\n" +
                        "    <script>\n" +
                        "        // Redirect after a brief delay to show success message\n" +
                        "        setTimeout(function() {\n" +
                        "            window.location.href = '" + finalRedirectUrl + "';\n" +
                        "        }, 1500);\n" +
                        "    </script>\n" +
                        "</body>\n" +
                        "</html>";

                return Response.ok(htmlRedirect)
                        .type(MediaType.TEXT_HTML)
                        .build();
            } else {
                // API request - return JSON
                AuthCallbackResponse response = new AuthCallbackResponse(
                        redirectUrl,
                        effectivePortal,
                        userInfo.get(),
                        backendToken);

                return Response.ok(response).build();
            }

        } catch (Exception e) {
            System.err.println("Callback error: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new AuthCallbackResponse("Internal server error during authentication"))
                    .build();
        }
    }

    /**
     * Check if OIDC is properly configured (not using default values)
     */
    private boolean isOidcConfigured() {
        String clientId = oidcConfig.getClientId();
        String clientSecret = oidcConfig.getClientSecret();
        String authorizeUrl = oidcConfig.getAuthorizeUrl();

        // For now, be more lenient - just check that we have non-null, non-empty values
        boolean configured = clientId != null && !clientId.isEmpty() &&
                clientSecret != null && !clientSecret.isEmpty() &&
                authorizeUrl != null && !authorizeUrl.isEmpty();

        System.out.println("OIDC Configuration check:");
        System.out.println("- ClientId: " + (clientId != null ? "'" + clientId + "'" : "null"));
        System.out.println("- ClientSecret: " + (clientSecret != null ? "[SET]" : "null"));
        System.out.println("- AuthorizeUrl: " + (authorizeUrl != null ? "'" + authorizeUrl + "'" : "null"));
        System.out.println("- Overall configured: " + configured);

        return configured;
    }

    /**
     * Build gub.uy authorization URL
     */
    private String buildAuthorizationUrl(String portal, String state) {
        // Use the injected OidcConfig instead of reading from environment directly
        String baseUrl = oidcConfig.getAuthorizeUrl();
        String clientId = oidcConfig.getClientId();
        String scope = oidcConfig.getScope();

        // Validate that we have the required configuration
        if (clientId == null || clientId.isEmpty() || clientId.equals("default-client-id")) {
            throw new IllegalStateException("OIDC client ID is not properly configured");
        }

        return baseUrl +
                "?client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
                "&redirect_uri=" + URLEncoder.encode(oidcConfig.getRedirectUri(), StandardCharsets.UTF_8) +
                "&response_type=code" +
                "&scope=" + URLEncoder.encode(scope, StandardCharsets.UTF_8) +
                "&state=" + URLEncoder.encode(state, StandardCharsets.UTF_8) +
                "&portal=" + URLEncoder.encode(portal, StandardCharsets.UTF_8) +
                "&prompt=login";
    }

    /**
     * Get redirect URL based on portal type
     * All portals redirect to /auth-redirect for session setup and portal
     * redirection
     */
    private String getRedirectUrlForPortal(String portal) {
        return frontendBaseUrl + "/auth-redirect";
    }

    /**
     * Callback endpoint for gub.uy authentication - Mobile app redirect
     * GET /api/auth/callback/mobile
     *
     * This endpoint redirects to the mobile app using a deep link.
     * It's used as a callback URL for gub.uy authentication for mobile
     * applications.
     */
    @GET
    @Path("/callback/mobile")
    @Produces(MediaType.TEXT_HTML)
    public Response callbackMobile() {
        String htmlContent = "<!DOCTYPE html>\n" +
                "<html lang=\"es\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Redirigiendo a la aplicación móvil...</title>\n" +
                "    <meta http-equiv=\"refresh\" content=\"0; url=" + mobileRedirectUrl + "\">\n" +
                "    <script>\n" +
                "        // Try to open mobile app, fallback to redirect\n" +
                "        window.location.href = '" + mobileRedirectUrl + "';\n" +
                "    </script>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <p>Redirigiendo a la aplicación móvil...</p>\n" +
                "    <p>Si la aplicación no se abre automáticamente, <a href=\"" + mobileRedirectUrl
                + "\">cliquea aquí</a>.</p>\n"
                +
                "</body>\n" +
                "</html>";

        return Response.ok(htmlContent).build();
    }

    // Inner classes for responses
    @Schema(description = "Respuesta de error")
    public static class ErrorResponse {
        @Schema(description = "Mensaje de error", example = "Invalid or expired token")
        private String error;

        @Schema(description = "Timestamp del error", example = "1705328400000")
        private long timestamp;

        public ErrorResponse(String error) {
            this.error = error;
            this.timestamp = System.currentTimeMillis();
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }

    @Schema(description = "Información del usuario autenticado")
    public static class UserInfoResponse {
        @Schema(description = "ID del usuario", example = "123")
        private Long userId;

        @Schema(description = "Nombre de usuario", example = "juan.perez")
        private String username;

        @Schema(description = "Rol del usuario", example = "USUARIO", allowableValues = { "ADMIN", "PROFESIONAL",
                "USUARIO", "SISTEMA" })
        private String role;

        public UserInfoResponse(Long userId, String username, String role) {
            this.userId = userId;
            this.username = username;
            this.role = role;
        }

        // Getters and Setters
        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}
