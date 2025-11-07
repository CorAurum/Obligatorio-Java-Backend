package Service;

import Entity.CentroDeSalud;
import Entity.DTO.UsuarioLocalPayload;
import Entity.Usuarios.IdentificadorUsuario;
import Entity.Usuarios.Usuario;
import Entity.Usuarios.UsuarioLocal;
import Repository.CentroDeSaludRepository;
import Repository.IdentificadorUsuarioRepository;
import Repository.UsuarioLocalRepository;
import Repository.UsuarioRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Stateless
@Transactional
public class UsuarioService {

    @Inject private UsuarioRepository usuarioRepository;
    @Inject private IdentificadorUsuarioRepository identificadorUsuarioRepository;
    @Inject private UsuarioLocalRepository usuarioLocalRepository;
    @Inject private CentroDeSaludRepository centroDeSaludRepository;

    public Usuario buscarPorId(String id) {
        return usuarioRepository.buscarPorId(id);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.listarTodos();
    }

////  METODO DE SINCRONIZACION  DE USUARIOS DESDE PERIFERICO, CREACION DE USUARIOS LOCALES, IDENTIFICADORES Y USUARIO GOLDEN RECORD

    public static class ResultadoSync {
        public boolean conflict;
        public String message;
        public String usuarioId; // golden
        public List<String> usuarioIdsEncontrados; // si conflict
        public List<String> identificadoresRegistrados;
    }

