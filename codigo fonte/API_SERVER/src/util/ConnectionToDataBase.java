package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionToDataBase {
    
    public static final String url = "jdbc:postgresql://localhost:5432/chat-application";
    public static final String user = "postgres";
    public static final String password = "paulo123";
    public static Connection connection;

    public static Connection getConnection(){
        try{
            if (connection == null){
                connection = DriverManager.getConnection(url, user, password);
            }
            return connection;
        }
        
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
