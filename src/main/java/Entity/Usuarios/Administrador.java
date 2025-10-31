package Entity.Usuarios;

import Entity.CentroDeSalud;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "administrador")
public class Administrador {

    @Id
    private String id;

    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private LocalDateTime fechaAlta;
    private Boolean activo;

    // Relaciones
    @OneToMany(mappedBy = "creadoPor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CentroDeSalud> centrosCreados = new ArrayList<>();

    @OneToMany(mappedBy = "habilitadoPor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProfesionalDeSalud> profesionalesHabilitados = new ArrayList<>();
}
