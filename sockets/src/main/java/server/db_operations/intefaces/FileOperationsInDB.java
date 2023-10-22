package server.db_operations.intefaces;

import java.sql.SQLException;
import java.util.List;

public interface FileOperationsInDB {
    void addFile(String filename, int userId);

    boolean deleteFile(String filename);

    List<String> listOfFiles() throws SQLException;

}
