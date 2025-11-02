package Entity;

import Entity.Usuarios.ProfesionalDeSalud;
import Entity.Usuarios.Usuario;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * 11) AccesoRequest
 */
@Entity
@Table(name = "acceso_request")
public class AccesoRequest {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    private String solicitanteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonBackReference("usuario-requests")
    private Usuario usuario;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documento_id")
    @JsonBackReference("documento-requests")
    private DocumentoClinico documentoClinico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesional_id")
    @JsonBackReference("profesional-requests")
    private ProfesionalDeSalud profesionalSolicitante;

    private LocalDateTime fechaSolicitud;

    @Enumerated(EnumType.STRING)
    private EstadoRequest estado;

    private String motivo;

    public enum EstadoRequest { PENDIENTE, APROBADO, RECHAZADO }

    public AccesoRequest() {}

    // Getters / Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSolicitanteId() { return solicitanteId; }
    public void setSolicitanteId(String solicitanteId) { this.solicitanteId = solicitanteId; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public DocumentoClinico getDocumentoClinico() { return documentoClinico; }
    public void setDocumentoClinico(DocumentoClinico documentoClinico) { this.documentoClinico = documentoClinico; }

    public LocalDateTime getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDateTime fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }

    public EstadoRequest getEstado() { return estado; }
    public void setEstado(EstadoRequest estado) { this.estado = estado; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}