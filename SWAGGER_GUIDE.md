# Guía de Uso de Swagger UI - Sistema de Salud API

## 🎯 ¿Qué es Swagger?

Swagger (OpenAPI) es una herramienta que **documenta automáticamente tu API REST** y proporciona una **interfaz web interactiva** donde puedes:

- ✅ Ver todos los endpoints disponibles
- ✅ Probar los endpoints directamente desde el navegador
- ✅ Ver los parámetros requeridos y respuestas esperadas
- ✅ Autenticarte con JWT tokens
- ✅ Exportar la especificación OpenAPI

---

## 🚀 Acceder a Swagger UI

### 1. Inicia tu servidor

```bash
# Asegúrate de que tu aplicación esté corriendo
# Por defecto en: http://localhost:8080
```

### 2. Abre Swagger UI en tu navegador

```
http://localhost:8080/api/swagger
```

### 3. También puedes acceder al JSON de OpenAPI

```
http://localhost:8080/api/openapi.json
```

---

## 🔐 Autenticación con JWT en Swagger

### Paso 1: Obtén un Token JWT

Primero necesitas un token JWT. Hay dos formas:

#### Opción A: Generar Token de Prueba en jwt.io

1. Ve a https://jwt.io/
2. En la sección **Decoded**, configura el **Payload**:

```json
{
  "userId": 1,
  "username": "test_user",
  "email": "test@example.com",
  "role": "ADMIN",
  "activo": true,
  "sub": "test_user",
  "iat": 1705328400,
  "exp": 9999999999
}
```

3. En la sección **Verify Signature**, usa la misma clave secreta que configuraste en `JWT_SECRET_KEY`:

```
your-secret-key-change-this-in-production-must-be-at-least-256-bits-long-for-security
```

4. Copia el token generado (la cadena completa del lado izquierdo)

#### Opción B: Obtener Token Real desde tu Frontend

Si ya tienes integración con gub.uy en tu frontend, usa el token que generas ahí.

### Paso 2: Configurar Autenticación en Swagger UI

1. En Swagger UI, busca el botón **"Authorize"** (🔓) en la parte superior derecha
2. Haz clic en él
3. Se abrirá un modal con el campo **"bearerAuth"**
4. En el campo **Value**, escribe:
   ```
   Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
   ```
   (Nota: Incluye la palabra "Bearer" seguida de un espacio y luego tu token)

5. Haz clic en **"Authorize"**
6. Haz clic en **"Close"**

✅ ¡Listo! Ahora todos tus endpoints protegidos con `@Secured` se ejecutarán con este token.

---

## 📖 Usar Swagger UI - Tutorial Paso a Paso

### 1. Explorar Endpoints Disponibles

En la página principal verás todos los controladores agrupados por tags:

```
🔵 Autenticación
   GET /api/auth/validate - Validar token JWT

🔵 Administradores
   POST   /api/administradores     - Crear administrador
   GET    /api/administradores     - Listar administradores
   GET    /api/administradores/{id} - Obtener administrador
   PUT    /api/administradores/{id} - Actualizar administrador
   DELETE /api/administradores/{id} - Eliminar administrador

🔵 Profesionales
   ...

🔵 Centros de Salud
   ...
```

### 2. Probar un Endpoint GET

Ejemplo: Validar tu token JWT

1. **Expande** el endpoint `GET /api/auth/validate`
2. Haz clic en **"Try it out"**
3. Haz clic en **"Execute"**
4. Verás la respuesta:

```json
{
  "userId": 1,
  "username": "test_user",
  "role": "ADMIN"
}
```

### 3. Probar un Endpoint POST

Ejemplo: Crear un administrador

1. **Expande** el endpoint `POST /api/administradores`
2. Haz clic en **"Try it out"**
3. Edita el **Request body** (JSON):

```json
{
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "juan.perez@salud.gub.uy",
  "activo": true
}
```

4. Haz clic en **"Execute"**
5. Verás la respuesta con el administrador creado

### 4. Probar un Endpoint con Parámetros de Ruta

Ejemplo: Obtener un administrador por ID

1. **Expande** el endpoint `GET /api/administradores/{id}`
2. Haz clic en **"Try it out"**
3. Ingresa el **id** en el campo (ejemplo: `1`)
4. Haz clic en **"Execute"**
5. Verás el administrador con ese ID

### 5. Ver Respuestas de Error

Swagger también muestra los posibles errores:

- **200**: Éxito
- **201**: Creado
- **400**: Bad Request (datos inválidos)
- **401**: No autorizado (token inválido)
- **404**: No encontrado
- **500**: Error del servidor

---

## 🎨 Interfaz de Swagger UI

### Elementos Principales

