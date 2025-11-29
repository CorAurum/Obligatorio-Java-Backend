# Backend Authentication Flow with gub.uy - Frontend Integration Guide

## Overview

This guide explains how the Java backend handles authentication with gub.uy and provides guidance for the Next.js frontend team to fix authentication issues.

## Current Backend Authentication Architecture

### Key Points
- **External Authentication**: The backend does NOT handle login/registration locally. All authentication is delegated to gub.uy (Uruguayan government identity provider).
- **OIDC Flow**: Uses OpenID Connect (OIDC) standard with Authorization Code flow.
- **Cross-Domain Design**: Backend and frontend are on different domains, so direct cookie setting is not possible.
- **Dual Token System**: Receives tokens from gub.uy and generates its own JWT for backend authorization.

## Backend Authentication Flow

### 1. Login Initiation (`/api/auth/login`)
**Endpoint**: `GET /api/auth/login?portal=admin|profesional|usuario`

**What it does:**
- Validates the `portal` parameter (admin/profesional/usuario)
- Generates a CSRF protection `state` parameter
- Redirects to gub.uy authorization URL with proper OIDC parameters

**Redirect URL built:**
```
https://auth-testing.iduruguay.gub.uy/oidc/v1/authorize
  ?client_id={CLIENT_ID}
  &redirect_uri={BACKEND_CALLBACK_URL}
  &response_type=code
  &scope=openid document personal_info auth_info
  &state={RANDOM_STATE}
  &portal={PORTAL_TYPE}
  &prompt=login
```

### 2. gub.uy Authentication
- User authenticates with their government credentials
- gub.uy validates identity and redirects back to backend callback

### 3. Backend Callback Processing (`/api/auth/callback/web`)
**Endpoint**: `GET /api/auth/callback/web?code={AUTH_CODE}&state={STATE}&portal={PORTAL}`

**Complete flow:**
1. **Validate parameters**: Checks for authorization code, validates state, validates portal
2. **Exchange code for tokens**: Calls gub.uy token endpoint to exchange auth code for tokens
3. **Decode user info**: Extracts user information from the `id_token` JWT
4. **Admin validation**: For admin portal, checks if user has admin privileges
5. **Generate backend JWT**: Creates internal JWT token for API authorization
6. **Redirect to frontend**: Returns HTML page that redirects to appropriate frontend URL

### 4. Token Exchange Details

**Request to gub.uy:**
```
POST https://auth-testing.iduruguay.gub.uy/oidc/v1/token
Authorization: Basic {BASE64_ENCODED_CLIENT_CREDENTIALS}
Content-Type: application/x-www-form-urlencoded

grant_type=authorization_code
&code={AUTH_CODE}
&redirect_uri={BACKEND_REDIRECT_URI}
```

**Response from gub.uy:**
```json
{
  "id_token": "JWT_WITH_USER_INFO",
  "access_token": "ACCESS_TOKEN",
  "token_type": "Bearer",
  "expires_in": 3600,
  "refresh_token": "REFRESH_TOKEN"
}
```

### 5. User Information Extraction

The `id_token` contains rich user information from gub.uy:

```json
{
  "sub": "subject-identifier",
  "numero_documento": "12345678",
  "email": "user@email.com",
  "name": "Full Name",
  "primer_nombre": "First Name",
  "primer_apellido": "Last Name",
  "tipo_documento": "CI",
  "pais_documento": "UY",
  "email_verified": true
}
```

## Frontend Integration Issues & Fixes

### Issue 1: Callback Response Format Mismatch

**Frontend Expectation:**
- Frontend callback route expects JSON response with `authData` containing user info and backend token

**Backend Reality:**
- Backend returns HTML redirect page that redirects with URL parameters:
  ```
  {FRONTEND_URL}/{PORTAL}?auth_success=true&portal={PORTAL}&user_id={CEDULA}&backend_token={JWT}
  ```

**Fix Needed:**
The frontend callback should handle URL parameters instead of expecting JSON response.

### Issue 2: Token Handling

**Frontend Current Approach:**
- Stores backend token in iron-session
- Uses session for authentication state

**Backend Design:**
- Passes JWT token via URL parameter for cross-domain compatibility
- Expects JWT to be sent in `Authorization: Bearer {token}` header for API calls

**Fix Needed:**
Frontend should extract the `backend_token` from URL parameters and store it appropriately.

### Issue 3: Callback Flow

**Frontend Current Flow:**
```
gub.uy → Backend → Frontend Callback Route → Exchange code → Set session → Redirect
```

**Backend Actual Flow:**
```
gub.uy → Backend Callback (processes everything) → HTML Redirect to Frontend with auth data
```

**Fix Needed:**
The frontend doesn't need a separate callback API route. The backend handles the complete OAuth flow and redirects directly to the frontend with authentication data.

## Recommended Frontend Flow Fix

### 1. Simplified Login Flow

```typescript
// app/page.tsx - No changes needed
// app/api/auth/login/route.ts - No changes needed
```

### 2. Remove Unnecessary Callback Route

**Delete or modify:** `app/api/auth/callback/route.ts`

The backend handles the complete callback flow. The frontend should handle the redirected URL with parameters instead.

