package Class;

import Class.Usuarios.profesionalDeSalud;
import Class.Usuarios.usuarioDeSalud;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

// =================== DOCUMENTO CLÍNICO ===================
@Entity
public class documentoClinico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDocumento;
    private String titulo;
    private String tipoDocumento; // consulta, estudio, informe
    private LocalDateTime fechaCreacion;
    private String contenido;
    private boolean estado; // activo, archivado

    // relaciones

    @ManyToOne
    @JoinColumn(name = "centroSalud_id")
    private centroSalud centroSalud;

    @ManyToOne
    @JoinColumn(name = "profesionalDeSalud_id")
    private profesionalDeSalud profesionalDeSalud;

    @ManyToOne
    @JoinColumn(name = "politicaDeAcceso_id")
    private politicaDeAcceso politicaDeAcceso;

    @ManyToOne
    @JoinColumn(name = "usuarioDeSalud_id")
    private usuarioDeSalud usuarioDeSalud;




}
