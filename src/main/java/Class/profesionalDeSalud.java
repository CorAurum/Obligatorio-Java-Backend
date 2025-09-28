package Class;

import jakarta.persistence.*;

import java.util.List;

// =================== PROFESIONAL DE SALUD ===================
@Entity
public class profesionalDeSalud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProfesional;

    private String cedulaIdentidad;
    private String nombre;
    private String apellido;
    private String numeroRegistro;
    private String email;
    private String telefono;

    @ManyToMany
    @JoinTable(
            name = "profesional_especialidad",
            joinColumns = @JoinColumn(name = "id_profesional"),
            inverseJoinColumns = @JoinColumn(name = "id_especialidad")
    )
    private List<Especialidad> especialidades;

    @ManyToOne
    @JoinColumn(name = "id_clinica")
    private Clinica clinica;

    @OneToMany(mappedBy = "profesional", cascade = CascadeType.ALL)
    private List<documentoClinico> documentos;
}
