package Class;

import jakarta.persistence.*;

import java.time.LocalDateTime;

// =================== POLÍTICA DE ACCESO ===================
@Entity
public class politicaDeAcceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPolitica;

    private String tipoAcceso; // profesional, clínica, especialidad
    private LocalDateTime fechaCreacion;
    private String vigencia;
    private String estado; // activa, revocada

    @ManyToOne
    @JoinColumn(name = "cedula_usuario")
    private usuarioDeSalud usuario;

    @ManyToOne
    @JoinColumn(name = "id_documento")
    private documentoClinico documento;
}
