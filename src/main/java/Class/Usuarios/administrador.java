package Class.Usuarios;

import Class.centroSalud;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class administrador extends usuario {


  @OneToMany(mappedBy = "administrador")
  private List<centroSalud> centroSalud;

    public List<centroSalud> getCentroSalud() {
        return centroSalud;
    }

    public void setCentroSalud(List<centroSalud> centroSalud) {
        this.centroSalud = centroSalud;
    }


    public administrador() {
    }
}
