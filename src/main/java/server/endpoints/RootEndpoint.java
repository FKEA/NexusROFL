package server.endpoints;

import server.util.Auth;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/")
public class RootEndpoint {

    @GET
    public Response defaultGetMethod(){

        return Response.status(200).type("text/plain").entity("Hello World").build();
    }
}
