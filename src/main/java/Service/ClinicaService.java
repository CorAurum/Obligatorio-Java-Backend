package Service;

import Class.Clinica;
import Repository.ClinicaRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class ClinicaService {

    @Inject
    private ClinicaRepository clinicaRepo;

    public void crearClinica(Clinica clinica) {
        clinicaRepo.guardar(clinica);
    }

    public Clinica obtenerClinica(Long id) {
        return clinicaRepo.buscarPorId(id);
    }

    public List<Clinica> listarClinicas() {
        return clinicaRepo.listar();
    }

    public void eliminarClinica(Long id) {
        clinicaRepo.eliminar(id);
    }

    public Clinica actualizarClinica(Clinica clinica) {
        return clinicaRepo.actualizar(clinica);
    }
}
