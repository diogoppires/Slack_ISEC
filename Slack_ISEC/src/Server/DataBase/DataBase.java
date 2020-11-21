package Server.DataBase;

import java.sql.*;

public class DataBase {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    public boolean connectDB(String ip, String dbName) {
        try {
            Class.forName(JDBC_DRIVER);
            String dbAddress = ip;
            String dbUser = "root";
            String dbPass = "root";
            String dbTable = "ServerData";
            // Connect to Server
            String dbURL = "jdbc:mysql://" + dbAddress + "/?useTimezone=true&serverTimezone=UTC";
            conn = DriverManager.getConnection(dbURL, dbUser, dbPass);
            stmt = conn.createStatement();
            System.out.println(dbName);
            // Create DataBase and Table
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
            stmt.executeUpdate("USE " + dbName);
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + dbTable + "("
                    + "id INT NOT NULL AUTO_INCREMENT, "
                    + "address TEXT NOT NULL, "
                    + "port INT NOT NULL, "
                    + "PRIMARY KEY (id))");
        } catch (ClassNotFoundException | SQLException sqlEx) {
            System.out.println(sqlEx);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }
        return true;
    }
}
