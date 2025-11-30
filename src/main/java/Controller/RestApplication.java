package Controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
@OpenAPIDefinition(info = @Info(title = "Sistema de Salud API", version = "1.0.0", description = "API REST para el sistema de gestión de salud integrado con gub.uy. "
                +
                "Incluye gestión de usuarios, profesionales, centros de salud, documentos clínicos y más.", contact = @Contact(name = "Equipo de Desarrollo", email = "soporte@salud.gub.uy"), license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html")), servers = {
                }, security = @SecurityRequirement(name = "bearerAuth"))
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", description = "Autenticación mediante JWT token. Obtén el token autenticándote con gub.uy y luego úsalo en el header: Authorization: Bearer {token}")
public class RestApplication extends Application {
}