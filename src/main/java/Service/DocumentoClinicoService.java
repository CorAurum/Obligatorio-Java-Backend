package Service;

import Class.documentoClinico;
import Repository.DocumentoClinicoRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class DocumentoClinicoService {

    @Inject
    private DocumentoClinicoRepository repository;

    public void crearDocumento(documentoClinico documento) {
        documento.setFechaCreacion(LocalDateTime.now());
        repository.guardar(documento);
    }

    public documentoClinico obtenerPorId(Long idDocumento) {
        return repository.buscarPorId(idDocumento);
    }

    public List<documentoClinico> listarDocumentos() {
        return repository.listarTodos();
    }

    public documentoClinico actualizarDocumento(Long idDocumento, documentoClinico documentoNuevo) {
        documentoClinico existente = repository.buscarPorId(idDocumento);
        if (existente != null) {
            existente.setTitulo(documentoNuevo.getTitulo());
            existente.setTipoDocumento(documentoNuevo.getTipoDocumento());
            existente.setContenido(documentoNuevo.getContenido());
            existente.setEstado(documentoNuevo.isEstado());
            existente.setCentroSalud(documentoNuevo.getCentroSalud());
            existente.setProfesionalDeSalud(documentoNuevo.getProfesionalDeSalud());
            existente.setPoliticaDeAcceso(documentoNuevo.getPoliticaDeAcceso());
            existente.setUsuarioDeSalud(documentoNuevo.getUsuarioDeSalud());
            return repository.actualizar(existente);
        } else {
            documentoNuevo.setIdDocumentoCentral(idDocumento);
            documentoNuevo.setFechaCreacion(LocalDateTime.now());
            repository.guardar(documentoNuevo);
            return documentoNuevo;
        }
    }

    public void eliminarDocumento(Long idDocumento) {
        repository.eliminar(idDocumento);
    }
}
