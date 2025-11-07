package Entity.Usuarios;

import Entity.CentroDeSalud;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "administrador")
public class Administrador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String cedula;
    private LocalDateTime fechaAlta;
    private Boolean activo;

    // Relación: centros creados (opcional, bidireccional si centro tiene creadoPor)
    @OneToMany(mappedBy = "creadoPor", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CentroDeSalud> centrosCreados = new ArrayList<>();

    @OneToMany(mappedBy = "habilitadoPor", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ProfesionalDeSalud> profesionalesHabilitados = new ArrayList<>();

    public Administrador() {}

    // Getters / setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public LocalDateTime getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDateTime fechaAlta) { this.fechaAlta = fechaAlta; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public List<CentroDeSalud> getCentrosCreados() { return centrosCreados; }
    public void setCentrosCreados(List<CentroDeSalud> centrosCreados) { this.centrosCreados = centrosCreados; }

    public List<ProfesionalDeSalud> getProfesionalesHabilitados() { return profesionalesHabilitados; }
    public void setProfesionalesHabilitados(List<ProfesionalDeSalud> profesionalesHabilitados) { this.profesionalesHabilitados = profesionalesHabilitados; }
}
