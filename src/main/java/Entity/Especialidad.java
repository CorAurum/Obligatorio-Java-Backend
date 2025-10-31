package Entity;

import Entity.Usuarios.ProfesionalDeSalud;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 7) Especialidad
 */
@Entity
@Table(name = "especialidad")
public class Especialidad {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    private String nombre;
    private String descripcion;

    @ManyToMany(mappedBy = "especialidades", fetch = FetchType.LAZY)
    @JsonBackReference("profesional-especialidades")
    private List<ProfesionalDeSalud> profesionales = new ArrayList<>();

    @ManyToMany(mappedBy = "especialidades", fetch = FetchType.LAZY)
    @JsonBackReference("politica-especialidades")
    private List<PoliticaDeAcceso> politicas = new ArrayList<>();

    public Especialidad() {}

    // Getters / Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<ProfesionalDeSalud> getProfesionales() { return profesionales; }
    public void setProfesionales(List<ProfesionalDeSalud> profesionales) { this.profesionales = profesionales; }

    public List<PoliticaDeAcceso> getPoliticas() { return politicas; }
    public void setPoliticas(List<PoliticaDeAcceso> politicas) { this.politicas = politicas; }
}