package server.db_operations;

import org.apache.tika.Tika;
import server.db_operations.intefaces.IFileOperationsInDB;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FileManagerDatabase implements IFileOperationsInDB {

    private Connector connector;

    public FileManagerDatabase() {
        connector = new Connector();
    }

    @Override
    public void addFile(String filename, int userId) {
        Connection connection = connector.getConnection();
        PreparedStatement statement = null;
        try {
            String query = "INSERT INTO files_info (user_id, filename, type, date) VALUES(?, ?, ?, ?) ";
            statement = connection.prepareStatement(query);
            String fileType = getFileType("client"+File.separator+filename);
            Date date = new Date(System.currentTimeMillis());
            statement.setInt(1, userId);
            statement.setString(2, filename);
            statement.setString(3, fileType);
            statement.setDate(4, date);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Entry added successfully");
            } else {
                System.out.println("Failed to add entry");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getSQLState());
        }
    }

    @Override
    public void deleteFile() {

    }

    @Override
    public List<String> listOfFiles() throws SQLException {
        Connection connection = connector.getConnection();
        PreparedStatement statement = null;
        List<String> filenames = new ArrayList<>();
        try {
            String query = "SELECT filename FROM files_info;";
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String filename = resultSet.getString("filename");
                System.out.println("Filename: " + filename);
                filenames.add(filename);
            }
            return filenames;

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getSQLState());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return filenames;
    }


    private String getFileType(String filename) {
        Tika tika = new Tika();
        try {
            String type = tika.detect(new File(filename));
            return type;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "error";
    }

    public void increaseDownloadFiles() {
        Connection connection = connector.getConnection();
        PreparedStatement statement = null;
        String query = "UPDATE users SET download_files = download_files + 1";
        try{
            statement = connection.prepareStatement(query);
            int rowsUpdated = statement.executeUpdate();
            System.out.println("Download_files++:\nRows updated: " + rowsUpdated);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void increaseUploadFiles(){
        Connection connection = connector.getConnection();
        PreparedStatement statement = null;
        String query = "UPDATE users SET upload_files = upload_files + 1";
        try{
            statement = connection.prepareStatement(query);
            int rowsUpdated = statement.executeUpdate();
            System.out.println("Upload files++:\nRows updated: " + rowsUpdated);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
