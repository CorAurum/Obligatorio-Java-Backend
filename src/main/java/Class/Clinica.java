package Class;

import jakarta.persistence.*;

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
}
