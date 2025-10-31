package Service;

import Entity.CentroDeSalud;
import Repository.ClinicaRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class ClinicaService {

    @Inject
    private ClinicaRepository clinicaRepo;

    public void crearClinica(CentroDeSalud centroSalud) {
        clinicaRepo.guardar(centroSalud);
    }

    public CentroDeSalud obtenerClinica(Long id) {
        return clinicaRepo.buscarPorId(id);
    }

    public List<CentroDeSalud> listarClinicas() {
        return clinicaRepo.listar();
    }

    public void eliminarClinica(Long id) {
        clinicaRepo.eliminar(id);
    }

    public CentroDeSalud actualizarClinica(CentroDeSalud centroSalud) {
        return clinicaRepo.actualizar(centroSalud);
    }
}
