package Entity.Usuarios;


import Entity.CentroDeSalud;
import Entity.DocumentoClinico;
import Entity.Especialidad;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import Entity.AccesoRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 6) ProfesionalDeSalud
 */
@Entity
@Table(name = "profesional_salud")
public class ProfesionalDeSalud {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    private String numeroRegistro;
    private String nombres;
    private String apellidos;
    private String email;
    private String telefono;
    private LocalDate fechaRegistroProfesional;

    @Enumerated(EnumType.STRING)
    private EstadoProfesional estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "centro_id")
    @JsonBackReference("centro-profesionales")
    private CentroDeSalud centroDeSalud;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "profesional_especialidad",
            joinColumns = @JoinColumn(name = "profesional_id"),
            inverseJoinColumns = @JoinColumn(name = "especialidad_id"))
    @JsonIgnore
    private List<Especialidad> especialidades = new ArrayList<>();

    @OneToMany(mappedBy = "profesionalSolicitante", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<AccesoRequest> Solicitudes_De_Acceso;

    @OneToMany(mappedBy = "autorProfesional", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<DocumentoClinico> documentos = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habilitado_por")
    @JsonBackReference("admin-profesionales")
    private Administrador habilitadoPor;

    public enum EstadoProfesional { ACTIVO, SUSPENDIDO }

    public ProfesionalDeSalud() {}

    // Getters / Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNumeroRegistro() { return numeroRegistro; }
    public void setNumeroRegistro(String numeroRegistro) { this.numeroRegistro = numeroRegistro; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public LocalDate getFechaRegistroProfesional() { return fechaRegistroProfesional; }
    public void setFechaRegistroProfesional(LocalDate fechaRegistroProfesional) { this.fechaRegistroProfesional = fechaRegistroProfesional; }

    public EstadoProfesional getEstado() { return estado; }
    public void setEstado(EstadoProfesional estado) { this.estado = estado; }

    public CentroDeSalud getCentroDeSalud() { return centroDeSalud; }
    public void setCentroDeSalud(CentroDeSalud centroDeSalud) { this.centroDeSalud = centroDeSalud; }

    public List<Especialidad> getEspecialidades() { return especialidades; }
    public void setEspecialidades(List<Especialidad> especialidades) { this.especialidades = especialidades; }

    public List<DocumentoClinico> getDocumentos() { return documentos; }
    public void setDocumentos(List<DocumentoClinico> documentos) { this.documentos = documentos; }

    public Administrador getHabilitadoPor() {return habilitadoPor;}
    public void setHabilitadoPor(Administrador habilitadoPor) {this.habilitadoPor = habilitadoPor;}

    @JsonIgnore
    public List<AccesoRequest> getSolicitudes_De_Acceso() {return Solicitudes_De_Acceso;}
    @JsonIgnore
    public void setSolicitudes_De_Acceso(List<AccesoRequest> solicitudes_De_Acceso) {Solicitudes_De_Acceso = solicitudes_De_Acceso;}
}
