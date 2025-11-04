package Entity.DTO;

import java.time.LocalDateTime;

public class DocumentoClinicoPayload {
    public String idOrigen;
    public String centroId;
    public String profesionalId;
    public String usuarioLocalId;

    public String titulo;
    public String descripcion;
    public String tipoDocumento;
    public String area;
    public String urlAlojamiento;
    public LocalDateTime fechaCreacion;
}
