package Controller;

import Class.pdi.pdiResponse;
import Service.pdiService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;

@Path("/pdi")
public class pdiController {

    @Inject
    private pdiService soapService;

    @GET
    @Path("/persona/{doc}")
    @Produces(MediaType.APPLICATION_JSON)
    public pdiResponse getPersona(@PathParam("doc") String doc) {
        return soapService.callObtPersonaPorDoc("AAA", "BBB", doc, "DO");
    }


    @GET
    @Path("/persona/over18/{doc}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean isPersonaMayorDeEdad(@PathParam("doc") String doc) {
        return soapService.isPersonaMayorDeEdad("AAA", "BBB", doc, "DO");
    }

}

