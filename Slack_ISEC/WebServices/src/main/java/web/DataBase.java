package web;

import java.sql.*;

public class DataBase {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static Connection conn = null;
    private static Statement stmt = null;
    private static ResultSet rs = null;
    private static int serverID;
    private static String dbName;

    public static boolean connectDB(String ip, int updPort) {
        dbName = ip + updPort;
        serverID = updPort;
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
            stmt.executeUpdate("USE " + dbName);



        } catch (ClassNotFoundException | SQLException sqlEx) {
            System.out.println("DB: " + sqlEx);
            closeConnections();
        }
        return true;
    }
    private static void closeConnections() {

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
    public static boolean loginUser(String username, String password) {
        DataBase.connectDB("localhost", 9999);

        String query = "select * from users where username = '" + username + "'";

        boolean confirm = false;

        try {
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                //System.out.print(" Nome: "+rs.getString("name"));
                if (username.equals(rs.getString("username"))) {
                    confirm = true;
                }
                System.out.print("User: " + rs.getString("username"));
                System.out.println(" Pass: " + rs.getString("password"));
            }
        } catch (SQLException ex) {
            System.out.println("ERRO LOGIN: " + ex);
        }

        return confirm;
    }
    public static String searchMessages(int n) {
        StringBuilder output = new StringBuilder();
        try {
            int valor = n;
            String query = "select * from messages; ";// order by created DESC limit " + valor;
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                output
                        .append("[" + rs.getString("originuser") + "] ")
                        .append("[" + rs.getString("senduser") + "] ")
                        .append(rs.getString("message"))
                        .append("\n");
            }

        } catch (NumberFormatException ex) {
            return "ERROR" + ex;
        } catch (SQLException ex) {
            return "ERROR" + ex;
        }
        return output.toString();
    }
}
