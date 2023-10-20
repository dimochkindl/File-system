package server.files_operations;

import java.io.IOException;

public interface FilesOperations {
    public int upload() throws IOException;
    public void download() throws IOException;
    public void delete() throws IOException;
    public void read() throws IOException;
    public void write() throws IOException;
}
