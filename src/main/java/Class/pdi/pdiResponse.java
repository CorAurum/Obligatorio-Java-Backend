package Class.pdi;

import java.util.ArrayList;
import java.util.List;

public class pdiResponse {

    private String codTipoDocumento;
    private String nroDocumento;
    private String nombre1;
    private String nombre2;
    private String apellido1;
    private String apellido2;
    private String sexo;
    private String fechaNacimiento;
    private String codNacionalidad;
    private String nombreEnCedula;

    private String errorCode;
    private String errorDescription;

    private List<String> warnings = new ArrayList<>();

    // No-args constructor
    public pdiResponse() {
    }

    // All-args constructor
    public pdiResponse(String codTipoDocumento, String nroDocumento, String nombre1, String nombre2,
                       String apellido1, String apellido2, String sexo, String fechaNacimiento,
                       String codNacionalidad, String nombreEnCedula, String errorCode,
                       String errorDescription, List<String> warnings) {
        this.codTipoDocumento = codTipoDocumento;
        this.nroDocumento = nroDocumento;
        this.nombre1 = nombre1;
        this.nombre2 = nombre2;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        this.codNacionalidad = codNacionalidad;
        this.nombreEnCedula = nombreEnCedula;
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
        this.warnings = warnings != null ? warnings : new ArrayList<>();
    }

    // Getters and Setters
    public String getCodTipoDocumento() {
        return codTipoDocumento;
    }

    public void setCodTipoDocumento(String codTipoDocumento) {
        this.codTipoDocumento = codTipoDocumento;
    }

    public String getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
    }

    public String getNombre1() {
        return nombre1;
    }

    public void setNombre1(String nombre1) {
        this.nombre1 = nombre1;
    }

    public String getNombre2() {
        return nombre2;
    }

    public void setNombre2(String nombre2) {
        this.nombre2 = nombre2;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCodNacionalidad() {
        return codNacionalidad;
    }

    public void setCodNacionalidad(String codNacionalidad) {
        this.codNacionalidad = codNacionalidad;
    }

    public String getNombreEnCedula() {
        return nombreEnCedula;
    }

    public void setNombreEnCedula(String nombreEnCedula) {
        this.nombreEnCedula = nombreEnCedula;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    // Custom methods
    public void setError(String code, String description) {
        this.errorCode = code;
        this.errorDescription = description;
    }

    public void addWarning(String code, String description) {
        this.warnings.add(code + ": " + description);
    }
}
