package Annotation;

import jakarta.ws.rs.NameBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Name binding annotation for securing JAX-RS endpoints with JWT authentication
 *
 * Usage:
 * - Apply to a class to secure all methods in that resource
 * - Apply to individual methods to secure specific endpoints
 *
 * Example:
 * @Path("/protected")
 * public class ProtectedResource {
 *
 *     @GET
 *     @Secured
 *     public Response getProtectedData() {
 *         // This endpoint requires authentication
 *     }
 * }
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Secured {
}
