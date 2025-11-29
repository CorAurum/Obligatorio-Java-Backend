# 🔐 Authentication Flow Update - Backend Implementation

## 🚨 **IMPORTANT UPDATE**: Authentication Flow Moved to Backend

The OIDC authentication flow has been **completely moved from the frontend to the backend**. This provides better security, centralized authentication logic, and simplified frontend implementation.

---

## 📋 **What Changed**

### **Before** (Frontend-Only Flow)
```
Frontend → gub.uy → Frontend → Token Exchange → JWT Decode → Portal Redirect
```

### **After** (Backend Flow)
```
Frontend → Backend → gub.uy → Backend → Complete Auth Flow → Frontend Response
```

---

## 🔄 **New Authentication Flow**

### **1. Login Initiation**
**Frontend Code:**
```typescript
// Instead of direct gub.uy redirect, call backend
const initiateLogin = async (portal: 'admin' | 'usuario' | 'profesional') => {
  // Simply redirect to backend login endpoint
  window.location.href = `https://backend.web.elasticloud.uy/api/auth/login?portal=${portal}`;
};
```

**Backend Endpoint:**
- **URL:** `GET https://backend.web.elasticloud.uy/api/auth/login?portal={admin|usuario|profesional}`
- **Purpose:** Redirects to gub.uy with proper OIDC parameters
- **Response:** HTTP 302 redirect to gub.uy

### **2. Callback Handling**
**Backend Endpoint:**
- **URL:** `GET https://backend.web.elasticloud.uy/api/auth/callback/web`
- **Purpose:** Handles complete OIDC flow, returns JSON response

**Backend Response Format:**
```typescript
interface AuthCallbackResponse {
  redirectUrl: string;           // Where to redirect the user
  portal: string;               // 'admin' | 'usuario' | 'profesional'
  userInfo: OidcUserInfo;       // Decoded user information
  backendToken?: string;        // Your own JWT token for API calls
  error?: string;              // Error message if authentication failed
}

interface OidcUserInfo {
  subject: string;
  numeroDocumento: string;      // User's CI/Cedula
  email: string;
  name: string;
  givenName?: string;
  familyName?: string;
  preferredUsername?: string;
  issuedAt?: number;
  expiresAt?: number;
  issuer?: string;
  audience?: string;
}
```

---

## 🛠️ **Frontend Implementation Guide**

### **Step 1: Update Login Buttons**
Replace your existing OIDC login logic:

