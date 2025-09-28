package Class;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// =================== CLÍNICA ===================

@Entity
public class Clinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idClinica;

    private String nombre;
    private String direccion;
    private String telefono;
    private String tipoInstitucion;

    @OneToMany(mappedBy = "clinica", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<profesionalDeSalud> profesionales;

    @OneToMany(mappedBy = "clinica", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<documentoClinico> documentos;

    // === Getters y Setters ===

    public Long getIdClinica() {
        return idClinica;
    }

    public void setIdClinica(Long idClinica) {
        this.idClinica = idClinica;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipoInstitucion() {
        return tipoInstitucion;
    }

    public void setTipoInstitucion(String tipoInstitucion) {
        this.tipoInstitucion = tipoInstitucion;
    }

    public List<profesionalDeSalud> getProfesionales() {
        return profesionales;
    }

    public void setProfesionales(List<profesionalDeSalud> profesionales) {
        this.profesionales = profesionales;
    }

    public List<documentoClinico> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<documentoClinico> documentos) {
        this.documentos = documentos;
    }
}