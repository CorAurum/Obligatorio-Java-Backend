package Entity;

import Entity.Usuarios.Usuario;
import Entity.Usuarios.UsuarioLocal;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * 10) PoliticaDeAcceso
 */
@Entity
@Table(name = "politica_acceso")
public class PoliticaDeAcceso {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonBackReference(value = "usuario-politicas")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "centro_id")
    @JsonBackReference(value = "centro-politicas")
    private CentroDeSalud centroDeSalud;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "politica_especialidad",
            joinColumns = @JoinColumn(name = "politica_id"),
            inverseJoinColumns = @JoinColumn(name = "especialidad_id"))
    @JsonIgnore
    private List<Especialidad> especialidades = new ArrayList<>();

    private LocalDateTime fechaCreacion;
    private LocalDate vigenciaHasta;

    @Enumerated(EnumType.STRING)
    private EstadoPolitica estado;

    public enum EstadoPolitica { ACTIVA, REVOCADA }

    public PoliticaDeAcceso() {}

    // Getters / Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public CentroDeSalud getCentroDeSalud() { return centroDeSalud; }
    public void setCentroDeSalud(CentroDeSalud centroDeSalud) { this.centroDeSalud = centroDeSalud; }

    public List<Especialidad> getEspecialidades() { return especialidades; }
    public void setEspecialidades(List<Especialidad> especialidades) { this.especialidades = especialidades; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDate getVigenciaHasta() { return vigenciaHasta; }
    public void setVigenciaHasta(LocalDate vigenciaHasta) { this.vigenciaHasta = vigenciaHasta; }

    public EstadoPolitica getEstado() { return estado; }
    public void setEstado(EstadoPolitica estado) { this.estado = estado; }
}