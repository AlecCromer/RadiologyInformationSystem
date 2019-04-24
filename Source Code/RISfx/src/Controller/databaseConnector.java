package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class databaseConnector {

    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String CONN_STRING = "jdbc:mysql://localhost:3306/RIS";


    /**
     * connection  method that connects us to the MySQL database
     * @return Connection returns the Driver Manager Connection
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
    }

    /**
     * initializes the connection to the database
     * @return Connection this returns the connection to the database from the DriverManager
     */
    public static Connection getStartConnection() throws SQLException {
        System.out.println("Connecting to Database...");
        Connection conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
        System.out.println("Success!");
        return conn;
    }

    /**
     * This method is used to add two integers. This is
     * @param ex Takes the SQL exception
     */
    public static void displayException(SQLException ex) {
        System.err.println("Error Message: " + ex.getMessage());
        System.err.println("Error Code: " + ex.getErrorCode());
        System.err.println("SQL Status: " + ex.getSQLState());
    }
}