```
┌─────────────────────────────────────────────────────────┐
│  Sistema de Salud API                    [Authorize 🔓] │
│  Version: 1.0.0                                         │
├─────────────────────────────────────────────────────────┤
│  Servers: ▼ http://localhost:8080/api                  │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  🔵 Autenticación ▼                                     │
│      GET /api/auth/validate                             │
│          Validar token JWT                              │
│          [Try it out]                                   │
│                                                         │
│  🔵 Administradores ▼                                   │
│      POST /api/administradores                          │
│      GET  /api/administradores                          │
│      ...                                                │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

### Secciones de Cada Endpoint

Cuando expandas un endpoint, verás:

1. **Description**: Descripción de qué hace el endpoint
2. **Parameters**: Parámetros que acepta (path, query, body)
3. **Request body**: Esquema JSON del cuerpo de la petición
4. **Responses**: Códigos de respuesta posibles y sus esquemas
5. **Try it out**: Botón para probar el endpoint
6. **Execute**: Ejecutar la petición
7. **Response**: Ver la respuesta del servidor

---

## 💡 Ejemplos de Uso Común

### Ejemplo 1: Flujo Completo de Autenticación

```
1. Generar token en jwt.io
2. En Swagger: Click "Authorize"
3. Pegar: Bearer {tu-token}
4. Click "Authorize" y "Close"
5. Probar GET /api/auth/validate
6. ✅ Debería retornar tu información de usuario
```

### Ejemplo 2: Crear y Consultar un Recurso

```
1. POST /api/administradores con datos
   {
     "nombre": "María",
     "apellido": "García",
     "email": "maria@salud.gub.uy"
   }

2. Copiar el "id" de la respuesta (ej: 5)

3. GET /api/administradores/5

4. ✅ Debería retornar el administrador creado
```

### Ejemplo 3: Testing de Errores

```
1. GET /api/administradores/999999
   (ID que no existe)

2. ✅ Debería retornar 404 Not Found
```

---

## 🔧 Documentar tus Propios Endpoints

### Paso 1: Agregar Anotaciones Básicas

```java
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Path("/mi-recurso")
@Tag(name = "Mi Recurso", description = "Endpoints para gestionar mi recurso")
public class MiController {

    @GET
    @Operation(summary = "Listar todos los recursos")
    public Response listar() {
        // tu código
    }
}
```

### Paso 2: Documentar Respuestas

```java
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@GET
@Path("/{id}")
@Operation(summary = "Obtener por ID")
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "Recurso encontrado",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = MiEntidad.class)
        )
    ),
    @ApiResponse(
        responseCode = "404",
        description = "Recurso no encontrado"
    )
})
public Response obtenerPorId(@PathParam("id") Long id) {
    // tu código
}
```

### Paso 3: Marcar Endpoints Protegidos

```java
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import Annotation.Secured;

@GET
@Secured
@Operation(
    summary = "Endpoint protegido",
    security = @SecurityRequirement(name = "bearerAuth")
)
public Response endpointProtegido() {
    // tu código
}
```

### Paso 4: Documentar Parámetros

```java
import io.swagger.v3.oas.annotations.Parameter;

@GET
public Response buscar(
    @QueryParam("nombre")
    @Parameter(description = "Nombre a buscar", example = "Juan")
    String nombre,

    @QueryParam("activo")
    @Parameter(description = "Filtrar por estado activo", example = "true")
    Boolean activo
) {
    // tu código
}
```

### Paso 5: Documentar Request Body

```java
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@POST
@Operation(summary = "Crear recurso")
public Response crear(
    @RequestBody(
        description = "Datos del recurso a crear",
        required = true,
        content = @Content(
            schema = @Schema(implementation = MiEntidad.class)
        )
    )
    MiEntidad entidad
) {
    // tu código
}
```

### Paso 6: Documentar DTOs y Entidades

```java
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Entidad que representa un Administrador")
public class Administrador {

    @Schema(description = "ID único del administrador", example = "1")
    private Long id;

    @Schema(description = "Nombre del administrador", example = "Juan", required = true)
    private String nombre;

    @Schema(description = "Email del administrador", example = "juan@salud.gub.uy", required = true)
    private String email;

    @Schema(description = "Estado activo/inactivo", example = "true")
    private Boolean activo;