### 3. Handle Authentication Redirect

**Modify:** `app/auth-redirect/page.tsx` (or create if doesn't exist)

```typescript
// app/auth-redirect/page.tsx
import { redirect } from 'next/navigation';
import { getIronSession } from 'iron-session';
import { sessionOptions } from '@/lib/session';

export default async function AuthRedirect({
  searchParams,
}: {
  searchParams: { [key: string]: string | string[] | undefined };
}) {
  const session = await getIronSession(cookies(), sessionOptions);

  // Extract auth data from URL parameters (set by backend)
  const authSuccess = searchParams.auth_success === 'true';
  const portal = searchParams.portal as string;
  const userId = searchParams.user_id as string;
  const backendToken = searchParams.backend_token as string;

  if (!authSuccess || !backendToken) {
    redirect('/?error=auth_failed');
  }

  // Store in session
  session.user = {
    id_token: backendToken,
    access_token: backendToken,
    token_type: 'Bearer',
    expires_in: 86400
  };
  session.userInfo = {
    id: userId,
    // Add other user info if needed
  };
  session.isLoggedIn = true;

  await session.save();

  // Redirect to appropriate portal
  switch (portal) {
    case 'admin':
      redirect('/admin-hcen');
    case 'profesional':
      redirect('/profesional');
    default:
      redirect('/usuario-salud');
  }
}
```

### 4. API Authentication

**Current Approach:** Frontend doesn't send auth tokens to backend API calls.

**Correct Approach:** Include the backend JWT in API requests:

```typescript
// lib/api.ts or similar
export async function apiRequest(endpoint: string, options: RequestInit = {}) {
  const session = await getIronSession(cookies(), sessionOptions);

  const headers = {
    'Content-Type': 'application/json',
    ...options.headers,
  };

  if (session.isLoggedIn && session.user?.access_token) {
    headers['Authorization'] = `Bearer ${session.user.access_token}`;
  }

  return fetch(`${process.env.BACKEND_URL}${endpoint}`, {
    ...options,
    headers,
  });
}
```

## Configuration Requirements

### Backend Environment Variables
```bash
# OIDC Configuration
OIDC_CLIENT_ID=890192
OIDC_CLIENT_SECRET=457d52f181bf11804a3365b49ae4d29a2e03bbabe74997a2f510b179
OIDC_REDIRECT_URI=https://backend.web.elasticloud.uy/api/auth/callback/web
OIDC_AUTHORIZE_URL=https://auth-testing.iduruguay.gub.uy/oidc/v1/authorize
OIDC_TOKEN_URL=https://auth-testing.iduruguay.gub.uy/oidc/v1/token
OIDC_SCOPE=openid document personal_info auth_info

# Frontend Configuration
FRONTEND_BASE_URL=https://hcen-central.vercel.app
```

### Frontend Environment Variables
```bash
NEXT_PUBLIC_BACKEND_URL=https://backend.web.elasticloud.uy
```

## Backend API Endpoints

### Authentication Endpoints
- `GET /api/auth/login?portal={type}` - Initiate login
- `GET /api/auth/callback/web` - Handle callback (internal)
- `GET /api/auth/validate` - Validate JWT token
- `GET /api/auth/callback/mobile` - Mobile app callback

### Protected API Access
All API endpoints requiring authentication use the `@Secured` annotation and expect:
```
Authorization: Bearer {BACKEND_JWT_TOKEN}
```

## Testing the Flow

### 1. Test Login Initiation
```bash
curl "http://localhost:8080/api/auth/login?portal=usuario"
# Should redirect to gub.uy
```

### 2. Test Token Validation
```bash
curl -H "Authorization: Bearer {JWT_TOKEN}" \
     http://localhost:8080/api/auth/validate
```

### 3. Test Admin Access
```bash
curl "http://localhost:8080/api/auth/login?portal=admin"
# Should check admin privileges after authentication
```

## Common Issues & Troubleshooting

### Issue: "OIDC not properly configured"
- Check environment variables are set correctly
- Verify OIDC credentials match gub.uy registration

### Issue: "Failed to exchange code for tokens"
- Check client credentials
- Verify redirect URI matches gub.uy configuration
- Check network connectivity to gub.uy

### Issue: "Invalid id_token"
- Verify JWT decoding is working
- Check token structure from gub.uy

### Issue: Frontend not receiving auth data
- Backend redirects with URL parameters
- Frontend must handle `auth_success`, `portal`, `user_id`, `backend_token` parameters

## Security Considerations

1. **State Parameter**: Used for CSRF protection
2. **HTTPS Only**: All redirects must use HTTPS in production
3. **Token Expiration**: Backend JWT expires in 24 hours (configurable)
4. **Secure Storage**: Never store tokens in localStorage, use httpOnly cookies when possible
5. **Cross-Domain**: Current design handles cross-domain authentication properly

## Next Steps

1. **Remove unnecessary frontend callback route**
2. **Implement URL parameter handling in auth-redirect page**
3. **Update API calls to include Authorization header**
4. **Test the complete flow end-to-end**
5. **Verify admin portal access restrictions work**

This should resolve the authentication issues between the frontend and backend.
