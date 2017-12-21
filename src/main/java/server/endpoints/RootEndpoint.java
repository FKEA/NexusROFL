package server.endpoints;

import server.util.Log;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/")
public class RootEndpoint {

    Log log = new Log();

    @GET
    public Response defaultGetMethod() {

        log.writeLog(this.getClass().getName(), this.getClass(), ("defaultGetMethod was successful - " +
                "User active was: " + AuthenticationFilter.userEmailByToken), 0);

        return Response.status(200).type("text/plain").entity("Welcome to our API").build();

    }
}
