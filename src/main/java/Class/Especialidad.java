package Class;

import jakarta.persistence.*;
import Class.Usuarios.profesionalDeSalud;
import java.util.List;

// =================== ESPECIALIDAD ===================
@Entity
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEspecialidad;
    private String nombre;
    private String descripcion;

    // relaciones

    @ManyToOne
    @JoinColumn(name = "profesional_id")
    private profesionalDeSalud profesionalDeSalud;

    @ManyToOne
    @JoinColumn(name = "PoliticaDeAcceso_Id")
    private politicaDeAcceso politicaDeAcceso;

    // getters and setters


    public profesionalDeSalud getProfesionalDeSalud() {
        return profesionalDeSalud;
    }

    public void setProfesionalDeSalud(profesionalDeSalud profesionalDeSalud) {
        this.profesionalDeSalud = profesionalDeSalud;
    }



}
