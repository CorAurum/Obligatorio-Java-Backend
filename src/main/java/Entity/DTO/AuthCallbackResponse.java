package Entity.DTO;

/**
 * Response from the auth callback endpoint
 */
public class AuthCallbackResponse {
    private String redirectUrl;
    private String portal;
    private OidcUserInfo userInfo;
    private String backendToken; // Our own JWT token for the backend
    private String error;

    public AuthCallbackResponse() {}

    public AuthCallbackResponse(String redirectUrl, String portal, OidcUserInfo userInfo, String backendToken) {
        this.redirectUrl = redirectUrl;
        this.portal = portal;
        this.userInfo = userInfo;
        this.backendToken = backendToken;
    }

    public AuthCallbackResponse(String error) {
        this.error = error;
    }

    // Getters and setters
    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getPortal() {
        return portal;
    }

    public void setPortal(String portal) {
        this.portal = portal;
    }

    public OidcUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(OidcUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getBackendToken() {
        return backendToken;
    }

    public void setBackendToken(String backendToken) {
        this.backendToken = backendToken;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
