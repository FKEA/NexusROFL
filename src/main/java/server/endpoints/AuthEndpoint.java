package server.endpoints;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import server.models.User;
import server.providers.UserProvider;
import server.util.Auth;
import server.util.Config;
import server.util.DBConnector;
import server.util.Log;

import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 * Created by Filip on 10-10-2017.
 */

@Path("/auth")
public class AuthEndpoint {

    //Creating objects of the classes UserProvider and User

    ArrayList<String> tokenArray = new ArrayList<String>();

    private Logger log = Logger.getLogger(DBConnector.class);

    UserProvider userProvider = new UserProvider();
    User foundUser = new User();
    String checkHashed;
    Date expDate;

    /**
     * This method authorizes an user by e-mail and password. To protect the users password, this method employ salted password hashing.
     * This method also converts from JSON to GSON
     *
     * @param jsonUser
     * @return This method returns different response status codes defined by HTTP
     */
    @POST
    public Response AuthUser(String jsonUser) {

        User authUser = new Gson().fromJson(jsonUser, User.class);
        String token = null;

        //Creating try-catch to check if the user is authorized by e-mail and password
        try {
            foundUser = userProvider.getUserByEmail(authUser.getEmail());
        } catch (Exception e) {

            log.info("Authentication completed - User not authorized");

            return Response.status(401).type("plain/text").entity("User not authorized").build();
        }
        checkHashed = Auth.hashPassword(authUser.getPassword(), foundUser.getSalt());

        //Creating if-else statement to check if the hashed password equals the password of a specific user.
        if (checkHashed.equals(foundUser.getPassword())) {
            try {
                Algorithm algorithm = Algorithm.HMAC256(Config.getJwtSecret());
                long timevalue;
                timevalue = (System.currentTimeMillis() * 1000) + 20000205238L;
                expDate = new Date(timevalue);

                token = JWT.create().withClaim("email", foundUser.getEmail()).withKeyId(String.valueOf(foundUser.getId()))
                        .withExpiresAt(expDate).withIssuer("ROFL").sign(algorithm);
                // tokenArray.add(token);
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            } catch (JWTCreationException e) {

                log.error("Error in JWT creation", e);

                e.printStackTrace();
            }

            User userInfo = new User();

            try {
                userInfo = userProvider.getUser(foundUser.getId());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            JsonObject resJson = new JsonObject();
            JsonObject userJson = new JsonObject();

            userJson.addProperty("id", userInfo.getId());
            userJson.addProperty("firstName", userInfo.getFirstName());
            userJson.addProperty("lastName", userInfo.getLastName());
            userJson.addProperty("email", userInfo.getEmail());

            resJson.addProperty("token", token);
            resJson.add("user", userJson);
            resJson.addProperty("exp", expDate.toString());

            log.info("Authentication completed - User Authorized");

            return Response.status(200).type("application/json").entity(resJson.toString()).build();


        } else {

            log.info("Authentication completed - User not authorized");

            return Response.status(401).type("plain/text").entity("User not authorized").build();



        }
    }

}
