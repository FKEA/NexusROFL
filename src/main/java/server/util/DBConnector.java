package server.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 * Created by Filip on 21-12-2017.
 */
public class DBConnector {

    private Connection connection;

    private Logger log = Logger.getLogger(DBConnector.class);

    public DBConnector() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            connection = DriverManager.getConnection(
                    "jdbc:mysql://"
                            + Config.getDatabaseHost() + ":"
                            + Config.getDatabasePort() + "/"
                            + Config.getDatabaseName() + "?useSSL=false&serverTimezone=GMT",
                    Config.getDatabaseUser(),
                    Config.getDatabasePassword());

        } catch (InstantiationException e) {
            log.error("Could not connect to database", e);
        } catch (IllegalAccessException e) {
            log.error("Could not get access to database", e);
        } catch (ClassNotFoundException e) {
            log.error("Could not find JDBC driver", e);
        } catch (SQLException e) {
            log.error("Database connection error", e);
        }
    }

    public Connection getConnection() {

        return connection;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
