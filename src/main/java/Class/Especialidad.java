package Class;

import jakarta.persistence.*;

import java.util.List;

// =================== ESPECIALIDAD ===================
@Entity
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEspecialidad;

    private String nombre;
    private String descripcion;

    @ManyToMany(mappedBy = "especialidades")
    private List<profesionalDeSalud> profesionales;
}
