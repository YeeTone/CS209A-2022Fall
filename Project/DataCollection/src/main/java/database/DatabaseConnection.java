package database;

import task1.Query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static Connection connection;

    public static Connection getInstance() {
        if(connection == null){
            try{
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(Query.JDBC);
                connection.prepareStatement("PRAGMA foreign_keys = ON").execute();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return connection;
    }
}
