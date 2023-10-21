package server.db_operations.intefaces;

import java.sql.SQLException;
import java.util.List;

public interface FileOperationsInDB {
    public void addFile(String filename, int userId);

    public boolean deleteFile(String filename);

    public List<String> listOfFiles() throws SQLException;

}
