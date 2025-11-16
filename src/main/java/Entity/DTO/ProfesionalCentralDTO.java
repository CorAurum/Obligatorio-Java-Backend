package Entity.DTO;

import java.util.List;

public class ProfesionalCentralDTO {
    private String id;
    private String numeroRegistro;
    private String nombres;
    private String apellidos;
    private String email;
    private String telefono;
    private List<String> especialidadesIds;

    public ProfesionalCentralDTO() {}
    // getters / setters (omito por brevedad, idénticos a los del periférico)
    // ...
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNumeroRegistro() { return numeroRegistro; }
    public void setNumeroRegistro(String numeroRegistro) { this.numeroRegistro = numeroRegistro; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public List<String> getEspecialidadesIds() { return especialidadesIds; }
    public void setEspecialidadesIds(List<String> especialidadesIds) { this.especialidadesIds = especialidadesIds; }
}
