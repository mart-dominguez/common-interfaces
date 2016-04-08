package ar.gov.santafe.meduc.interfaces;

import ar.gov.santafe.meduc.dto.SimpleDto;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("persona")
public interface PersonaService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SimpleDto getPersona(@QueryParam("nombre") String nombre);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public SimpleDto createPersona(SimpleDto persona);

}