```typescript
// OLD CODE - Remove this
const initiateLogin = async (portal: string) => {
  const session = await getIronSession<SessionData>(cookies(), { password: SESSION_SECRET, cookieName: 'auth-session' });
  session.intendedPortal = portal;
  await session.save();

  const authUrl = buildGubUyAuthUrl(portal);
  redirect(authUrl);
};

// NEW CODE - Use this
const initiateLogin = (portal: 'admin' | 'usuario' | 'profesional') => {
  window.location.href = `https://backend.web.elasticloud.uy/api/auth/login?portal=${portal}`;
};
```

### **Step 2: Update Callback Route**
Replace your callback route (`/api/auth/callback`) with a simple redirect handler:

```typescript
// app/api/auth/callback/route.ts
export async function GET(request: NextRequest) {
  const searchParams = request.nextUrl.searchParams;
  const code = searchParams.get('code');
  const state = searchParams.get('state');
  const portal = searchParams.get('portal') || 'usuario';

  if (!code) {
    // Handle error - redirect to error page
    return NextResponse.redirect(new URL('/?error=no_code', request.url));
  }

  try {
    // Call backend callback endpoint
    const backendResponse = await fetch(`https://backend.web.elasticloud.uy/api/auth/callback/web?code=${code}&state=${state}&portal=${portal}`, {
      method: 'GET',
      headers: {
        'Accept': 'application/json'
      }
    });

    const authData: AuthCallbackResponse = await backendResponse.json();

    if (!backendResponse.ok || authData.error) {
      // Handle authentication error
      const errorMsg = authData.error || 'Authentication failed';
      return NextResponse.redirect(new URL(`/?error=${encodeURIComponent(errorMsg)}`, request.url));
    }

    // Create session with backend token and user info
    const session = await getIronSession<SessionData>(await cookies(), {
      password: process.env.SESSION_SECRET!,
      cookieName: 'auth-session',
    });

    session.user = {
      id_token: authData.backendToken || '', // Use backend token instead of gub.uy token
      access_token: authData.backendToken || '',
      token_type: 'Bearer',
      expires_in: 86400, // 24 hours
    };
    session.userInfo = authData.userInfo; // Store decoded user info
    session.isLoggedIn = true;
    await session.save();

    // Redirect to the URL provided by backend
    return NextResponse.redirect(authData.redirectUrl);

  } catch (error) {
    console.error('Callback error:', error);
    return NextResponse.redirect(new URL('/?error=callback_failed', request.url));
  }
}
```

### **Step 3: Update User Info Route**
Simplify your user info route since backend now handles JWT decoding:

```typescript
// app/api/auth/user/route.ts
export async function GET() {
  const session = await getIronSession<SessionData>(await cookies(), {
    password: process.env.SESSION_SECRET!,
    cookieName: 'auth-session',
  });

  if (!session.isLoggedIn || !session.userInfo) {
    return NextResponse.json({ error: 'Not authenticated' }, { status: 401 });
  }

  // Return the user info that backend already decoded
  return NextResponse.json(session.userInfo);
}
```

### **Step 4: Update Admin Verification**
Remove the admin check call - backend now handles this:

```typescript
// OLD CODE - Remove this
const admin = await backendAPI.checkIsAdmin(cedula);

// NEW CODE - Admin verification happens automatically in backend callback
// If user is not admin, backend returns error and redirects to /?error=not_admin
```

---

## 🔒 **Security Improvements**

1. **OIDC Flow on Backend**: More secure, server-side token handling
2. **Backend JWT Tokens**: Your own signed tokens instead of exposing gub.uy tokens to frontend
3. **Centralized Auth Logic**: Single source of truth for authentication
4. **Admin Verification**: Server-side admin checks before allowing access

---

## 📝 **Environment Variables**

Update your frontend `.env` file:

```env
# Remove these OIDC variables (now handled by backend)
# OIDC_CLIENT_ID=...
# OIDC_CLIENT_SECRET=...
# OIDC_REDIRECT_URI=...
# OIDC_AUTHORIZE_URL=...
# OIDC_TOKEN_URL=...
# OIDC_SCOPE=...

# Keep these for session management
SESSION_SECRET=your_session_secret

# Add backend URL
BACKEND_URL=https://backend.web.elasticloud.uy
```

---

## 🧪 **Testing Checklist**

- [ ] Login buttons redirect to backend
- [ ] Backend redirects to gub.uy properly
- [ ] gub.uy redirects back to backend callback
- [ ] Backend processes authentication and returns correct response
- [ ] Frontend receives redirect URL and user info
- [ ] Session is created with backend token
- [ ] User is redirected to correct portal
- [ ] Admin verification works for admin portal
- [ ] Non-admin users are blocked from admin portal
- [ ] Error handling works for failed authentication

---

## 🚀 **Migration Steps**

1. **Deploy Backend**: Ensure backend is deployed with new authentication endpoints
2. **Update Frontend**: Replace authentication logic as described above
3. **Test Each Portal**: Test admin, profesional, and usuario portals
4. **Verify Admin Access**: Ensure only authorized admins can access admin portal
5. **Update Documentation**: Update any frontend documentation that referenced the old flow

---

## ❓ **Questions?**

If you encounter any issues:

1. Check backend logs for authentication errors
2. Verify environment variables are set correctly
3. Test backend endpoints directly with curl/Postman
4. Ensure frontend callback route matches backend expectations

**Backend Team Contact:** For backend-related authentication issues
**Frontend Team Contact:** For frontend integration questions

---

*This update provides a more secure and maintainable authentication architecture. The frontend is now simplified while the backend handles all OIDC complexity.*
