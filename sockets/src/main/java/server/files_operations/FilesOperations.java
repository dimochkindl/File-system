package server.files_operations;

import java.io.IOException;

public interface FilesOperations {
    int upload() throws IOException;
    void download() throws IOException;
    void delete() throws IOException;
    void read() throws IOException;
    void write() throws IOException;
    void update() throws IOException;
}
