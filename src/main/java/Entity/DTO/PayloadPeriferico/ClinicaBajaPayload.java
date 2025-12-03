package Entity.DTO.PayloadPeriferico;

import java.time.LocalDateTime;

public class ClinicaBajaPayload {
    private String id;
    private LocalDateTime fechaBaja;

    public ClinicaBajaPayload(String id, LocalDateTime fechaBaja) {
        this.id = id;
        this.fechaBaja = fechaBaja;
    }

    // getters y setters
}

