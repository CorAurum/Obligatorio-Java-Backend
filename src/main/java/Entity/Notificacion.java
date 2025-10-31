package Entity;

import Entity.Usuarios.Usuario;
import Entity.Usuarios.UsuarioLocal;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * 12) Notificacion
 */
@Entity
@Table(name = "notificacion")
public class Notificacion {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    private String tipo;
    private String mensaje;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_destino_id")
    @JsonBackReference
    private Usuario usuarioDestino;

    private LocalDateTime fechaEnvio;

    @Enumerated(EnumType.STRING)
    private EstadoNotificacion estado;

    public enum EstadoNotificacion { ENVIADA, LEIDA }

    public Notificacion() {}

    // Getters / Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public Usuario getUsuarioDestino() { return usuarioDestino; }
    public void setUsuarioDestino(Usuario usuarioDestino) { this.usuarioDestino = usuarioDestino; }

    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public EstadoNotificacion getEstado() { return estado; }
    public void setEstado(EstadoNotificacion estado) { this.estado = estado; }
}
