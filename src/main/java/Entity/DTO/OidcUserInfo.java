package Entity.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User information extracted from gub.uy id_token JWT
 */
public class OidcUserInfo {
    @JsonProperty("sub")
    private String subject;

    @JsonProperty("numero_documento")
    private String numeroDocumento;

    @JsonProperty("email")
    private String email;

    @JsonProperty("name")
    private String name;

    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("family_name")
    private String familyName;

    @JsonProperty("preferred_username")
    private String preferredUsername;

    @JsonProperty("iat")
    private Long issuedAt;

    @JsonProperty("exp")
    private Long expiresAt;

    @JsonProperty("iss")
    private String issuer;

    @JsonProperty("aud")
    private String audience;

    @JsonProperty("auth_time")
    private Long authTime;

    // Additional gub.uy specific claims
    @JsonProperty("nombre_completo")
    private String nombreCompleto;

    @JsonProperty("primer_nombre")
    private String primerNombre;

    @JsonProperty("segundo_nombre")
    private String segundoNombre;

    @JsonProperty("primer_apellido")
    private String primerApellido;

    @JsonProperty("segundo_apellido")
    private String segundoApellido;

    @JsonProperty("pais_documento")
    private Object paisDocumento;

    @JsonProperty("tipo_documento")
    private Object tipoDocumento;

    @JsonProperty("email_verified")
    private Boolean emailVerified;

    @JsonProperty("rid")
    private String rid;

    @JsonProperty("nid")
    private String nid;

    @JsonProperty("ae")
    private String ae;

    @JsonProperty("amr")
    private String[] amr;

    @JsonProperty("acr")
    private String acr;

    @JsonProperty("at_hash")
    private String atHash;

    @JsonProperty("uid")
    private String uid;

    @JsonProperty("idp")
    private String idp;

    public OidcUserInfo() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getPreferredUsername() {
        return preferredUsername;
    }

    public void setPreferredUsername(String preferredUsername) {
        this.preferredUsername = preferredUsername;
    }

    public Long getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Long issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public Long getAuthTime() {
        return authTime;
    }

    public void setAuthTime(Long authTime) {
        this.authTime = authTime;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public Object getPaisDocumento() {
        return paisDocumento;
    }

    public void setPaisDocumento(Object paisDocumento) {
        this.paisDocumento = paisDocumento;
    }

    public Object getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(Object tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getAe() {
        return ae;
    }

    public void setAe(String ae) {
        this.ae = ae;
    }

    public String[] getAmr() {
        return amr;
    }

    public void setAmr(String[] amr) {
        this.amr = amr;
    }

    public String getAcr() {
        return acr;
    }

    public void setAcr(String acr) {
        this.acr = acr;
    }

    public String getAtHash() {
        return atHash;
    }

    public void setAtHash(String atHash) {
        this.atHash = atHash;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIdp() {
        return idp;
    }

    public void setIdp(String idp) {
        this.idp = idp;
    }
}
