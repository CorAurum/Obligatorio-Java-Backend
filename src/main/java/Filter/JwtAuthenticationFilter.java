package Filter;

import Annotation.Secured;
import Entity.UsuarioAuth;
import Service.AuthService;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.util.Optional;

@Provider
@Secured
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthenticationFilter implements ContainerRequestFilter {

    @Inject
    private AuthService authService;

    private static final String AUTHENTICATION_SCHEME = "Bearer";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Get the Authorization header from the request
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Validate the Authorization header
        if (!isTokenBasedAuthentication(authorizationHeader)) {
            abortWithUnauthorized(requestContext, "Missing or invalid Authorization header");
            return;
        }

        // Extract the token from the Authorization header
        String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();

        try {
            // Validate the token
            Optional<UsuarioAuth> userOpt = authService.validateToken(token);

            if (userOpt.isEmpty()) {
                abortWithUnauthorized(requestContext, "Invalid or expired token");
                return;
            }

            UsuarioAuth user = userOpt.get();

            // Check if user is active
            if (!user.getActivo()) {
                abortWithUnauthorized(requestContext, "User account is inactive");
                return;
            }

            // Store user information in the request context for later use
            requestContext.setProperty("user", user);
            requestContext.setProperty("userId", user.getId());
            requestContext.setProperty("username", user.getUsername());
            requestContext.setProperty("userRole", user.getRole());

        } catch (Exception e) {
            System.err.println("Authentication error: " + e.getMessage());
            abortWithUnauthorized(requestContext, "Authentication failed");
        }
    }

    private boolean isTokenBasedAuthentication(String authorizationHeader) {
        return authorizationHeader != null &&
               authorizationHeader.toLowerCase().startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext, String message) {
        ErrorResponse error = new ErrorResponse(message);
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .entity(error)
                        .build()
        );
    }

    // Error response class
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
}
