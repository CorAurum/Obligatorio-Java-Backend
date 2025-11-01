package Entity.DTO;

import java.util.List;

public class UsuarioLocalPayload {
    public String idLocal;

    public String getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(String idLocal) {
        this.idLocal = idLocal;
    }

    public String getCentroId() {
        return centroId;
    }

    public void setCentroId(String centroId) {
        this.centroId = centroId;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<IdentificadorDto> getIdentificadores() {
        return identificadores;
    }

    public void setIdentificadores(List<IdentificadorDto> identificadores) {
        this.identificadores = identificadores;
    }

    public String centroId;
    public String nombres;
    public String apellidos;
    public String fechaNacimiento;
    public String sexo;
    public String direccion;
    public String email;
    public String telefono;
    public List<IdentificadorDto> identificadores;

    public static class IdentificadorDto {
        public String tipo;
        public String valor;

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public String getValor() {
            return valor;
        }

        public void setValor(String valor) {
            this.valor = valor;
        }

        public String getOrigen() {
            return origen;
        }

        public void setOrigen(String origen) {
            this.origen = origen;
        }

        public String origen;
    }
}
