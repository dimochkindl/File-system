package server.db_operations.intefaces;

import java.sql.SQLException;
import java.util.List;

public interface IFileOperationsInDB {
    public void addFile(String filename, int userId);

    public void deleteFile();

    public List<String> listOfFiles() throws SQLException;

}
