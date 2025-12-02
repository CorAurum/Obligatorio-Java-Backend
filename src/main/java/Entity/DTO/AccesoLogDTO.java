package Entity.DTO;

import java.time.LocalDateTime;

public class AccesoLogDTO {

    private String profesionalId;
    private String profesionalNombre;
    private String profesionalApellido;
    private String centroNombre;

    private LocalDateTime fechaAcceso;
    private Boolean resultado;
    private String motivo;

    // Constructor to initialize all fields
    public AccesoLogDTO(String profesionalId, String profesionalNombre, String profesionalApellido,
                        String centroNombre, LocalDateTime fechaAcceso, Boolean resultado, String motivo) {
        this.profesionalId = profesionalId != null ? profesionalId : "Desconocido"; // Default if null
        this.profesionalNombre = profesionalNombre != null ? profesionalNombre : "Desconocido"; // Default if null
        this.profesionalApellido = profesionalApellido != null ? profesionalApellido : ""; // Empty string if null
        this.centroNombre = centroNombre != null ? centroNombre : "No asignado"; // Default if null
        this.fechaAcceso = fechaAcceso != null ? fechaAcceso : LocalDateTime.now(); // Default to now if null
        this.resultado = resultado != null ? resultado : false; // Default to false if null
        this.motivo = motivo != null ? motivo : ""; // Empty string if null
    }

    // Getters and Setters (to be added)
    public String getProfesionalId() {
        return profesionalId;
    }

    public String getProfesionalNombre() {
        return profesionalNombre;
    }

    public String getProfesionalApellido() {
        return profesionalApellido;
    }

    public String getCentroNombre() {
        return centroNombre;
    }

    public LocalDateTime getFechaAcceso() {
        return fechaAcceso;
    }

    public Boolean getResultado() {
        return resultado;
    }

    public String getMotivo() {
        return motivo;
    }
}
