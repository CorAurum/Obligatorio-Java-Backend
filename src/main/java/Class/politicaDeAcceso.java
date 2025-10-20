package Class;

import Class.Usuarios.usuarioDeSalud;
import Class.Usuarios.profesionalDeSalud;
import jakarta.persistence.*;

import javax.print.Doc;
import java.time.LocalDateTime;
import java.util.List;

// =================== POLÍTICA DE ACCESO ===================
@Entity
public class politicaDeAcceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPolitica;

    private LocalDateTime fechaCreacion;
    private String vigencia;
    private boolean estado; // activa, revocada


    // relaciones

    @ManyToOne
    @JoinColumn(name = "CentroSalud_Id")
    private centroSalud centroSalud;

    @OneToMany(mappedBy = "politicaDeAcceso")
    private List<Especialidad> especialidad;

    @OneToMany(mappedBy = "politicaDeAcceso")
    private List<documentoClinico> documentoClinicos;

    @ManyToOne
    @JoinColumn(name = "usuarioDeSalud_id")
    private usuarioDeSalud usuarioDeSalud;

}
