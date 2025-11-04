package Controller;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.InputStream;

/**
 * Controller para servir la documentación Swagger UI
 */
@Path("/")
@Hidden
public class OpenApiController {

    /**
     * Redirect to Swagger UI
     */
    @GET
    @Path("/swagger")
    @Produces(MediaType.TEXT_HTML)
    public Response swaggerUI() {
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"es\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Sistema de Salud API - Swagger UI</title>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"https://unpkg.com/swagger-ui-dist@5.10.5/swagger-ui.css\" />\n" +
                "    <link rel=\"icon\" type=\"image/png\" href=\"https://unpkg.com/swagger-ui-dist@5.10.5/favicon-32x32.png\" sizes=\"32x32\" />\n" +
                "    <style>\n" +
                "        html { box-sizing: border-box; overflow: -moz-scrollbars-vertical; overflow-y: scroll; }\n" +
                "        *, *:before, *:after { box-sizing: inherit; }\n" +
                "        body { margin:0; padding:0; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div id=\"swagger-ui\"></div>\n" +
                "    <script src=\"https://unpkg.com/swagger-ui-dist@5.10.5/swagger-ui-bundle.js\" charset=\"UTF-8\"></script>\n" +
                "    <script src=\"https://unpkg.com/swagger-ui-dist@5.10.5/swagger-ui-standalone-preset.js\" charset=\"UTF-8\"></script>\n" +
                "    <script>\n" +
                "        window.onload = function() {\n" +
                "            const ui = SwaggerUIBundle({\n" +
                "                url: window.location.origin + '/api/openapi.json',\n" +
                "                dom_id: '#swagger-ui',\n" +
                "                deepLinking: true,\n" +
                "                presets: [\n" +
                "                    SwaggerUIBundle.presets.apis,\n" +
                "                    SwaggerUIStandalonePreset\n" +
                "                ],\n" +
                "                plugins: [\n" +
                "                    SwaggerUIBundle.plugins.DownloadUrl\n" +
                "                ],\n" +
                "                layout: \"StandaloneLayout\",\n" +
                "                persistAuthorization: true,\n" +
                "                tryItOutEnabled: true,\n" +
                "                displayRequestDuration: true\n" +
                "            });\n" +
                "            window.ui = ui;\n" +
                "        };\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";

        return Response.ok(html).build();
    }

    /**
     * Serve OpenAPI JSON specification
     */
    @GET
    @Path("/openapi.json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOpenApiSpec() {
        try {
            // Generate OpenAPI spec dynamically
            io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder builder =
                new io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder();

            io.swagger.v3.oas.integration.api.OpenApiContext context = builder
                .buildContext(true);

            io.swagger.v3.oas.models.OpenAPI openAPI = context.read();

            // Convert to JSON
            String json = io.swagger.v3.core.util.Json.pretty(openAPI);

            return Response.ok(json).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Failed to generate OpenAPI specification\"}")
                    .build();
        }
    }
}
