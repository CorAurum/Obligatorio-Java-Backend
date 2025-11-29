# 🧪 Postman Test Payloads - Healthcare API

This document contains JSON payloads for testing all major API endpoints in the healthcare backend system.

## 🔐 Authentication Endpoints

### 1. Initiate Login (GET - No Body Required)
```
GET https://backend.web.elasticloud.uy/api/auth/login?portal=admin
GET https://backend.web.elasticloud.uy/api/auth/login?portal=profesional
GET https://backend.web.elasticloud.uy/api/auth/login?portal=usuario
```

**Expected Response:** HTTP 302 redirect to gub.uy OIDC

### 2. Authentication Callback (GET - Called by gub.uy)
```
GET https://backend.web.elasticloud.uy/api/auth/callback/web?code=abc123&state=xyz789&portal=admin
```

**Response:**
```json
{
  "redirectUrl": "https://frontend.web.elasticloud.uy/admin-hcen",
  "portal": "admin",
  "userInfo": {
    "subject": "sub_123",
    "numeroDocumento": "12345678",
    "email": "user@gub.uy",
    "name": "Juan Pérez",
    "givenName": "Juan",
    "familyName": "Pérez",
    "preferredUsername": "juan.perez",
    "issuedAt": 1704067200,
    "expiresAt": 1704103200,
    "issuer": "https://auth.gub.uy",
    "audience": "your-client-id"
  },
  "backendToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### 3. Validate Token (GET - Authorization Header Required)
```
GET https://backend.web.elasticloud.uy/api/auth/validate
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

**Response:**
```json
{
  "userId": 123,
  "username": "12345678",
  "role": "USUARIO"
}
```

---

## 👥 User Management Endpoints

### 4. Create/Update User from Peripheral System
```
POST https://backend.web.elasticloud.uy/api/usuarios/externo
Content-Type: application/json
```

**Payload:**
```json
{
  "idLocal": "USR001",
  "centroId": "CENTRO001",
  "nombres": "María",
  "apellidos": "González",
  "fechaNacimiento": "1985-06-15",
  "sexo": "F",
  "direccion": "Av. Libertador 1234, Montevideo",
  "email": "maria.gonzalez@email.com",
  "telefono": "+598 99 123 456",
  "identificadores": [
    {
      "tipo": "CI",
      "valor": "23456789",
      "origen": "SISTEMA_LOCAL"
    },
    {
      "tipo": "PASAPORTE",
      "valor": "AB123456",
      "origen": "SISTEMA_LOCAL"
    }
  ]
}
```

**Response:**
```json
{
  "usuario": {
    "id": "USR001",
    "nombres": "María",
    "apellidos": "González",
    "email": "maria.gonzalez@email.com",
    "telefono": "+598 99 123 456",
    "fechaNacimiento": "1985-06-15",
    "sexo": "F",
    "direccion": "Av. Libertador 1234, Montevideo"
  },
  "identificadores": [
    {
      "id": 1,
      "tipo": "CI",
      "valor": "23456789",
      "origen": "SISTEMA_LOCAL"
    }
  ],
  "conflict": false,
  "message": "Usuario creado exitosamente"
}
```

### 5. List Users (GET)
```
GET https://backend.web.elasticloud.uy/api/usuarios
```

### 6. Get User by ID (GET)
```
GET https://backend.web.elasticloud.uy/api/usuarios/USR001
```

---

## 👨‍⚕️ Professional Healthcare Management

### 7. Create Professional
```
POST https://backend.web.elasticloud.uy/api/profesionales
Content-Type: application/json
```

**Payload:**
```json
{
  "id": "PROF001",
  "numeroRegistro": "MP12345",
  "nombres": "Dr. Carlos",
  "apellidos": "Rodríguez",
  "email": "carlos.rodriguez@hospital.com",
  "telefono": "+598 99 987 654",
  "fechaRegistroProfesional": "2010-03-15",
  "estado": "ACTIVO",
  "centroDeSalud": {
    "id": "CENTRO001"
  }
}
```

