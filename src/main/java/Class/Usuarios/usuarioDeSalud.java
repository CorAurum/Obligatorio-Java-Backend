package Class.Usuarios;

import Class.documentoClinico;
import Class.Notificacion;
import Class.politicaDeAcceso;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class usuarioDeSalud extends usuario {

    //relaciones

    @OneToMany(mappedBy = "usuarioDeSalud")
    private List<Notificacion> notificaciones;

    @OneToMany(mappedBy = "usuarioDeSalud")
    private List<documentoClinico> documentoClinico;

    @OneToMany(mappedBy = "usuarioDeSalud")
    private List<politicaDeAcceso> politicaDeAcceso;


}
