package Class;

import jakarta.persistence.*;

import java.time.LocalDateTime;

// =================== NOTIFICACIÓN ===================
@Entity
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNotificacion;

    private String tipo; // nuevo acceso, solicitud acceso
    private String mensaje;
    private LocalDateTime fechaEnvio;
    private String estado; // enviada, leída

    @ManyToOne
    @JoinColumn(name = "cedula_usuario")
    private usuarioDeSalud usuario;
}