**Response:**
```json
{
  "id": "PROF001",
  "numeroRegistro": "MP12345",
  "nombres": "Dr. Carlos",
  "apellidos": "Rodríguez",
  "email": "carlos.rodriguez@hospital.com",
  "telefono": "+598 99 987 654",
  "fechaRegistroProfesional": "2010-03-15",
  "estado": "ACTIVO",
  "centroDeSalud": {
    "id": "CENTRO001",
    "nombre": "Hospital Central"
  }
}
```

### 8. Update Professional
```
PUT https://backend.web.elasticloud.uy/api/profesionales/PROF001
Content-Type: application/json
```

**Payload:**
```json
{
  "numeroRegistro": "MP12345",
  "nombres": "Dr. Carlos Alberto",
  "apellidos": "Rodríguez",
  "email": "carlos.rodriguez@hospital.com",
  "telefono": "+598 99 987 654",
  "estado": "ACTIVO"
}
```

### 9. List Professionals (GET)
```
GET https://backend.web.elasticloud.uy/api/profesionales
```

### 10. Get Professional by ID (GET)
```
GET https://backend.web.elasticloud.uy/api/profesionales/PROF001
```

---

## 🏥 Healthcare Center Management

### 11. Create Healthcare Center
```
POST https://backend.web.elasticloud.uy/api/CentroDeSalud
Content-Type: application/json
```

**Payload:**
```json
{
  "id": "CENTRO001",
  "nombre": "Hospital Central de Montevideo",
  "direccion": "Av. 18 de Julio 1234",
  "telefono": "+598 2 901 234",
  "email": "info@hospitalcentral.com",
  "tipo": "HOSPITAL",
  "estado": "ACTIVO"
}
```

### 12. List Healthcare Centers (GET)
```
GET https://backend.web.elasticloud.uy/api/CentroDeSalud
```

---

## 🔍 Specialty Management

### 13. Create Specialty
```
POST https://backend.web.elasticloud.uy/api/especialidades
Content-Type: application/json
```

**Payload:**
```json
{
  "id": "CARD001",
  "nombre": "Cardiología",
  "descripcion": "Especialidad médica dedicada al estudio, diagnóstico y tratamiento de enfermedades del corazón y del aparato circulatorio"
}
```

**Response:**
```json
{
  "id": "CARD001",
  "nombre": "Cardiología",
  "descripcion": "Especialidad médica dedicada al estudio, diagnóstico y tratamiento de enfermedades del corazón y del aparato circulatorio"
}
```

### 14. Update Specialty
```
PUT https://backend.web.elasticloud.uy/api/especialidades/CARD001
Content-Type: application/json
```

**Payload:**
```json
{
  "nombre": "Cardiología Intervencionista",
  "descripcion": "Especialidad médica dedicada al estudio, diagnóstico y tratamiento intervencionista de enfermedades del corazón"
}
```

### 15. List Specialties (GET)
```
GET https://backend.web.elasticloud.uy/api/especialidades
```

### 16. Get Specialty by ID (GET)
```
GET https://backend.web.elasticloud.uy/api/especialidades/CARD001
```

### 17. Delete Specialty
```
DELETE https://backend.web.elasticloud.uy/api/especialidades/CARD001
```

---

## 📋 Access Policy Management

### 18. Create Access Policy
```
POST https://backend.web.elasticloud.uy/api/politicas
Content-Type: application/json
```

**Payload:**
```json
{
  "usuarioId": "USR001",
  "centroId": "CENTRO001",
  "especialidades": ["CARD001", "NEUR001"],
  "vigenciaHasta": "2025-12-31"
}
```

