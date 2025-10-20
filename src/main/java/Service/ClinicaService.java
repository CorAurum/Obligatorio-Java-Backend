package Service;

import Class.centroSalud;
import Repository.ClinicaRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class ClinicaService {

    @Inject
    private ClinicaRepository clinicaRepo;

    public void crearClinica(centroSalud centroSalud) {
        clinicaRepo.guardar(centroSalud);
    }

    public centroSalud obtenerClinica(Long id) {
        return clinicaRepo.buscarPorId(id);
    }

    public List<centroSalud> listarClinicas() {
        return clinicaRepo.listar();
    }

    public void eliminarClinica(Long id) {
        clinicaRepo.eliminar(id);
    }

    public centroSalud actualizarClinica(centroSalud centroSalud) {
        return clinicaRepo.actualizar(centroSalud);
    }
}
