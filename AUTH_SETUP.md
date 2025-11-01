# Sistema de Autenticación JWT - Guía de Uso

## ⚠️ IMPORTANTE: Autenticación Externa con gub.uy

Este sistema **NO maneja login/registro local**. La autenticación se realiza externamente mediante **gub.uy**. Este backend solo se encarga de:

1. **Validar tokens JWT** enviados desde el frontend
2. **Proteger endpoints** que requieren autenticación
3. **Extraer información del usuario** desde el token

---

## 📦 Componentes Implementados

### 1. **Configuración**
- **[Config/JwtConfig.java](src/main/java/Config/JwtConfig.java)** - Gestión de configuración JWT (clave secreta, expiración)

### 2. **Utilidades**
- **[Util/JwtUtil.java](src/main/java/Util/JwtUtil.java)** - Generación y validación de tokens JWT
- **[Util/PasswordUtil.java](src/main/java/Util/PasswordUtil.java)** - Hash de contraseñas con BCrypt (opcional, para uso futuro)

### 3. **Servicios**
- **[Service/AuthService.java](src/main/java/Service/AuthService.java)** - Validación de tokens y consulta de usuarios

### 4. **Controlador**
- **[Controller/AuthController.java](src/main/java/Controller/AuthController.java)** - Endpoint para validar tokens

### 5. **Seguridad**
- **[Filter/JwtAuthenticationFilter.java](src/main/java/Filter/JwtAuthenticationFilter.java)** - Filtro automático que valida tokens en endpoints protegidos
- **[Annotation/Secured.java](src/main/java/Annotation/Secured.java)** - Anotación para marcar endpoints protegidos

### 6. **Configuración**
- **[application.properties](src/main/resources/application.properties)** - Archivo de configuración
- **[pom.xml](pom.xml)** - Dependencias agregadas (JJWT, BCrypt)

---

## 🔐 Variables de Entorno Requeridas

### **1. JWT_SECRET_KEY** (OBLIGATORIA en producción)

La clave secreta para firmar y validar los tokens JWT. **Debe tener al menos 256 bits (32 caracteres)**.

```bash
# Linux/Mac
export JWT_SECRET_KEY="mi-super-clave-secreta-de-256-bits-para-jwt-tokens-2024-produccion"

# Windows CMD
set JWT_SECRET_KEY=mi-super-clave-secreta-de-256-bits-para-jwt-tokens-2024-produccion

# Windows PowerShell
$env:JWT_SECRET_KEY="mi-super-clave-secreta-de-256-bits-para-jwt-tokens-2024-produccion"
```

**⚠️ IMPORTANTE**:
- Esta clave **DEBE SER LA MISMA** que usa el frontend para generar los tokens
- En desarrollo puedes usar la clave por defecto del `application.properties`
- En producción **SIEMPRE** usa una variable de entorno segura

### **2. JWT_EXPIRATION_MS** (Opcional)

Tiempo de expiración del token en milisegundos.

```bash
# Ejemplos:
export JWT_EXPIRATION_MS=86400000   # 24 horas (por defecto)
export JWT_EXPIRATION_MS=3600000    # 1 hora
export JWT_EXPIRATION_MS=604800000  # 7 días
```

---

## 🚀 Flujo de Autenticación

### Flujo Completo

```
┌─────────────┐      ┌──────────────┐      ┌─────────────┐      ┌─────────────┐
│   Usuario   │      │   Frontend   │      │   gub.uy    │      │   Backend   │
└──────┬──────┘      └──────┬───────┘      └──────┬──────┘      └──────┬──────┘
       │                    │                     │                    │
       │ 1. Click "Login"   │                     │                    │
       ├───────────────────>│                     │                    │
       │                    │                     │                    │
       │                    │ 2. Redirect a gub.uy│                    │
       │                    ├────────────────────>│                    │
       │                    │                     │                    │
       │                    │ 3. Usuario se autentica                  │
       │                    │                     │                    │
       │                    │ 4. Callback con datos                    │
       │                    │<────────────────────┤                    │
       │                    │                     │                    │
       │                    │ 5. Genera JWT token │                    │
       │                    │     localmente      │                    │
       │                    │                     │                    │
       │                    │ 6. Guarda token en  │                    │
       │                    │    localStorage     │                    │
       │                    │                     │                    │
       │ 7. Usuario accede  │                     │                    │
       │    a recursos      │                     │                    │
       ├───────────────────>│                     │                    │
       │                    │                     │                    │
       │                    │ 8. Request con token                     │
       │                    │    Authorization: Bearer {token}         │
       │                    ├─────────────────────────────────────────>│
       │                    │                     │                    │
       │                    │                     │ 9. Valida token    │
       │                    │                     │    (firma, exp.)   │
       │                    │                     │                    │
       │                    │ 10. Response con datos                   │
       │                    │<─────────────────────────────────────────┤
       │                    │                     │                    │
       │ 11. Muestra datos  │                     │                    │
       │<───────────────────┤                     │                    │
```

