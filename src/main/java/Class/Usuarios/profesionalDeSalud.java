package Class.Usuarios;


import Class.centroSalud;
import Class.documentoClinico;
import Class.politicaDeAcceso;
import Class.Especialidad;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class profesionalDeSalud extends usuario {

    private String numeroRegistro;



    // Relaciones



    @OneToMany(mappedBy = "profesionalDeSalud")
    private List<Especialidad> especialidad;

    @OneToMany(mappedBy = "profesionalDeSalud")
    private List<documentoClinico> documentoClinico;

    @ManyToOne
    @JoinColumn(name = "centroSalud_id")
    private centroSalud centroSalud;



    // Getters and Setters

    public List<Especialidad> getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(List<Especialidad> especialidad) {
        this.especialidad = especialidad;
    }





}
