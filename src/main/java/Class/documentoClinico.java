package Class;

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
    @Column(columnDefinition = "TEXT")
    private String contenido;
    private String estado; // activo, archivado

    @ManyToOne
    @JoinColumn(name = "cedula_usuario")
    private usuarioDeSalud usuario;

    @ManyToOne
    @JoinColumn(name = "id_profesional")
    private profesionalDeSalud profesional;

    @ManyToOne
    @JoinColumn(name = "id_clinica")
    private Clinica clinica;

    @OneToMany(mappedBy = "documento", cascade = CascadeType.ALL)
    private List<politicaDeAcceso> politicas;
}