### Responsabilidades

**Frontend:**
- Autenticar con gub.uy
- Generar token JWT con los datos del usuario
- Incluir token en todas las requests: `Authorization: Bearer {token}`

**Backend:**
- Validar firma del token
- Verificar que no esté expirado
- Extraer información del usuario
- Proteger endpoints con `@Secured`

---

## 📡 Endpoints Disponibles

### 1. Validar Token (Protegido)

**Endpoint**: `GET /api/auth/validate`

**Headers**:
```
Authorization: Bearer {tu-token-jwt}
```

**Response** (200 OK):
```json
{
  "userId": 123,
  "username": "juan.perez",
  "role": "USUARIO"
}
```

**Response** (401 Unauthorized):
```json
{
  "error": "Invalid or expired token",
  "timestamp": 1705328400000
}
```

**Ejemplo con cURL**:
```bash
curl -X GET http://localhost:8080/api/auth/validate \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

**Ejemplo con JavaScript/Fetch**:
```javascript
const token = localStorage.getItem('jwt_token');

fetch('http://localhost:8080/api/auth/validate', {
  method: 'GET',
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  }
})
.then(response => response.json())
.then(data => console.log('Usuario validado:', data))
.catch(error => console.error('Error:', error));
```

---

## 🔒 Cómo Proteger tus Endpoints

### Opción 1: Proteger un Método Específico

```java
import Annotation.Secured;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("/profesionales")
public class ProfesionalController {

    @GET
    @Secured  // ← Solo usuarios autenticados pueden acceder
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProfesionales() {
        // Tu código aquí
        return Response.ok(profesionales).build();
    }

    @GET
    @Path("/publico")
    // Sin @Secured = endpoint público
    public Response getInfoPublica() {
        return Response.ok("Info pública").build();
    }
}
```

### Opción 2: Proteger Toda una Clase

```java
import Annotation.Secured;
import jakarta.ws.rs.*;

@Path("/administradores")
@Secured  // ← Todos los métodos requieren autenticación
public class AdministradorController {

    @GET
    public Response getAll() {
        // Protegido automáticamente
    }

    @POST
    public Response create(AdministradorDTO dto) {
        // También protegido
    }
}
```

### Opción 3: Acceder a Información del Usuario Autenticado

Cuando un endpoint está protegido con `@Secured`, puedes acceder a la información del usuario:

```java
import Annotation.Secured;
import Entity.UsuarioAuth;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;

@Path("/documentos")
public class DocumentoController {

    @GET
    @Path("/mis-documentos")
    @Secured
    public Response getMisDocumentos(@Context ContainerRequestContext requestContext) {
        // Extraer información del usuario del contexto
        UsuarioAuth user = (UsuarioAuth) requestContext.getProperty("user");
        Long userId = (Long) requestContext.getProperty("userId");
        String username = (String) requestContext.getProperty("username");
        UsuarioAuth.Role role = (UsuarioAuth.Role) requestContext.getProperty("userRole");

        // Usar la información
        System.out.println("Usuario autenticado: " + username);
        System.out.println("ID: " + userId);
        System.out.println("Rol: " + role);

        // Obtener solo documentos del usuario actual
        List<Documento> documentos = documentoService.findByUserId(userId);

        return Response.ok(documentos).build();
    }
}
```

---

## 🎯 Ejemplo de Uso Frontend → Backend

### Frontend: Generar y Enviar Token

```javascript
// 1. Después de autenticar con gub.uy, generar el token
// (Necesitas una librería JWT en el frontend como jose o jsonwebtoken)
import { SignJWT } from 'jose';

