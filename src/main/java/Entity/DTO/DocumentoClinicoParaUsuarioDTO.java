package Entity.DTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO que devuelve el periférico cuando el central pide el documento completo.
 * Contiene nombres legibles (tenant, profesional) y datos del documento con
 * motivos/diagnósticos ya resueltos a nombres.
 */
public class DocumentoClinicoParaUsuarioDTO {

    private String tenant; // nombre del centro (no id)
    private String profesional; // "Nombre Apellido"
    private DocumentoDTO documento;

    public DocumentoClinicoParaUsuarioDTO() {}

    // getters / setters
    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }

    public String getProfesional() { return profesional; }
    public void setProfesional(String profesional) { this.profesional = profesional; }

    public DocumentoDTO getDocumento() { return documento; }
    public void setDocumento(DocumentoDTO documento) { this.documento = documento; }

    public static class DocumentoDTO {
        private String area;
        private String titulo;
        private String descripcion;
        private String tipoDocumento;
        private LocalDateTime fechaCreacion;
        private LocalDateTime fechaProximaConsultaConfirmada;
        private List<String> motivosConsulta; // nombres
        private List<DiagnosticoDTO> diagnosticos;

        public DocumentoDTO() {}

        // getters / setters
        public String getArea() { return area; }
        public void setArea(String area) { this.area = area; }

        public String getTitulo() { return titulo; }
        public void setTitulo(String titulo) { this.titulo = titulo; }

        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

        public String getTipoDocumento() { return tipoDocumento; }
        public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

        public LocalDateTime getFechaCreacion() { return fechaCreacion; }
        public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

        public LocalDateTime getFechaProximaConsultaConfirmada() { return fechaProximaConsultaConfirmada; }
        public void setFechaProximaConsultaConfirmada(LocalDateTime fechaProximaConsultaConfirmada) {
            this.fechaProximaConsultaConfirmada = fechaProximaConsultaConfirmada;
        }

        public List<String> getMotivosConsulta() { return motivosConsulta; }
        public void setMotivosConsulta(List<String> motivosConsulta) { this.motivosConsulta = motivosConsulta; }

        public List<DiagnosticoDTO> getDiagnosticos() { return diagnosticos; }
        public void setDiagnosticos(List<DiagnosticoDTO> diagnosticos) { this.diagnosticos = diagnosticos; }
    }

    public static class DiagnosticoDTO {
        private String gradoCerteza;
        private String estadoProblema;
        private String descripcion;

        public DiagnosticoDTO() {}

        public String getGradoCerteza() { return gradoCerteza; }
        public void setGradoCerteza(String gradoCerteza) { this.gradoCerteza = gradoCerteza; }

        public String getEstadoProblema() { return estadoProblema; }
        public void setEstadoProblema(String estadoProblema) { this.estadoProblema = estadoProblema; }

        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    }
}
