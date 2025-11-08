package Controller;

import Annotation.Secured;
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

    // Redirect URLs loaded from environment variables
    private final String frontendRedirectUrl;
    private final String mobileRedirectUrl;

    public AuthController() {
        // Load redirect URLs from environment variables or use defaults
        String frontendEnv = System.getenv("FRONTEND_REDIRECT");
        this.frontendRedirectUrl = (frontendEnv != null && !frontendEnv.isEmpty())
                ? frontendEnv
                : "http://localhost:3000"; // Default for development

        String mobileEnv = System.getenv("MOBILE_REDIRECT");
        this.mobileRedirectUrl = (mobileEnv != null && !mobileEnv.isEmpty())
                ? mobileEnv
                : "myapp://auth/callback"; // Default mobile deep link
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
     * Callback endpoint for gub.uy authentication - Frontend redirect
     * GET /api/auth/callback/web
     *
     * This endpoint serves an HTML page that redirects to the frontend application.
     * It's used as a callback URL for gub.uy authentication since they only accept
     * URLs under the same domain as the backend.
     */
    @GET
    @Path("/callback/web")
    @Produces(MediaType.TEXT_HTML)
    public Response callbackWeb() {
        String htmlContent = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Redirecting to Frontend...</title>\n" +
                "    <meta http-equiv=\"refresh\" content=\"0; url=" + frontendRedirectUrl + "\">\n" +
                "    <script>\n" +
                "        // Fallback redirect using JavaScript\n" +
                "        window.location.href = '" + frontendRedirectUrl + "';\n" +
                "    </script>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <p>Redirecting to frontend application...</p>\n" +
                "    <p>If you are not redirected automatically, <a href=\"" + frontendRedirectUrl
                + "\">click here</a>.</p>\n"
                +
                "</body>\n" +
                "</html>";

        return Response.ok(htmlContent).build();
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
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Redirecting to Mobile App...</title>\n" +
                "    <meta http-equiv=\"refresh\" content=\"0; url=" + mobileRedirectUrl + "\">\n" +
                "    <script>\n" +
                "        // Try to open mobile app, fallback to redirect\n" +
                "        window.location.href = '" + mobileRedirectUrl + "';\n" +
                "    </script>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <p>Redirecting to mobile application...</p>\n" +
                "    <p>If the app doesn't open automatically, <a href=\"" + mobileRedirectUrl
                + "\">click here</a>.</p>\n"
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
