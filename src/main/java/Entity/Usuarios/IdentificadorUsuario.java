package Entity.Usuarios;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import Entity.CentroDeSalud;

/**
 * 2) IdentificadorUsuario
 */
@Entity
@Table(name = "identificador_usuario")
public class IdentificadorUsuario {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    private String tipo; // CI, gubuy, id_local_clinica, pasaporte, etc.
    private String valor;
    private String origen;
    private LocalDateTime fechaAlta;
    private LocalDateTime fechaBaja;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonBackReference("usuario-identificadores")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "centro_id_origen", nullable = true)
    private CentroDeSalud centroOrigen;

    public IdentificadorUsuario() {}

    // Getters / Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getValor() { return valor; }
    public void setValor(String valor) { this.valor = valor; }

    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }

    public LocalDateTime getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDateTime fechaAlta) { this.fechaAlta = fechaAlta; }

    public LocalDateTime getFechaBaja() { return fechaBaja; }
    public void setFechaBaja(LocalDateTime fechaBaja) { this.fechaBaja = fechaBaja; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public CentroDeSalud getCentroOrigen() {return centroOrigen;}
    public void setCentroOrigen(CentroDeSalud centroOrigen) {  this.centroOrigen = centroOrigen;}

}
