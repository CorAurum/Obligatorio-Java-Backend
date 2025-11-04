package Entity.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// ESTE DTO SE USA PARA DEVOLVER LAS POLITICAS ASOCIADAS A UN USUARIO X, HAY QUE USARLO PORQUE SI NO HACE RECURSION INFINITA Y DEVUELVE ERROR

public class PoliticaDeAccesoDTO {
    public String id;
    public String centroId;
    public String centroNombre;
    public List<EspecialidadDto> especialidades;
    public LocalDateTime fechaCreacion;
    public LocalDate vigenciaHasta;
    public String estado;

    public static class EspecialidadDto {
        public String id;
        public String nombre;
    }
}
