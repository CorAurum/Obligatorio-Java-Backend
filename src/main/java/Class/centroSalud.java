package Class;

import Class.Usuarios.profesionalDeSalud;
import jakarta.persistence.*;
import Class.Usuarios.administrador;
import java.util.List;

// =================== CLÍNICA ===================

@Entity
public class centroSalud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String direccion;
    private String telefono;
    private String tipoInstitucion;

    // relaciones

    @ManyToOne
    @JoinColumn(name = "administrador_id")
    private administrador administrador;

    @OneToMany(mappedBy = "centroSalud")
    private List<documentoClinico> documentoClinicos;

    @OneToMany(mappedBy = "centroSalud")
    private List<profesionalDeSalud> profesionalDeSaluds;

    @OneToMany(mappedBy = "centroSalud")
    private List<politicaDeAcceso> politicaDeAccesos;

    // === Getters y Setters ===

    public Long getId() {
        return id;
    }

    public void setId(Long idClinica) {
        this.id = idClinica;
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

}