    // getters y setters
}
```

---

## 📋 Checklist para Documentar un Endpoint

Cuando crees un nuevo endpoint, asegúrate de:

- [ ] Agregar `@Tag` al controlador
- [ ] Agregar `@Operation` con `summary` y `description`
- [ ] Documentar todas las respuestas con `@ApiResponses`
- [ ] Si está protegido, agregar `@SecurityRequirement(name = "bearerAuth")`
- [ ] Documentar parámetros con `@Parameter`
- [ ] Documentar el request body si aplica
- [ ] Documentar los schemas de las entidades con `@Schema`

---

## 🎯 Ventajas de Usar Swagger

### Para Desarrolladores

1. **Documentación automática**: No necesitas escribir docs aparte
2. **Testing integrado**: Prueba endpoints sin Postman
3. **Validación visual**: Ves inmediatamente si algo falla
4. **Onboarding rápido**: Nuevos desarrolladores entienden la API rápido

### Para Frontend

1. **Contrato claro**: Saben exactamente qué esperar
2. **Tipos de datos**: Ven los schemas JSON
3. **Pruebas rápidas**: Pueden probar sin backend local
4. **Generación de código**: Pueden generar clientes automáticamente

### Para QA/Testing

1. **Test manual fácil**: Interface visual para testing
2. **Documentación de casos**: Ven todos los casos de error
3. **Regresión**: Pueden verificar que todo funciona después de cambios

---

## 🛠️ Troubleshooting

### No aparece Swagger UI

**Problema**: Al ir a `/api/swagger` no carga nada

**Solución**:
1. Verifica que el servidor esté corriendo
2. Revisa logs del servidor por errores
3. Asegúrate de que las dependencias de Swagger estén en `pom.xml`
4. Intenta acceder a `/api/openapi.json` para ver si el JSON se genera

### No aparecen mis endpoints

**Problema**: Swagger UI carga pero no muestra mis controladores

**Solución**:
1. Asegúrate de que tus controladores tengan `@Path`
2. Verifica que estén en el package `Controller`
3. Agrega `@Tag` a tus controladores para que aparezcan
4. Reinicia el servidor

### Error 401 en todos los endpoints protegidos

**Problema**: Todos los endpoints con `@Secured` fallan con 401

**Solución**:
1. Haz clic en "Authorize" y configura el token
2. Verifica que el token sea válido (no expirado)
3. Asegúrate de incluir "Bearer " antes del token
4. Verifica que `JWT_SECRET_KEY` sea la misma que usaste para generar el token

### CORS errors al usar Swagger

**Problema**: Errores de CORS al ejecutar requests

**Solución**:
1. Configura CORS en tu backend
2. Si estás usando un proxy, configúralo correctamente
3. En desarrollo, puedes desactivar CORS temporalmente

---

## 🌐 Exportar Especificación OpenAPI

### Para Generar Código Cliente

1. Ve a `/api/openapi.json`
2. Copia el JSON completo
3. Ve a https://editor.swagger.io/
4. Pega el JSON
5. En el menú, haz clic en **Generate Client** y elige tu lenguaje:
   - JavaScript/TypeScript
   - Python
   - Java
   - C#
   - PHP
   - etc.

### Para Importar en Postman

1. Abre Postman
2. File → Import
3. Pega la URL: `http://localhost:8080/api/openapi.json`
4. ✅ Todos tus endpoints estarán disponibles en Postman

### Para Usar en Herramientas de Testing

- **Insomnia**: Import → From URL → `/api/openapi.json`
- **API Testing Tools**: La mayoría soporta OpenAPI 3.0

---

## 📚 Recursos Adicionales

- **OpenAPI Specification**: https://swagger.io/specification/
- **Swagger Editor**: https://editor.swagger.io/
- **Anotaciones Jakarta**: https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Annotations
- **Ejemplos**: https://github.com/swagger-api/swagger-samples

---

## 🎓 Próximos Pasos

1. **Explora Swagger UI** en tu aplicación
2. **Autentícate** con un token JWT
3. **Prueba** todos los endpoints disponibles
4. **Documenta** tus propios endpoints siguiendo los ejemplos
5. **Comparte** la URL de Swagger con tu equipo

---

## 💡 Tips Pro

1. **Usa Tags para organizar**: Agrupa endpoints relacionados
2. **Ejemplos realistas**: Usa datos de ejemplo que tengan sentido
3. **Describe los errores**: Explica por qué puede fallar un endpoint
4. **Mantén actualizado**: La documentación debe reflejar el código
5. **Usa Schemas**: Define una vez, reutiliza en múltiples endpoints
6. **Ambiente por defecto**: Configura el servidor local como predeterminado

---

## ✅ Resumen Rápido

| Acción | URL/Comando |
|--------|-------------|
| Ver Swagger UI | `http://localhost:8080/api/swagger` |
| Ver OpenAPI JSON | `http://localhost:8080/api/openapi.json` |
| Autenticarse | Click "Authorize" → `Bearer {token}` |
| Probar endpoint | Click "Try it out" → "Execute" |
| Generar token de prueba | https://jwt.io/ |

¡Listo! Ahora tienes toda la información para usar Swagger en tu proyecto. 🚀
