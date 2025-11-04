package Entity;

import Entity.Usuarios.Usuario;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * 13) AccesoLog
 */

@Entity
@Table(name = "acceso_log")
public class AccesoLog {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    private String usuarioSolicitante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documento_id")
    @JsonBackReference("documento-accesos")
    private DocumentoClinico documentoClinico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonBackReference("usuario-accesos")
    private Usuario usuario;

    // Registra que politica autorizo un acceso
    @Column(name = "politica_aplicada_id")
    private String politicaAplicadaId;


    private LocalDateTime fechaAcceso;
    private Boolean resultado;
    private String motivo;

    public AccesoLog() {}

    // Getters / Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsuarioSolicitante() { return usuarioSolicitante; }
    public void setUsuarioSolicitante(String usuarioSolicitante) { this.usuarioSolicitante = usuarioSolicitante; }

    public DocumentoClinico getDocumentoClinico() { return documentoClinico; }
    public void setDocumentoClinico(DocumentoClinico documentoClinico) { this.documentoClinico = documentoClinico; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public LocalDateTime getFechaAcceso() { return fechaAcceso; }
    public void setFechaAcceso(LocalDateTime fechaAcceso) { this.fechaAcceso = fechaAcceso; }

    public Boolean getResultado() { return resultado; }
    public void setResultado(Boolean resultado) { this.resultado = resultado; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}