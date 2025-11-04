package Config;

import jakarta.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@ApplicationScoped
public class JwtConfig {

    private String secretKey;
    private long expirationTime;

    public JwtConfig() {
        loadConfiguration();
    }

    private void loadConfiguration() {
        Properties props = new Properties();

        // Try to load from application.properties
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                props.load(input);
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not load application.properties: " + e.getMessage());
        }

        // Get JWT secret key from environment variable or properties file
        // Priority: Environment Variable > Properties File > Default
        this.secretKey = System.getenv("JWT_SECRET_KEY");
        if (this.secretKey == null || this.secretKey.isEmpty()) {
            this.secretKey = props.getProperty("jwt.secret.key");
        }
        if (this.secretKey == null || this.secretKey.isEmpty()) {
            // IMPORTANTE: En producción, siempre usar una variable de entorno
            System.err.println("WARNING: Using default JWT secret key. Set JWT_SECRET_KEY environment variable in production!");
            this.secretKey = "default-secret-key-change-this-in-production-must-be-at-least-256-bits-long";
        }

        // Get JWT expiration time (in milliseconds)
        String expirationEnv = System.getenv("JWT_EXPIRATION_MS");
        if (expirationEnv != null && !expirationEnv.isEmpty()) {
            this.expirationTime = Long.parseLong(expirationEnv);
        } else {
            String expirationProp = props.getProperty("jwt.expiration.ms", "86400000"); // Default: 24 hours
            this.expirationTime = Long.parseLong(expirationProp);
        }
    }

    public String getSecretKey() {
        return secretKey;
    }

    public long getExpirationTime() {
        return expirationTime;
    }
}
