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

    public AccesoLogDTO(String profesionalId, String profesionalNombre, String profesionalApellido,
                        String centroNombre, LocalDateTime fechaAcceso, Boolean resultado, String motivo) {
        this.profesionalId = profesionalId;
        this.profesionalNombre = profesionalNombre;
        this.profesionalApellido = profesionalApellido;
        this.centroNombre = centroNombre;
        this.fechaAcceso = fechaAcceso;
        this.resultado = resultado;
        this.motivo = motivo;
    }

    // Getters
}
