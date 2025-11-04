package Entity.Usuarios;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import Entity.CentroDeSalud;

@Entity
@Table(name = "usuario_local")
public class UsuarioLocal {

    @Id
    @Column(name = "id_local", nullable = false)
    private String idLocal;

    @Column(name = "centro_salud_id")
    private String centroDeSaludId;

    @Column(name = "usuario_id_inus")
    private String usuarioId;

    private String nombres;
    private String apellidos;
    private String email;
    private String telefono;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "centro_id")
    @JsonBackReference("centro-usuariosLocal")
    private CentroDeSalud centroDeSalud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_inus_id")
    @JsonBackReference("usuario-usuariosLocal")
    private Usuario usuario;

    public UsuarioLocal() {}

    // Getters / Setters
    public String getIdLocal() { return idLocal; }
    public void setIdLocal(String idLocal) { this.idLocal = idLocal; }

    public String getCentroDeSaludId() { return centroDeSaludId; }
    public void setCentroDeSaludId(String centroDeSaludId) { this.centroDeSaludId = centroDeSaludId; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public CentroDeSalud getCentroDeSalud() { return centroDeSalud; }
    public void setCentroDeSalud(CentroDeSalud centroDeSalud) { this.centroDeSalud = centroDeSalud; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
