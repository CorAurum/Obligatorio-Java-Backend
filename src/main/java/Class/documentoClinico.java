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
    private Long idDocumentoCentral;

    //nuevos atributos
    private Long idDocumentoOrigen;
    private String area;
    // ----

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

    // GETTERS AND SETTERS

    public usuarioDeSalud getUsuarioDeSalud() {
        return usuarioDeSalud;
    }

    public void setUsuarioDeSalud(usuarioDeSalud usuarioDeSalud) {
        this.usuarioDeSalud = usuarioDeSalud;
    }

    public politicaDeAcceso getPoliticaDeAcceso() {
        return politicaDeAcceso;
    }

    public void setPoliticaDeAcceso(politicaDeAcceso politicaDeAcceso) {
        this.politicaDeAcceso = politicaDeAcceso;
    }

    public Class.Usuarios.profesionalDeSalud getProfesionalDeSalud() {
        return profesionalDeSalud;
    }

    public void setProfesionalDeSalud(Class.Usuarios.profesionalDeSalud profesionalDeSalud) {
        this.profesionalDeSalud = profesionalDeSalud;
    }

    public centroSalud getCentroSalud() {
        return centroSalud;
    }

    public void setCentroSalud(centroSalud centroSalud) {
        this.centroSalud = centroSalud;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Long getIdDocumentoCentral() {
        return idDocumentoCentral;
    }

    public void setIdDocumentoCentral(Long idDocumento) {
        this.idDocumentoCentral = idDocumento;
    }

    public Long getIdDocumentoOrigen() {
        return idDocumentoOrigen;
    }

    public void setIdDocumentoOrigen(Long idDocumentoOrigen) {
        this.idDocumentoOrigen = idDocumentoOrigen;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }




}