async function generarToken(usuarioGubUy) {
  const secret = new TextEncoder().encode('mi-super-clave-secreta-de-256-bits-para-jwt-tokens-2024');

  const token = await new SignJWT({
    userId: usuarioGubUy.id,
    username: usuarioGubUy.username,
    email: usuarioGubUy.email,
    role: 'USUARIO',
    activo: true
  })
  .setProtectedHeader({ alg: 'HS256' })
  .setSubject(usuarioGubUy.username)
  .setIssuedAt()
  .setExpirationTime('24h')
  .sign(secret);

  // 2. Guardar token en localStorage
  localStorage.setItem('jwt_token', token);

  return token;
}

// 3. Hacer request al backend con el token
async function fetchProtectedData() {
  const token = localStorage.getItem('jwt_token');

  const response = await fetch('http://localhost:8080/api/profesionales', {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    }
  });

  if (response.ok) {
    const data = await response.json();
    console.log('Datos:', data);
  } else if (response.status === 401) {
    console.error('Token inválido o expirado');
    // Redirigir a login
  }
}
```

### Backend: Recibir y Validar

```java
@Path("/profesionales")
public class ProfesionalController {

    @GET
    @Secured  // El JwtAuthenticationFilter valida automáticamente
    public Response getAllProfesionales(@Context ContainerRequestContext requestContext) {
        // El token ya fue validado por el filtro
        Long userId = (Long) requestContext.getProperty("userId");
        String role = requestContext.getProperty("userRole").toString();

        // Tu lógica aquí
        List<Profesional> profesionales = profesionalService.findAll();
        return Response.ok(profesionales).build();
    }
}
```

---

## 🛡️ Estructura del Token JWT

El token que el frontend debe generar debe tener esta estructura:

### Header
```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

### Payload (Claims)
```json
{
  "userId": 123,
  "username": "juan.perez",
  "email": "juan.perez@example.com",
  "role": "USUARIO",
  "activo": true,
  "sub": "juan.perez",
  "iat": 1705328400,
  "exp": 1705414800
}
```

**Claims requeridos**:
- `userId`: ID del usuario (Long)
- `username`: Nombre de usuario (String)
- `email`: Email del usuario (String)
- `role`: Rol del usuario (`ADMIN`, `PROFESIONAL`, `USUARIO`, `SISTEMA`)
- `activo`: Si el usuario está activo (Boolean)
- `sub`: Subject (normalmente username)
- `iat`: Issued At (timestamp)
- `exp`: Expiration (timestamp)

### Signature

Firmado con la clave secreta configurada en `JWT_SECRET_KEY`.

---

## ⚙️ Configuración en Servidor de Aplicaciones

### WildFly / JBoss

```bash
# Usando CLI
/system-property=JWT_SECRET_KEY:add(value="your-production-secret-key-256-bits-long")
/system-property=JWT_EXPIRATION_MS:add(value="86400000")

# O en standalone.xml
<system-properties>
    <property name="JWT_SECRET_KEY" value="your-production-secret-key-256-bits-long"/>
    <property name="JWT_EXPIRATION_MS" value="86400000"/>
</system-properties>
```

### Docker

```yaml
version: '3.8'
services:
  backend:
    image: your-backend-image
    environment:
      - JWT_SECRET_KEY=your-production-secret-key-256-bits-long
      - JWT_EXPIRATION_MS=86400000
    ports:
      - "8080:8080"
```

### Variables de Entorno en Linux

```bash
# Añadir a /etc/environment o ~/.bashrc
export JWT_SECRET_KEY="your-production-secret-key-256-bits-long"
export JWT_EXPIRATION_MS=86400000
```

---

## 🔍 Testing con Postman

### 1. Generar un Token de Prueba

Usa https://jwt.io/ para generar un token de prueba:

**Payload**:
```json
{
  "userId": 1,
  "username": "test_user",
  "email": "test@example.com",
  "role": "USUARIO",
  "activo": true,
  "sub": "test_user",
  "iat": 1705328400,
  "exp": 9999999999
}
```

**Secret**: Usa la misma clave de `JWT_SECRET_KEY`

### 2. Crear Collection en Postman

**Request: Validate Token**
- Method: `GET`
- URL: `http://localhost:8080/api/auth/validate`
- Headers:
  - Key: `Authorization`
  - Value: `Bearer {tu-token-aqui}`

