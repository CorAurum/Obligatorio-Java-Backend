package Util;

import jakarta.enterprise.context.ApplicationScoped;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class PasswordUtil {

    private static final int BCRYPT_ROUNDS = 12;

    /**
     * Hash a plain text password using BCrypt
     */
    public String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_ROUNDS));
    }

    /**
     * Verify if a plain password matches a hashed password
     */
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            System.err.println("Error verifying password: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if password meets minimum security requirements
     */
    public boolean isPasswordValid(String password) {
        if (password == null) {
            return false;
        }

        // Minimum 8 characters
        if (password.length() < 8) {
            return false;
        }

        // At least one digit
        if (!password.matches(".*\\d.*")) {
            return false;
        }

        // At least one letter
        if (!password.matches(".*[a-zA-Z].*")) {
            return false;
        }

        return true;
    }

    /**
     * Get password validation error message
     */
    public String getPasswordValidationMessage() {
        return "Password must be at least 8 characters long and contain at least one letter and one number";
    }
}
