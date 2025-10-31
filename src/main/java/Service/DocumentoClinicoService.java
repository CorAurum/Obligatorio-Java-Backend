package Service;

import Entity.DocumentoClinico;
import Repository.DocumentoClinicoRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class DocumentoClinicoService {

    @Inject
    private DocumentoClinicoRepository repository;

    public void crearDocumento(DocumentoClinico documento) {
        documento.setFechaCreacion(LocalDateTime.now());
        repository.guardar(documento);
    }

    public DocumentoClinico obtenerPorId(Long idDocumento) {
        return repository.buscarPorId(idDocumento);
    }

    public List<DocumentoClinico> listarDocumentos() {
        return repository.listarTodos();
    }

    public DocumentoClinico actualizarDocumento(Long idDocumento, DocumentoClinico documentoNuevo) {
        DocumentoClinico existente = repository.buscarPorId(idDocumento);
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
