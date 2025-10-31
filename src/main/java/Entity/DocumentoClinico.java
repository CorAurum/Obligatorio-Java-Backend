package Entity;

import Entity.Usuarios.ProfesionalDeSalud;
import Entity.Usuarios.Usuario;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 8) DocumentoClinico
 */
@Entity
@Table(name = "documento_clinico")
public class DocumentoClinico {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    private LocalDateTime fechaCreacion;
    private String idOrigen;
    @Enumerated(EnumType.STRING)
    private EstadoDocumento estado;
    private String area;
    private String titulo;
    private String descripcion;
    private String tipoDocumento;
    private String urlAlojamiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "centro_id")
    @JsonBackReference
    private CentroDeSalud centroDeSalud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_profesional_id")
    @JsonBackReference
    private ProfesionalDeSalud autorProfesional;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonBackReference
    private Usuario usuario;

    @OneToMany(mappedBy = "documentoClinico", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<AccesoLog> accesos = new ArrayList<>();

    public enum EstadoDocumento { ACTIVO, ARCHIVADO }

    public DocumentoClinico() {}

    // Getters / Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getIdOrigen() { return idOrigen; }
    public void setIdOrigen(String idOrigen) { this.idOrigen = idOrigen; }

    public EstadoDocumento getEstado() { return estado; }
    public void setEstado(EstadoDocumento estado) { this.estado = estado; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public String getUrlAlojamiento() { return urlAlojamiento; }
    public void setUrlAlojamiento(String urlAlojamiento) { this.urlAlojamiento = urlAlojamiento; }

    public CentroDeSalud getCentroDeSalud() { return centroDeSalud; }
    public void setCentroDeSalud(CentroDeSalud centroDeSalud) { this.centroDeSalud = centroDeSalud; }

    public ProfesionalDeSalud getAutorProfesional() { return autorProfesional; }
    public void setAutorProfesional(ProfesionalDeSalud autorProfesional) { this.autorProfesional = autorProfesional; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<AccesoLog> getAccesos() { return accesos; }
    public void setAccesos(List<AccesoLog> accesos) { this.accesos = accesos; }
}