    public ResultadoSync syncFromPeriferico(UsuarioLocalPayload p, boolean forceMerge) {
        ResultadoSync out = new ResultadoSync();

        if (p.centroId == null || p.idLocal == null) {
            throw new IllegalArgumentException("centroId e idLocal son obligatorios");
        }

        // 1️⃣ Buscar UsuarioLocal
        UsuarioLocal ul = usuarioLocalRepository.buscarPorCentroYIdLocal(p.centroId, p.idLocal);
        boolean nuevoLocal = (ul == null);

        if (nuevoLocal) {
            ul = new UsuarioLocal();
            ul.setIdLocal(p.idLocal);
            ul.setCentroDeSaludId(p.centroId);
            // revisar si setear 2 ids es necesario, es la misma id, medio al pedo

            CentroDeSalud centro = centroDeSaludRepository.buscarPorId(p.centroId);
            if (centro == null) {
                throw new IllegalArgumentException("No existe el centro de salud con id: " + p.centroId);
            }
            ul.setCentroDeSalud(centro);


        }

        // Actualizar datos del usuario local
        ul.setNombres(p.nombres);
        ul.setApellidos(p.apellidos);
        ul.setEmail(p.email);
        ul.setTelefono(p.telefono);



        // 2) procesar identificadores -> buscar coincidencias
        List<String> idsRegistrados = new ArrayList<>();
        Set<String> usuarioGoldenCandidates = new LinkedHashSet<>();

        if (p.identificadores != null) {
            for (UsuarioLocalPayload.IdentificadorDto idDto : p.identificadores) {
                String tipo = normalizeTipo(idDto.tipo);
                String valor = normalizeValor(tipo, idDto.valor);

                IdentificadorUsuario idFound = identificadorUsuarioRepository.buscarPorTipoYValor(tipo, valor);
                if (idFound != null) {
                    usuarioGoldenCandidates.add(idFound.getUsuario().getId());
                } else {
                    // no existe identificador: crearlo después apuntando al usuario elegido
                    // pero guardamos para crear luego
                    idsRegistrados.add(tipo + ":" + valor);
                }
            }
        }

        // 3) decidir usuario golden target
        if (usuarioGoldenCandidates.isEmpty()) {
            // crear nuevo usuario
            Usuario nuevo = new Usuario();
            nuevo.setId(UUID.randomUUID().toString());
            nuevo.setNombres(p.nombres);
            nuevo.setApellidos(p.apellidos);
            nuevo.setEmailPrincipal(p.email);
            nuevo.setTelefonoPrincipal(p.telefono);
            nuevo.setDireccion(p.direccion);
            nuevo.setSexo(p.sexo);
            if (p.fechaNacimiento != null) {
                nuevo.setFechaNacimiento(LocalDate.parse(p.fechaNacimiento));
            }
            nuevo.setEstado(Usuario.EstadoUsuario.ACTIVO);
            nuevo.setFechaRegistro(LocalDateTime.now());
            usuarioRepository.crear(nuevo);

            System.out.println("[UsuarioService] Nuevo usuario creado: " + nuevo.getId());
            // crear identificadores y asociarlos
            if (p.identificadores != null) {
                for (UsuarioLocalPayload.IdentificadorDto idDto : p.identificadores) {

                    IdentificadorUsuario idu = new IdentificadorUsuario();
                    idu.setId(UUID.randomUUID().toString());
                    idu.setTipo(normalizeTipo(idDto.tipo));
                    idu.setValor(normalizeValor(idu.getTipo(), idDto.valor));
                    idu.setOrigen(idDto.origen);
                    idu.setFechaAlta(LocalDateTime.now());
                    idu.setUsuario(nuevo);
                    idu.setCentroOrigen(ul.getCentroDeSalud());
                    identificadorUsuarioRepository.crear(idu);
                }
            }
            // vincular usuarioLocal -> nuevo
            ul.setUsuarioId(nuevo.getId());
            ul.setUsuario(nuevo);
            usuarioLocalRepository.crear(ul);
            out.usuarioId = nuevo.getId();
            out.identificadoresRegistrados = idsRegistrados;
            out.message = "CREATED";
            return out;
        }
        else if (usuarioGoldenCandidates.size() == 1) {
            // ok, hay un usuario objetivo
            String usuarioId = usuarioGoldenCandidates.iterator().next();
            Usuario target = usuarioRepository.buscarPorId(usuarioId);
            // actualizar datos no nulos del target
            if (isNotBlank(p.nombres)) target.setNombres(p.nombres);
            if (isNotBlank(p.apellidos)) target.setApellidos(p.apellidos);
            if (isNotBlank(p.email)) target.setEmailPrincipal(p.email);
            if (isNotBlank(p.telefono)) target.setTelefonoPrincipal(p.telefono);
            if (isNotBlank(p.direccion)) target.setDireccion(p.direccion);
            if (isNotBlank(p.sexo)) target.setSexo(p.sexo);
            if (p.fechaNacimiento != null) target.setFechaNacimiento(LocalDate.parse(p.fechaNacimiento));
            target.setUltimaActualizacion(LocalDateTime.now());
            usuarioRepository.actualizar(target);

            // crear identificadores faltantes apuntando a target
            if (p.identificadores != null) {
                for (UsuarioLocalPayload.IdentificadorDto idDto : p.identificadores) {
                    String tipo = normalizeTipo(idDto.tipo);
                    String valor = normalizeValor(tipo, idDto.valor);
                    IdentificadorUsuario existe = identificadorUsuarioRepository.buscarPorTipoYValor(tipo, valor);
                    if (existe == null) {
                        IdentificadorUsuario idu = new IdentificadorUsuario();
                        idu.setId(UUID.randomUUID().toString());
                        idu.setTipo(tipo);
                        idu.setValor(valor);
                        idu.setOrigen(idDto.origen);
                        idu.setFechaAlta(LocalDateTime.now());
                        idu.setUsuario(target);
                        identificadorUsuarioRepository.crear(idu);
                    }
                }
            }
            // vincular UsuarioLocal
            ul.setUsuarioId(target.getId());
            ul.setUsuario(target);
            if (nuevoLocal) usuarioLocalRepository.crear(ul); else usuarioLocalRepository.actualizar(ul);

            out.usuarioId = target.getId();
            out.message = "UPDATED";
            out.identificadoresRegistrados = idsRegistrados;
            return out;
        } else {
            // varios usuarios encontrados -> conflicto
            out.conflict = true;
            out.usuarioIdsEncontrados = new ArrayList<>(usuarioGoldenCandidates);
            out.message = "CONFLICT_MULTIPLE_GOLDENS";
            if (forceMerge) {
                // ejemplo de política si forceMerge == true (opcional y con auditoría)
                String chosen = usuarioGoldenCandidates.iterator().next();
                // comentar: aquí podrías invocar función mergeUsuarios(usuarioIds, chosen)
                // por simplicidad retornamos conflict a menos que implementes merge robusto
                out.message = "MERGE_NOT_IMPLEMENTED_SAFE_MODE";
            }
            return out;
        }
    }

    // utilitarios
    private String normalizeTipo(String tipo) {
        return tipo == null ? null : tipo.trim().toUpperCase();
    }
    private String normalizeValor(String tipo, String valor) {
        if (valor == null) return null;
        String v = valor.trim();
        if ("CI".equalsIgnoreCase(tipo)) {
            v = v.replaceAll("[^0-9]", ""); // quitar puntos/guiones
        }
        return v;
    }
    private boolean isNotBlank(String s) { return s != null && !s.trim().isEmpty(); }

}
