package Service;

import Entity.CentroDeSalud;
import Entity.DTO.PayloadPeriferico.ClinicaPayload;
import Entity.Usuarios.Administrador;
import Repository.AdministradorRepository;
import Repository.CentroDeSaludRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Stateless
@Transactional
public class CentroDeSaludService {

    @Inject
    private CentroDeSaludRepository centroDeSaludRepository;

    @Inject
    private AdministradorRepository administradorRepository;

    private static final Logger logger = Logger.getLogger(CentroDeSaludService.class.getName());

    public CentroDeSalud crearCentro(CentroDeSalud centro, Long adminId) {

        Administrador admin = administradorRepository.buscarPorId(adminId);
        if (admin == null)
            throw new IllegalArgumentException("Administrador no encontrado");

        if (centro.getId() == null)
            centro.setId(UUID.randomUUID().toString());

        centro.setCreadoPor(admin);
        centro.setEstado(CentroDeSalud.EstadoCentro.HABILITADO);
        centroDeSaludRepository.crear(centro);
        enviarAlPeriferico(centro); // crea la clinica en el componente periferico
        return centro;
    }

    public List<CentroDeSalud> listarCentros() {
        return centroDeSaludRepository.listarTodos();
    }

    public CentroDeSalud obtenerPorId(String id) {
        return centroDeSaludRepository.buscarPorId(id);
    }


    private void enviarAlPeriferico(CentroDeSalud centro) {
        try {
            // 🔹 URL del periférico (ajustar si cambia el dominio o puerto)
            String perifericoUrl = "http://localhost:8081/api/clinicas";

            ClinicaPayload payload = new ClinicaPayload(
                    centro.getNombre(),
                    centro.getDireccion(),
                    centro.getTelefono(),
                    centro.getTipoInstitucion(),
                    "Dom1" // asegúrate que este campo exista en tu entidad
            );

            Client client = ClientBuilder.newClient();
            Response response = client.target(perifericoUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(payload, MediaType.APPLICATION_JSON));

            if (response.getStatus() == 200 || response.getStatus() == 201) {
                logger.info("[SYNC] CentroDeSalud sincronizado con periférico: " + centro.getNombre());
            } else {
                logger.warning("[SYNC] Error al sincronizar con periférico. Código HTTP: " + response.getStatus());
                logger.warning(response.readEntity(String.class));
            }

            response.close();
            client.close();

        } catch (Exception e) {
            logger.severe("[SYNC] Falló la sincronización con el periférico: " + e.getMessage());
        }
    }


}

