package server.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Source code in this class taken from:
 * https://github.com/Distribuerede-Systemer-2017/
 *      STFU-new/blob/master/src/main/java/server/config/Config.java
 *
 * Created by Filip on 18-10-2017.
 */
public final class Config {

    private static String DATABASE_HOST;
    private static String DATABASE_PORT;
    private static String DATABASE_NAME;
    private static String DATABASE_USER;
    private static String DATABASE_PASSWORD;

    private static String JWT_SECRET;

    private static boolean SECURE_ENDPOINTS;

    public Config() throws IOException {

        JsonObject jsonConfig = new JsonObject();

        //Filen hentes i Inputstream
        //config.java åbnes for at kunne læses
        //Bufferedreader læser streamen igennem
        InputStream input = this.getClass().getClassLoader().getResourceAsStream("/config.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        //Stringbuffer bruges til at samle hele filen i en streng
        StringBuffer stringBuffer = new StringBuffer();
        String str = "";

        //Filen læses en linje ad gangen og indlæses i stringbuffer
        while((str = reader.readLine()) != null){
            stringBuffer.append(str);
        }
        JsonParser parser = new JsonParser();

        jsonConfig = (JsonObject) parser.parse(stringBuffer.toString());

        DATABASE_HOST = jsonConfig.get("DATABASE_HOST").getAsString();
        DATABASE_PORT = jsonConfig.get("DATABASE_PORT").getAsString();
        DATABASE_NAME = jsonConfig.get("DATABASE_NAME").getAsString();
        DATABASE_USER = jsonConfig.get("DATABASE_USER").getAsString();
        DATABASE_PASSWORD = jsonConfig.get("DATABASE_PASSWORD").getAsString();

        JWT_SECRET = jsonConfig.get("JWT_SECRET").getAsString();

        SECURE_ENDPOINTS = jsonConfig.get("SECURE_ENDPOINTS").getAsBoolean();

        reader.close();
        input.close();

    }

    public static String getDatabaseHost() {
        return DATABASE_HOST;
    }

    public static String getDatabasePort() {
        return DATABASE_PORT;
    }

    public static String getDatabaseName() {
        return DATABASE_NAME;
    }

    public static String getDatabaseUser() {
        return DATABASE_USER;
    }

    public static String getDatabasePassword() {
        return DATABASE_PASSWORD;
    }

    public static String getJwtSecret() { return JWT_SECRET; }

    public static boolean getSecureEndpoints() { return SECURE_ENDPOINTS; }
}
