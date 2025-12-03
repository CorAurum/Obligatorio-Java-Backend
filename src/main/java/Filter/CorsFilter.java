package Filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {

        String origin = requestContext.getHeaderString("Origin");

        // Define allowed origins
        String[] allowedOrigins = {
                "http://localhost:3000",
                "https://hcen-central.vercel.app"
        };

        for (String allowed : allowedOrigins) {
            if (allowed.equals(origin)) {
                responseContext.getHeaders().putSingle("Access-Control-Allow-Origin", origin);
                break;
            }
        }

        responseContext.getHeaders().putSingle("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH");
        responseContext.getHeaders().putSingle("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        responseContext.getHeaders().putSingle("Access-Control-Allow-Credentials", "true");

        if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
            responseContext.setStatus(200);
        }
    }
}