package server.db_operations;

import server.db_operations.intefaces.ITablesCreator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Connector implements ITablesCreator {

    public Connection getConnection() {
        final String url = "jdbc:postgresql://localhost:5432/file_manager_system";
        final String password = "lilasgard228";
        final String username = "postgres";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (SQLException e) {
            System.out.println("Can't connect to db");
        }
        return null;
    }

    public void createTableUsers(){
        Connection connection = getConnection();
        Statement statement = null;
        try{
            statement = connection.createStatement();
            String query = "CREATE TABLE users (" +
                    "id SERIAL PRIMARY KEY," +
                    "username VARCHAR(30)," +
                    "upload_files INT," +
                    "download_files INT)";
            statement.executeUpdate(query);
            System.out.println("Table users created");
        }catch (SQLException ex){
            System.out.println("Can't create statement or table users");
        }
    }

    public void createTableUsersInfo(){
        Connection connection = getConnection();
        Statement statement = null;
        try{
            statement = connection.createStatement();
            String query = "CREATE TABLE users_info (" +
                    "user_id INT PRIMARY KEY REFERENCES users(id)," +
                    "email VARCHAR(30)," +
                    "password VARCHAR(15))";
            statement.executeUpdate(query);
            System.out.println("Table users_info created");
        }catch (SQLException ex){
            System.out.println("Can't create statement or table users_info");
        }
    }

    public void creteTableFilesInfo(){
        Connection connection = getConnection();
        Statement statement = null;
        try{
            statement = connection.createStatement();
            String query = "CREATE TABLE files_info (" +
                    "user_id INT REFERENCES users(id)," +
                    "filename VARCHAR(30)," +
                    "type VARCHAR(10)," +
                    "date DATE)";
            statement.executeUpdate(query);
            System.out.println("Table files_info created");
        }catch (SQLException ex){
            System.out.println("Can't create statement or table files_info");
        }
    }
}
