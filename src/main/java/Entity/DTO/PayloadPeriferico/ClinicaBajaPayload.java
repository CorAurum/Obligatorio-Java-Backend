package Entity.DTO.PayloadPeriferico;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class ClinicaBajaPayload {

    @JsonProperty
    private String id;

    @JsonProperty
    private LocalDateTime fechaBaja;

    public ClinicaBajaPayload(String id, LocalDateTime fechaBaja) {
        this.id = id;
        this.fechaBaja = fechaBaja;
    }

    // Public getters — REQUIRED for Jackson
    public String getId() { return id; }
    public LocalDateTime getFechaBaja() { return fechaBaja; }

    // Optional setters if needed
    public void setId(String id) { this.id = id; }
    public void setFechaBaja(LocalDateTime fechaBaja) { this.fechaBaja = fechaBaja; }
}