**Response:**
```json
{
  "id": "POL001",
  "usuario": {
    "id": "USR001",
    "nombres": "María",
    "apellidos": "González"
  },
  "centroDeSalud": {
    "id": "CENTRO001",
    "nombre": "Hospital Central de Montevideo"
  },
  "especialidades": [
    {
      "id": "CARD001",
      "nombre": "Cardiología"
    },
    {
      "id": "NEUR001",
      "nombre": "Neurología"
    }
  ],
  "fechaCreacion": "2024-01-15T10:30:00",
  "vigenciaHasta": "2025-12-31",
  "estado": "ACTIVA"
}
```

### 19. List Policies by User (GET)
```
GET https://backend.web.elasticloud.uy/api/politicas/usuario/USR001
```

### 20. Revoke Policy
```
PUT https://backend.web.elasticloud.uy/api/politicas/POL001/revocar
```

### 21. Verify Access Policy (GET)
```
GET https://backend.web.elasticloud.uy/api/politicas/verificar?usuarioId=USR001&centroId=CENTRO001&especialidadId=CARD001
```

### 22. Get Clinical History (GET)
```
GET https://backend.web.elasticloud.uy/api/politicas/historiaClinica?usuarioId=USR001&centroId=CENTRO001
```

---

## 📄 Clinical Document Management

### 23. Create Clinical Document (External)
```
POST https://backend.web.elasticloud.uy/api/documentoClinico/externo
Content-Type: application/json
```

**Payload:**
```json
{
  "usuarioId": "USR001",
  "profesionalId": "PROF001",
  "centroId": "CENTRO001",
  "especialidadId": "CARD001",
  "tipo": "CONSULTA",
  "titulo": "Consulta Cardiología - Dolor en pecho",
  "contenido": "Paciente presenta dolor torácico atípico. Se realiza electrocardiograma y ecocardiograma. Resultados dentro de parámetros normales. Se recomienda seguimiento ambulatorio.",
  "diagnostico": "Dolor torácico atípico - causa no cardíaca",
  "tratamiento": "Analgésicos comunes, seguimiento en 2 semanas",
  "recomendaciones": "Mantener estilo de vida saludable, controlar factores de riesgo cardiovascular"
}
```

**Response:**
```json
{
  "id": "DOC001",
  "usuario": {
    "id": "USR001",
    "nombres": "María",
    "apellidos": "González"
  },
  "profesional": {
    "id": "PROF001",
    "nombres": "Dr. Carlos",
    "apellidos": "Rodríguez"
  },
  "centroDeSalud": {
    "id": "CENTRO001",
    "nombre": "Hospital Central"
  },
  "especialidad": {
    "id": "CARD001",
    "nombre": "Cardiología"
  },
  "tipo": "CONSULTA",
  "titulo": "Consulta Cardiología - Dolor en pecho",
  "estado": "ACTIVO",
  "fechaCreacion": "2024-01-15T14:30:00",
  "fechaUltimaModificacion": "2024-01-15T14:30:00"
}
```

### 24. List Documents by User (GET)
```
GET https://backend.web.elasticloud.uy/api/documentoClinico/usuario/USR001
```

### 25. List Documents by User (DTO format) (GET)
```
GET https://backend.web.elasticloud.uy/api/documentoClinico/usuarioDTO/USR001
```

### 26. Get Document by ID (GET)
```
GET https://backend.web.elasticloud.uy/api/documentoClinico/DOC001
```

### 27. Get Document Details (GET)
```
GET https://backend.web.elasticloud.uy/api/documentoClinico/DOC001/detalle
```

### 28. List All Documents (GET)
```
GET https://backend.web.elasticloud.uy/api/documentoClinico/todos
```

---

## 🔑 Access Request Management

### 29. Create Access Request
```
POST https://backend.web.elasticloud.uy/api/acceso/solicitud
Content-Type: application/json
```

**Payload:**
```json
{
  "usuarioId": "USR001",
  "profesionalId": "PROF001",
  "politicaId": "POL001",
  "tipoAcceso": "LECTURA",
  "justificacion": "Necesito acceder al historial clínico del paciente para consulta especializada en cardiología"
}
```

