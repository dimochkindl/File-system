package client.file_operations.interfaces;

import java.util.List;

public interface ClientFilesOperations {
    List<String> showUserFiles();
    boolean deleteFile(String filename);
    List<String> readFile(String filename);
    List<String> writeFile(String filename);
}
