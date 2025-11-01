package Controller;

import Annotation.Secured;
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
public class AuthController {

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
                    userRole != null ? userRole.toString() : "UNKNOWN"
            );

            return Response.ok(userInfo).build();

        } catch (Exception e) {
            System.err.println("Validate token error: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("An error occurred while validating token"))
                    .build();
        }
    }

    // Inner classes for responses
    public static class ErrorResponse {
        private String error;
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

    public static class UserInfoResponse {
        private Long userId;
        private String username;
        private String role;

        public UserInfoResponse(Long userId, String username, String role) {
            this.userId = userId;
            this.username = username;
            this.role = role;
        }

        // Getters and Setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
}
