package client.file_operations;

import client.file_operations.interfaces.ClientFilesOperations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClientFilesManager implements ClientFilesOperations {
    @Override
    public List<String> showUserFiles() {
        List<String> userFiles = new ArrayList<>();

        File directory = new File("client" + File.separator);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        userFiles.add(file.getName());
                    }
                }
            } else {
                System.err.println("Empty directory");
            }
        } else {
            System.err.println("No such directory");
        }

        return userFiles;
    }

    @Override
    public boolean deleteFile(String filename) {
        return false;
    }

    @Override
    public List<String> readFile(String filename) {
        return null;
    }

    @Override
    public List<String> writeFile(String filename) {
        return null;
    }
}
