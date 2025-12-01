package Service;


import Entity.Especialidad;
import Repository.EspecialidadRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Stateless
@Transactional
public class EspecialidadService {

    @Inject
    private EspecialidadRepository especialidadRepository;

    public Especialidad crearSiNoExiste(String nombre, String descripcion) {
        Especialidad existente = especialidadRepository.buscarPorNombre(nombre);
        if (existente != null) return existente;

        Especialidad nueva = new Especialidad();
        nueva.setId(UUID.randomUUID().toString());
        nueva.setNombre(nombre);
        nueva.setDescripcion(descripcion);
        especialidadRepository.crear(nueva);
        return nueva;
    }

    // CON ESTO SINCRONIZAMOS LAS ESPECIALIDADES CREADAS AL CENTRAL CON EL PERIFERICO PARA ALOJARLAS Y SEAN IGUALES EN AMBAS BD
    private static final String PERIFERICO_BASE = "https://p1.enbondi.xyz/api/especialidades";
    // cámbialo al dominio real

    private final Client client = ClientBuilder.newClient();

    public void enviarEspecialidad(Especialidad esp) {
        Response response = client
                .target(PERIFERICO_BASE)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(esp));

        if (response.getStatus() >= 400) {
            throw new RuntimeException("Error enviando especialidad al periférico: "
                    + response.getStatus() + " - " + response.readEntity(String.class));
        }
    }


}
