package Service;

import Entity.CentroDeSalud;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Stateless
@Transactional
public class UsuarioService {

    @Inject
    private UsuarioRepository usuarioRepository;

    @Inject
    private IdentificadorUsuarioRepository identificadorRepository;

    @Inject
    private UsuarioLocalRepository usuarioLocalRepository;

    @Inject
    private CentroDeSaludRepository centroRepository;

    public Usuario crearOActualizarUsuario(Usuario usuario, String centroId, List<IdentificadorUsuario> identificadores, UsuarioLocal usuarioLocal) {
        // 1️⃣ Buscar si ya existe un identificador que coincida
        Usuario existente = null;
        for (IdentificadorUsuario idu : identificadores) {
            IdentificadorUsuario encontrado = identificadorRepository.buscarPorValor(idu.getValor());
            if (encontrado != null) {
                existente = encontrado.getUsuario();
                break;
            }
        }

        // 2️⃣ Si no existe, crear nuevo usuario
        if (existente == null) {
            usuario.setId(UUID.randomUUID().toString());
            usuario.setEstado(Usuario.EstadoUsuario.ACTIVO);
            usuario.setFechaRegistro(LocalDateTime.now());
            usuario.setUltimaActualizacion(LocalDateTime.now());
            usuarioRepository.crear(usuario);
            existente = usuario;
        }

        // 3️⃣ Asociar identificadores nuevos
        for (IdentificadorUsuario idu : identificadores) {
            if (identificadorRepository.buscarPorValor(idu.getValor()) == null) {
                idu.setId(UUID.randomUUID().toString());
                idu.setUsuario(existente);
                idu.setFechaAlta(LocalDateTime.now());
                identificadorRepository.crear(idu);
            }
        }

        // 4️⃣ Crear UsuarioLocal (vinculado al centro)
        if (usuarioLocal != null && centroId != null) {
            CentroDeSalud centro = centroRepository.buscarPorId(centroId);
            if (centro == null) throw new RuntimeException("Centro de salud no encontrado");

            usuarioLocal.setUsuario(existente);
            usuarioLocal.setCentroDeSalud(centro);
            usuarioLocalRepository.crear(usuarioLocal);
        }

        return existente;
    }
}
