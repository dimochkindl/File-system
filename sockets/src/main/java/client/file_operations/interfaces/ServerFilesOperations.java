package client.file_operations.interfaces;

import java.util.List;

public interface ServerFilesOperations {
    String uploadFile(String filename);
    String downloadFile(String filename);
    boolean deleteFile(String filename);
    List<String> readFile(String filename);
    boolean writeFile(String filename, String text);
}
