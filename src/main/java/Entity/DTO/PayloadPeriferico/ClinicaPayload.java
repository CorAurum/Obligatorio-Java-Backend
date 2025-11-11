package Entity.DTO.PayloadPeriferico;

public class ClinicaPayload {


    private String id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String tipoInstitucion;
    private String dominioSubdominio;

    public ClinicaPayload(String id, String nombre, String direccion, String telefono,
                          String tipoInstitucion, String dominioSubdominio) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.tipoInstitucion = tipoInstitucion;
        this.dominioSubdominio = dominioSubdominio;
    }

    // Getters y setters

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getTipoInstitucion() { return tipoInstitucion; }
    public void setTipoInstitucion(String tipoInstitucion) { this.tipoInstitucion = tipoInstitucion; }

    public String getDominioSubdominio() { return dominioSubdominio; }
    public void setDominioSubdominio(String dominioSubdominio) { this.dominioSubdominio = dominioSubdominio; }
}
