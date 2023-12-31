package client.file_operations;

import client.file_operations.interfaces.ServerFilesOperations;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FilesManager implements ServerFilesOperations {

    private final Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public FilesManager(Socket socket) {
        this.socket = socket;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String uploadFile(String filename) {
        try {
            File file = new File("client" + File.separator + filename);
            if (file.exists()) {
                out.writeUTF("upload");
                out.writeUTF(filename);
                String existsFile = in.readUTF();
                if (existsFile.equals("exists")) {
                    return null;
                }
                long size = file.length();
                out.writeLong(size);
                FileInputStream fis = new FileInputStream(file);
                int read = 0;
                byte[] buffer = new byte[256];
                while ((read = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                out.flush();
                String status = in.readUTF();
                return status;
            } else {
                return "File doesn't exist";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Some Error";
    }

    @Override
    public String downloadFile(String filename) {
        try {
            out.writeUTF("download");
            out.writeUTF(filename);
            Boolean serverConsistsFile = in.readBoolean();
            if (!serverConsistsFile) {
                return "doesn'tExistsOnServer";
            }

            File file = new File("client" + File.separator + filename);
            if (!file.exists()) {
                file.createNewFile();
            } else {
                return "alreadyExists";
            }

            long size = in.readLong();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[256];
            for (int i = 0; i < (size + 255) / 256; i++) {
                int read = in.read(buffer);
                fos.write(buffer, 0, read);
            }
            fos.close();
            Integer fileSize = (int) file.length();
            return fileSize.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteFile(String filename) {
        try {
            out.writeUTF("delete");
            out.writeUTF(filename);
            Boolean serverConsistsFile = in.readBoolean();
            if (!serverConsistsFile) {
                System.out.println("Server doesn't consists file(deleteFile - FilesManager)");
                return false;
            }
            if (in.readBoolean()) {
                return true;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public List<String> readFile(String filename) {
        List<String> readData = new LinkedList<>();
        try {
            out.writeUTF("read");
            out.writeUTF(filename);
            int bytesRead;
            if ((bytesRead = in.readInt()) != -1) {
                byte[] buffer = new byte[bytesRead];
                in.readFully(buffer);
                String text = new String(buffer, StandardCharsets.UTF_8);
                String[] textLines = text.split(System.lineSeparator());
                readData.addAll(Arrays.asList(textLines));
            }else{
                return null;
            }
            return readData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean writeFile(String filename, String text) {
        try{
            out.writeUTF("write");
            out.writeUTF(filename);
            out.writeUTF(text);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateFile(String filename, String text) {
        try{
            out.writeUTF("update");
            out.writeUTF(filename);
            out.writeUTF(text);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String> showFiles() {

        try {
            out.writeUTF("showFilesList");
            List<String> filenames = new ArrayList<>();
            int numberOfFiles = in.readInt();
            for (int i = 0; i < numberOfFiles; i++) {
                String filename = in.readUTF();
                filenames.add(filename);
            }
            return filenames;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