**Request: Get Protected Resource**
- Method: `GET`
- URL: `http://localhost:8080/api/profesionales`
- Headers:
  - Key: `Authorization`
  - Value: `Bearer {{jwt_token}}`

---

## ❌ Manejo de Errores

### Errores Comunes

**401 Unauthorized - Token Inválido**:
```json
{
  "error": "Invalid or expired token",
  "timestamp": 1705328400000
}
```

**401 Unauthorized - Token Expirado**:
```json
{
  "error": "Invalid or expired token",
  "timestamp": 1705328400000
}
```

**401 Unauthorized - Sin Header Authorization**:
```json
{
  "error": "Missing or invalid Authorization header",
  "timestamp": 1705328400000
}
```

**401 Unauthorized - Usuario Inactivo**:
```json
{
  "error": "User account is inactive",
  "timestamp": 1705328400000
}
```

---

## 🔐 Seguridad - Mejores Prácticas

### Para el Backend

1. ✅ **NUNCA** commitear la clave secreta JWT al repositorio
2. ✅ Usar variables de entorno en producción
3. ✅ Generar claves secretas fuertes (mínimo 256 bits)
4. ✅ Validar siempre la firma del token
5. ✅ Verificar expiración del token
6. ✅ Usar HTTPS en producción
7. ✅ Implementar rate limiting
8. ✅ Logs de intentos de acceso no autorizados

### Para el Frontend

1. ✅ La clave secreta debe ser **LA MISMA** en frontend y backend
2. ✅ Guardar token en `localStorage` o `sessionStorage` (no en cookies si no tienes httpOnly)
3. ✅ Limpiar token al hacer logout
4. ✅ Manejar expiración del token (renovar o redirigir a login)
5. ✅ Usar HTTPS en producción
6. ✅ No incluir información sensible en el payload del token (es visible)

### Sincronización de Claves

**⚠️ CRÍTICO**: El frontend y backend deben usar la **misma clave secreta**:

```javascript
// Frontend
const secret = 'mi-super-clave-secreta-de-256-bits-para-jwt-tokens-2024';
```

```bash
# Backend
export JWT_SECRET_KEY="mi-super-clave-secreta-de-256-bits-para-jwt-tokens-2024"
```

---

## 📁 Archivos Creados/Modificados

```
src/main/java/
├── Annotation/
│   └── Secured.java                    # Anotación @Secured
├── Config/
│   └── JwtConfig.java                  # Configuración JWT
├── Controller/
│   └── AuthController.java             # Endpoint /api/auth/validate
├── Filter/
│   └── JwtAuthenticationFilter.java    # Filtro automático de validación
├── Service/
│   └── AuthService.java                # Servicio de validación
└── Util/
    ├── JwtUtil.java                    # Utilidades JWT
    └── PasswordUtil.java               # Utilidades de contraseñas

src/main/resources/
└── application.properties               # Configuración

pom.xml                                  # Dependencias agregadas
```

---

## 📚 Recursos

- **JWT.io**: https://jwt.io/ - Decoder y debugger de tokens
- **JJWT Documentation**: https://github.com/jwtk/jjwt
- **Jakarta EE Security**: https://jakarta.ee/specifications/security/

---

## 🚨 Troubleshooting

### Token no válido

**Causa**: Las claves secretas no coinciden entre frontend y backend

**Solución**: Verifica que `JWT_SECRET_KEY` sea idéntica en ambos lados

### Token expirado constantemente

**Causa**: Diferencia de tiempo entre servidores o `exp` mal configurado

**Solución**:
- Sincroniza relojes de servidores
- Aumenta `JWT_EXPIRATION_MS`
- En frontend, verifica que `exp` se calcule correctamente

### 401 en todos los requests

**Causa**: El filtro no está interceptando correctamente o el header no se envía

**Solución**:
- Verifica que el header sea: `Authorization: Bearer {token}` (con espacio)
- Confirma que `@Secured` está presente en el endpoint
- Revisa logs del servidor para ver errores de validación

---

## 💡 Próximos Pasos (Opcional)

1. Implementar validación de roles específicos (`@RolesAllowed`)
2. Añadir refresh tokens
3. Implementar lista negra de tokens (logout)
4. Rate limiting para prevenir ataques
5. Logging y auditoría de accesos
6. CORS configuration
7. Integración con sistema de usuarios externo
