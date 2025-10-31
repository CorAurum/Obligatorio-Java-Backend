package Entity.Usuarios;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import Entity.*;


/**
 * 1) Usuario (Golden Record / INUS)
 */
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String sexo;
    private String direccion;
    private String emailPrincipal;
    private String telefonoPrincipal;

    @Enumerated(EnumType.STRING)
    private EstadoUsuario estado;

    private LocalDateTime fechaRegistro;
    private LocalDateTime ultimaActualizacion;

    // Relations - bidirectional
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference("usuario-identificadores")
    private List<IdentificadorUsuario> identificadores = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference("usuario-documentos")
    private List<DocumentoClinico> documentosClinicos = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference("usuario-politicas")
    private List<PoliticaDeAcceso> politicasDeAcceso = new ArrayList<>();

    @OneToMany(mappedBy = "usuarioDestino", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference("usuario-notificaciones")
    private List<Notificacion> notificaciones = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference("usuario-accesos")
    private List<AccesoLog> accesos = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    @JsonManagedReference("usuario-usuariosLocal")
    private List<UsuarioLocal> usuariosLocales = new ArrayList<>();

    // Enum for estado
    public enum EstadoUsuario {
        ACTIVO, INACTIVO, BORRADO_LOGICO
    }

    public Usuario() {}

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getEmailPrincipal() { return emailPrincipal; }
    public void setEmailPrincipal(String emailPrincipal) { this.emailPrincipal = emailPrincipal; }

    public String getTelefonoPrincipal() { return telefonoPrincipal; }
    public void setTelefonoPrincipal(String telefonoPrincipal) { this.telefonoPrincipal = telefonoPrincipal; }

    public EstadoUsuario getEstado() { return estado; }
    public void setEstado(EstadoUsuario estado) { this.estado = estado; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public LocalDateTime getUltimaActualizacion() { return ultimaActualizacion; }
    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) { this.ultimaActualizacion = ultimaActualizacion; }

    public List<IdentificadorUsuario> getIdentificadores() { return identificadores; }
    public void setIdentificadores(List<IdentificadorUsuario> identificadores) { this.identificadores = identificadores; }

    public List<DocumentoClinico> getDocumentosClinicos() { return documentosClinicos; }
    public void setDocumentosClinicos(List<DocumentoClinico> documentosClinicos) { this.documentosClinicos = documentosClinicos; }

    public List<PoliticaDeAcceso> getPoliticasDeAcceso() { return politicasDeAcceso; }
    public void setPoliticasDeAcceso(List<PoliticaDeAcceso> politicasDeAcceso) { this.politicasDeAcceso = politicasDeAcceso; }

    public List<Notificacion> getNotificaciones() { return notificaciones; }

}
