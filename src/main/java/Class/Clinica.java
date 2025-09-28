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

    @OneToMany(mappedBy = "clinica", cascade = CascadeType.ALL)
    private List<profesionalDeSalud> profesionales;

    @OneToMany(mappedBy = "clinica", cascade = CascadeType.ALL)
    private List<documentoClinico> documentos;

    public void setNombre(String clinicaCentral) {
    }

    public String getNombre() {
        return nombre;
    }

    public void setDireccion(String clinicaCentral) {}
    public String getDireccion() {
        return direccion;
    }

    public void setTelefono(String clinicaCentral) {

    }

    public String getTelefono() {
        return telefono;
    }

    public void setTipoInstitucion(String clinicaCentral) {}

    public String getTipoInstitucion() {
        return tipoInstitucion;
    }


}
