//package Entity.DTO;
//
//import Entity.Notificacion;
//import java.time.LocalDateTime;
//
//public class NotificacionDTO {
//
//    public Long id;
//    public String tipo;
//    public String titulo;
//    public String mensaje;
//    public LocalDateTime fechaCreacion;
//
//    public static NotificacionDTO fromEntity(Notificacion n) {
//        NotificacionDTO dto = new NotificacionDTO();
//        dto.id = Long.valueOf(n.getId());
//        dto.tipo = n.getTipo();
//        dto.titulo = n.getTitulo();
//        dto.mensaje = n.getMensaje();
//        dto.fechaCreacion = n.getFecha();
//        return dto;
//    }
//}
