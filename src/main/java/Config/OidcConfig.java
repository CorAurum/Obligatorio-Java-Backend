package Config;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration for gub.uy OIDC integration
 */
@ApplicationScoped
public class OidcConfig {

    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String authorizeUrl;
    private String tokenUrl;
    private String scope;
    private String logoutUrl;

    public OidcConfig() {
        loadConfiguration();
    }

    private void loadConfiguration() {
        // First, try to load from application.properties
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                props.load(input);
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not load application.properties: " + e.getMessage());
        }

        // Load configuration with priority: Environment Variable > Properties File >
        // Default
        this.clientId = getConfigValue("OIDC_CLIENT_ID", props.getProperty("OIDC_CLIENT_ID"));
        this.clientSecret = getConfigValue("OIDC_CLIENT_SECRET", props.getProperty("OIDC_CLIENT_SECRET"));
        this.redirectUri = getConfigValue("OIDC_REDIRECT_URI", props.getProperty("OIDC_REDIRECT_URI"));
        this.authorizeUrl = getConfigValue("OIDC_AUTHORIZE_URL", props.getProperty("OIDC_AUTHORIZE_URL"));
        this.tokenUrl = getConfigValue("OIDC_TOKEN_URL", props.getProperty("OIDC_TOKEN_URL"));
        this.scope = getConfigValue("OIDC_SCOPE", props.getProperty("OIDC_SCOPE"));
        this.logoutUrl = getConfigValue("OIDC_LOGOUT_URL", props.getProperty("OIDC_LOGOUT_URL"));

        // Debug logging
        System.out.println("=== OIDC Configuration Loaded ===");
        System.out.println("ClientId: '" + this.clientId + "'");
        System.out.println("ClientSecret: '" + (this.clientSecret != null ? "[SET]" : "null") + "'");
        System.out.println("RedirectUri: '" + this.redirectUri + "'");
        System.out.println("AuthorizeUrl: '" + this.authorizeUrl + "'");
        System.out.println("TokenUrl: '" + this.tokenUrl + "'");
        System.out.println("Scope: '" + this.scope + "'");
        System.out.println("=================================");

        // Set defaults for missing values instead of throwing exceptions
        if (clientId == null || clientId.isEmpty()) {
            System.err.println("WARNING: OIDC_CLIENT_ID not configured, using default for development");
            clientId = "default-client-id";
        }
        if (clientSecret == null || clientSecret.isEmpty()) {
            System.err.println("WARNING: OIDC_CLIENT_SECRET not configured, using default for development");
            clientSecret = "default-client-secret";
        }
        if (redirectUri == null || redirectUri.isEmpty()) {
            System.err.println("WARNING: OIDC_REDIRECT_URI not configured, using default for development");
            redirectUri = "http://localhost:3000/api/auth/callback";
        }
        if (authorizeUrl == null || authorizeUrl.isEmpty()) {
            System.err.println("WARNING: OIDC_AUTHORIZE_URL not configured, using default for development");
            authorizeUrl = "https://auth.gub.uy/oauth2/authorize";
        }
        if (tokenUrl == null || tokenUrl.isEmpty()) {
            System.err.println("WARNING: OIDC_TOKEN_URL not configured, using default for development");
            tokenUrl = "https://auth.gub.uy/oauth2/token";
        }
        if (scope == null || scope.isEmpty()) {
            scope = "openid profile email"; // Default scope
        }
    }

    /**
     * Get configuration value with priority: Environment Variable > Properties File
     * > null
     */
    private String getConfigValue(String envVarName, String propValue) {
        String envValue = System.getenv(envVarName);
        if (envValue != null && !envValue.isEmpty()) {
            return envValue;
        }
        if (propValue != null && !propValue.isEmpty()) {
            return propValue;
        }
        return null;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getAuthorizeUrl() {
        return authorizeUrl;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public String getScope() {
        return scope;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }
}
