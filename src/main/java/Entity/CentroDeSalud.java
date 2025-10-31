package Entity;


import Entity.Usuarios.Administrador;
import Entity.Usuarios.ProfesionalDeSalud;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 4) CentroDeSalud
 */
@Entity
@Table(name = "centro_salud")
public class CentroDeSalud {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    private String nombre;
    private String direccion;
    private String telefono;
    private String tipoInstitucion;
    private String urlWebService;

    @ElementCollection
    @CollectionTable(name = "centro_especialidades", joinColumns = @JoinColumn(name = "centro_id"))
    @Column(name = "especialidad")
    private List<String> especialidades = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private EstadoCentro estado;

    @OneToMany(mappedBy = "centroDeSalud", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ProfesionalDeSalud> profesionales = new ArrayList<>();

    @OneToMany(mappedBy = "centroDeSalud", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<DocumentoClinico> documentosClinicos = new ArrayList<>();

    @OneToMany(mappedBy = "centroDeSalud", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<PoliticaDeAcceso> politicas = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por")
    private Administrador creadoPor;


    public enum EstadoCentro { HABILITADO, INHABILITADO }

    public CentroDeSalud() {}

    // Getters / Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getTipoInstitucion() { return tipoInstitucion; }
    public void setTipoInstitucion(String tipoInstitucion) { this.tipoInstitucion = tipoInstitucion; }

    public String getUrlWebService() { return urlWebService; }
    public void setUrlWebService(String urlWebService) { this.urlWebService = urlWebService; }

    public List<String> getEspecialidades() { return especialidades; }
    public void setEspecialidades(List<String> especialidades) { this.especialidades = especialidades; }

    public EstadoCentro getEstado() { return estado; }
    public void setEstado(EstadoCentro estado) { this.estado = estado; }

    public List<ProfesionalDeSalud> getProfesionales() { return profesionales; }
    public void setProfesionales(List<ProfesionalDeSalud> profesionales) { this.profesionales = profesionales; }

    public List<DocumentoClinico> getDocumentosClinicos() { return documentosClinicos; }
    public void setDocumentosClinicos(List<DocumentoClinico> documentosClinicos) { this.documentosClinicos = documentosClinicos; }

    public List<PoliticaDeAcceso> getPoliticas() { return politicas; }
    public void setPoliticas(List<PoliticaDeAcceso> politicas) { this.politicas = politicas; }

    public Administrador getCreadoPor() {return creadoPor;}
    public void setCreadoPor(Administrador creadoPor) { this.creadoPor = creadoPor;}

}