package server.endpoints;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.impl.JWTParser;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.codec.Charsets;
import org.apache.commons.io.IOUtils;
import server.util.Config;


import javax.annotation.Priority;
import javax.ws.rs.NameBinding;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.nio.charset.Charset;
import java.util.Date;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    ContainerRequestContext localContainerRequestContext;
    String httpMethodType;
    AuthEndpoint authEndpoint = new AuthEndpoint();
    DecodedJWT jwt;
    public static String userEmailByToken;
    private static final String AUTHENTICATION_SCHEME = "Bearer";
    String token;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {

        if (Config.getSecureEndpoints()) {

            String authorizationHeader = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
            localContainerRequestContext = containerRequestContext;
            token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();

            httpMethodType = containerRequestContext.getMethod().toString();

            try {
                if (validateToken(token) == true) {

                } else {
                    containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private boolean validateToken(String token) throws Exception {

        boolean isValidToken = true;
        try {


            Algorithm algorithm = Algorithm.HMAC256(Config.getJwtSecret());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("ROFL").build();

            jwt = verifier.verify(token);


            userEmailByToken = jwt.getClaim("email").asString();

            if (jwt.getExpiresAt().before(new Date(System.currentTimeMillis() * 1000))) {
                localContainerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }

            //This if + method could be used to verify that the user is Authorizedm to use HTTP method DELETE
            //Not used as of 19-10-2017
        /*
        if (httpMethodType.equalsIgnoreCase("DELETE")){

            System.out.println(jwt.getKeyId().toString());
            validateHttpRequest();
        }
        */

        } catch (UnsupportedEncodingException e) {
            isValidToken = false;
        } catch (JWTVerificationException e) {
            isValidToken = false;
        }


        return isValidToken;
    }
/*
    public int validateHttpRequest() {

        Integer userKeyId = null;

        try {
            //UNUSED
            //VERY USEFUL FOR ACTION AUTHENTICATION
            userKeyId = Integer.valueOf(jwt.getKeyId());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return userKeyId;
    }
*/

}
