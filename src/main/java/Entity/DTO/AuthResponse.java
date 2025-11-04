package Entity.DTO;

import Entity.UsuarioAuth;

public class AuthResponse {
    private String token;
    private String username;
    private String email;
    private UsuarioAuth.Role role;
    private Long expiresIn;

    public AuthResponse() {}

    public AuthResponse(String token, String username, String email, UsuarioAuth.Role role, Long expiresIn) {
        this.token = token;
        this.username = username;
        this.email = email;
        this.role = role;
        this.expiresIn = expiresIn;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UsuarioAuth.Role getRole() {
        return role;
    }

    public void setRole(UsuarioAuth.Role role) {
        this.role = role;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
