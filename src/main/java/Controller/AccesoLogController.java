package Controller;

import Service.AccesoLogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static com.google.cloud.firestore.telemetry.MetricsUtil.logger;

// /accesos?usuarioId=abc123

@Path("/accesos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccesoLogController {

    @Inject
    AccesoLogService accesoLogService;

    @GET
    public Response listar(@QueryParam("usuarioId") String usuarioId) throws JsonProcessingException {
        // Log the incoming query parameter
        logger.info("Received request to listar with usuarioId: {}" + usuarioId);

        if (usuarioId == null || usuarioId.isBlank()) {
            logger.info("usuarioId is null or blank");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Debe proporcionar usuarioId")
                    .build();
        }

        // Log before calling the service
        logger.info("Calling service to get AccesoLogDTO list for usuarioId: {}" + usuarioId);

        var lista = accesoLogService.listarDTOporUsuario(usuarioId);

        // Log the number of items returned
        logger.info("Service returned {} logs" + lista.size());

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(lista);

// Log the response for debugging
        System.out.println("Response JSON: " + jsonResponse);

        return Response.ok(lista)
                .type("application/json")
                .build();
    }
}
