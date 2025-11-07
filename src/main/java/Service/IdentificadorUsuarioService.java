package Service;

import Entity.Usuarios.IdentificadorUsuario;
import Repository.IdentificadorUsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class IdentificadorUsuarioService {

    @Inject
    private IdentificadorUsuarioRepository identificadorUsuarioRepository;

    /**
     * Returns a list of Usuario IDs (golden records) that match a given CI (cedula).
     */
    public List<String> buscarUsuarioIdsPorCedula(String cedula) {
        if (cedula == null || cedula.isBlank())
            throw new IllegalArgumentException("La cédula no puede ser nula ni vacía");

        // Normalize: remove dots and dashes
        String normalized = cedula.replaceAll("[^0-9]", "");

        List<IdentificadorUsuario> matches =
                identificadorUsuarioRepository.buscarTodosPorTipoYValor("CI", normalized);

        return matches.stream()
                .map(i -> i.getUsuario().getId())
                .distinct()
                .collect(Collectors.toList());
    }
}