### 30. Approve Access Request
```
PUT https://backend.web.elasticloud.uy/api/acceso/solicitud/REQ001/aprobar
```

### 31. Reject Access Request
```
PUT https://backend.web.elasticloud.uy/api/acceso/solicitud/REQ001/rechazar
```

### 32. List Pending Requests (GET)
```
GET https://backend.web.elasticloud.uy/api/acceso/solicitudes/pendientes
```

### 33. List Pending Requests by User (GET)
```
GET https://backend.web.elasticloud.uy/api/acceso/solicitudes/pendientes/USR001
```

---

## 📊 Access Log Management

### 34. Get Access Logs (GET)
```
GET https://backend.web.elasticloud.uy/api/accesos
```

---

## 👑 Administrator Management

### 35. Create Administrator
```
POST https://backend.web.elasticloud.uy/api/administradores
Content-Type: application/json
```

**Payload:**
```json
{
  "cedula": "34567890",
  "nombres": "Ana",
  "apellidos": "Martínez",
  "email": "ana.martinez@salud.gub.uy",
  "telefono": "+598 99 555 666",
  "cargo": "Jefa de Sistema de Salud",
  "fechaAlta": "2020-01-15T08:00:00",
  "activo": true
}
```

### 36. Update Administrator
```
PUT https://backend.web.elasticloud.uy/api/administradores/1
Content-Type: application/json
```

**Payload:**
```json
{
  "cedula": "34567890",
  "nombres": "Ana María",
  "apellidos": "Martínez",
  "email": "ana.martinez@salud.gub.uy",
  "telefono": "+598 99 555 666",
  "cargo": "Directora de Sistema de Salud",
  "activo": true
}
```

### 37. List Administrators (GET)
```
GET https://backend.web.elasticloud.uy/api/administradores
```

### 38. Get Administrator by ID (GET)
```
GET https://backend.web.elasticloud.uy/api/administradores/1
```

### 39. Get Administrator by Cedula (GET)
```
GET https://backend.web.elasticloud.uy/api/administradores/cedula/34567890
```

### 40. Delete Administrator
```
DELETE https://backend.web.elasticloud.uy/api/administradores/1
```

---

## 🔍 User Identifier Management

### 41. Search by Cedula (GET)
```
GET https://backend.web.elasticloud.uy/api/identificadores/usuario/ci/12345678
```

---

## 📱 PDI (Personal Data Interface) Integration

### 42. Get Person Data by Document (GET)
```
GET https://backend.web.elasticloud.uy/api/pdi/persona/12345678
```

---

## 📚 API Documentation

### 43. Swagger UI (GET)
```
GET https://backend.web.elasticloud.uy/api/swagger
```

### 44. OpenAPI JSON (GET)
```
GET https://backend.web.elasticloud.uy/api/openapi.json
```

---

## 🧪 Testing Tips

1. **Always include proper headers:**
   ```
   Content-Type: application/json
   Authorization: Bearer {your-jwt-token}
   ```

2. **Test authentication flow first** to get valid JWT tokens

3. **Use descriptive IDs** in test data for easy identification

4. **Test error scenarios** by sending invalid data or missing required fields

5. **Verify relationships** between entities (users, professionals, centers, specialties)

6. **Test all CRUD operations** for each resource type

7. **Validate business logic** like access policies and document permissions

## 🔄 Common Response Patterns

**Success Response:**
```json
{
  "id": "RESOURCE_ID",
  "message": "Operation completed successfully",
  // ... additional data
}
```

**Error Response:**
```json
{
  "error": "Error message description",
  "timestamp": 1704067200000
}
```

**Validation Error:**
```json
{
  "error": "Validation failed: field 'email' is required",
  "timestamp": 1704067200000
}
```

**Conflict Response:**
```json
{
  "conflict": true,
  "message": "Resource already exists",
  "existingResource": { /* ... */ }
}
```
