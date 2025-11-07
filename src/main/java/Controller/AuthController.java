package Controller;

import Annotation.Secured;
import Util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
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

    @Inject
    private JwtUtil jwtUtil;

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
     * Web auth callback endpoint
     * GET /api/auth/callback/web?token={jwt_token}
     *
     * Validates the JWT token and redirects to the web auth callback URI
     * configured via WEB_AUTH_CALLBACK environment variable
     */
    @GET
    @Path("/callback/web")
    @Operation(summary = "Web auth callback", description = "Valida el token JWT y redirige al callback URI configurado para web clients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Token válido - Redirección al callback URI web"),
            @ApiResponse(responseCode = "400", description = "Token faltante o inválido", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Response webCallback(
            @Parameter(description = "JWT token para validar", required = true) @QueryParam("token") String token) {

        try {
            // Validate token parameter
            if (token == null || token.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("Token parameter is required"))
                        .build();
            }

            // Validate JWT token
            if (!jwtUtil.validateToken(token)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("Invalid or expired token"))
                        .build();
            }

            // Get callback URI from environment variable
            String callbackUri = System.getenv("WEB_AUTH_CALLBACK");
            if (callbackUri == null || callbackUri.trim().isEmpty()) {
                System.err.println("WEB_AUTH_CALLBACK environment variable not configured");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(new ErrorResponse("Web callback URI not configured"))
                        .build();
            }

            // Redirect with token as query parameter
            String redirectUrl = callbackUri + (callbackUri.contains("?") ? "&" : "?") + "token=" + token;
            return Response.status(Response.Status.FOUND)
                    .header("Location", redirectUrl)
                    .build();

        } catch (Exception e) {
            System.err.println("Web callback error: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("An error occurred during web callback"))
                    .build();
        }
    }

    /**
     * Mobile auth callback endpoint
     * GET /api/auth/callback/mobile?token={jwt_token}
     *
     * Validates the JWT token and redirects to the mobile auth callback URI
     * configured via MOBILE_AUTH_CALLBACK environment variable
     */
    @GET
    @Path("/callback/mobile")
    @Operation(summary = "Mobile auth callback", description = "Valida el token JWT y redirige al callback URI configurado para mobile clients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Token válido - Redirección al callback URI mobile"),
            @ApiResponse(responseCode = "400", description = "Token faltante o inválido", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Response mobileCallback(
            @Parameter(description = "JWT token para validar", required = true) @QueryParam("token") String token) {

        try {
            // Validate token parameter
            if (token == null || token.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("Token parameter is required"))
                        .build();
            }

            // Validate JWT token
            if (!jwtUtil.validateToken(token)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("Invalid or expired token"))
                        .build();
            }

            // Get callback URI from environment variable
            String callbackUri = System.getenv("MOBILE_AUTH_CALLBACK");
            if (callbackUri == null || callbackUri.trim().isEmpty()) {
                System.err.println("MOBILE_AUTH_CALLBACK environment variable not configured");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(new ErrorResponse("Mobile callback URI not configured"))
                        .build();
            }

            // Redirect with token as query parameter
            String redirectUrl = callbackUri + (callbackUri.contains("?") ? "&" : "?") + "token=" + token;
            return Response.status(Response.Status.FOUND)
                    .header("Location", redirectUrl)
                    .build();

        } catch (Exception e) {
            System.err.println("Mobile callback error: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("An error occurred during mobile callback"))
                    .build();
        }
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
