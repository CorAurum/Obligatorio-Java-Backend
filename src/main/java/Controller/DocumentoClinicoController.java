package Controller;

import Entity.DTO.DocumentoClinicoDTO;
import Entity.DTO.DocumentoClinicoParaUsuarioDTO;
import Entity.DTO.DocumentoClinicoPayload;
import Entity.DocumentoClinico;
import Service.DocumentoClinicoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/documentoClinico")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DocumentoClinicoController {

    @Inject
    private DocumentoClinicoService documentoClinicoService;

    // 🔹 POST desde un componente periférico
    @POST
    @Path("/externo")
    public Response registrarDesdePeriferico(DocumentoClinicoPayload payload) {
        var result = documentoClinicoService.registrarDesdePeriferico(payload);
        if (!result.success)
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        return Response.ok(result).build();
    }

    // 🔹 GET por ID
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") String id) {
        DocumentoClinico doc = documentoClinicoService.buscarPorId(id);
        if (doc == null)
            return Response.status(Response.Status.NOT_FOUND).entity("No se encontró el documento").build();
        return Response.ok(doc).build();
    }

    // 🔹 GET por usuario (Golden)
    @GET
    @Path("/usuario/{usuarioId}")
    public Response listarPorUsuario(@PathParam("usuarioId") String usuarioId) {
        List<DocumentoClinico> docs = documentoClinicoService.listarPorUsuario(usuarioId);
        return Response.ok(docs).build();
    }

    // GET de DTO para usuario (Golden)

    @GET
    @Path("/usuarioDTO/{usuarioId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarPorUsuarioDTO(@PathParam("usuarioId") String usuarioId) {
        List<DocumentoClinicoDTO> docs = documentoClinicoService.listarPorUsuarioDTO(usuarioId);
        return Response.ok(docs).build();
    }


    // 🔹 GET todos
    @GET
    @Path("/todos")
    public Response listarTodos() {
        List<DocumentoClinico> docs = documentoClinicoService.listarTodos();
        return Response.ok(docs).build();
    }


    // devuelve documento clinico completo traido desde el periferico para que el usuario lo vea

    @GET
    @Path("/{id}/detalle")
    public Response obtenerDetalleDocumento(@PathParam("id") String documentoId) {
        try {
            DocumentoClinicoParaUsuarioDTO dto =
                    documentoClinicoService.obtenerDocumentoCompleto(documentoId);

            // Devuelve el JSON recibido desde el periférico al front
            return Response.ok(dto).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }


}
