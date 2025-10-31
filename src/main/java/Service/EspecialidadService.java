package Service;


import Entity.Especialidad;
import Repository.EspecialidadRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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
}
