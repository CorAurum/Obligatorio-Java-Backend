package Entity.DTO;

import java.time.LocalDateTime;

public class DocumentoClinicoDTO {

    private LocalDateTime fechaCreacion;
    private String area;
    private String descripcion;
    private String documentoId;
    private String profesionalNombre;
    private String profesionalApellido;

    // Constructor completo
    public DocumentoClinicoDTO(LocalDateTime fechaCreacion, String area, String descripcion,
                               String documentoId, String profesionalNombre, String profesionalApellido) {
        this.fechaCreacion = fechaCreacion;
        this.area = area;
        this.descripcion = descripcion;
        this.documentoId = documentoId;
        this.profesionalNombre = profesionalNombre;
        this.profesionalApellido = profesionalApellido;
    }

    // Getters y setters
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getdocumentoId() {
        return documentoId;
    }

    public void setdocumentoIdo(String documentoId) {
        this.documentoId = documentoId;
    }

    public String getProfesionalNombre() {
        return profesionalNombre;
    }

    public void setProfesionalNombre(String profesionalNombre) {
        this.profesionalNombre = profesionalNombre;
    }

    public String getProfesionalApellido() {
        return profesionalApellido;
    }

    public void setProfesionalApellido(String profesionalApellido) {
        this.profesionalApellido = profesionalApellido;
    }
}
