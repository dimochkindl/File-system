package server.files_operations;

import server.db_operations.FileManagerDatabase;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

public class OperationsHandler implements FilesOperations {

    private final int userId;
    private final Socket socket;
    private final FileManagerDatabase fileManagerDatabase;
    private final DataInputStream in;
    private final DataOutputStream out;

    public OperationsHandler(DataInputStream in, DataOutputStream out, int userId, Socket socket) {
        this.userId = userId;
        this.in = in;
        this.out = out;
        this.socket = socket;
        fileManagerDatabase = new FileManagerDatabase();
    }

    @Override
    public int upload() throws IOException {
        String filename = in.readUTF();
        File file = new File("server" + File.separator + filename);
        if (!file.exists()) {
            file.createNewFile();
            out.writeUTF("ok");
        } else {
            out.writeUTF("exists");
            return 1;
        }

        long size = in.readLong();
        FileOutputStream fos = new FileOutputStream(file);
        byte[] buffer = new byte[256];
        for (int i = 0; i < (size + 255) / 256; i++) {
            int read = in.read(buffer);
            fos.write(buffer, 0, read);
        }
        fos.close();
        fileManagerDatabase.addFile(filename, userId);
        fileManagerDatabase.increaseUploadFiles();
        out.writeUTF("Done");

        return 0;
    }

    @Override
    public void download() {
        try {
            String filename = in.readUTF();
            List<String> availableFiles = fileManagerDatabase.listOfFiles();
            out.writeBoolean(availableFiles.contains(filename));
            File file = new File("server" + File.separator + filename);
            if (!file.exists()) {
                {
                    throw new IOException("Can't create file(OperationsHandler.download)");
                }
            }

            out.writeLong(file.length());
            FileInputStream fileInputStream = new FileInputStream(file);
            int read = 0;
            byte[] buffer = new byte[256];
            while ((read = fileInputStream.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            fileInputStream.close();
            out.flush();
            fileManagerDatabase.increaseDownloadFiles();

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete() {
        try {
            String filename = in.readUTF();
            List<String> availableFiles = fileManagerDatabase.listOfFiles();
            out.writeBoolean(availableFiles.contains(filename));
            File file = new File("server" + File.separator + filename);
            boolean fromServer = false;
            if (file.exists()) {
                fromServer = file.delete();
            } else {
                System.out.println("file doesn't exists on server site");
            }
            out.writeBoolean((fileManagerDatabase.deleteFile(filename) && fromServer));

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    @Override
    public void read() throws IOException {
        String filename = in.readUTF();
        File file = new File("server" + File.separator + filename);
        FileInputStream fis = new FileInputStream(file);
        int length = (int) file.length();
        byte[] buffer = new byte[length];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
            out.writeInt(bytesRead);
            out.write(buffer, 0, bytesRead);
        }

    }


    @Override
    public void write() {
        File file = null;
        String text = null;
        try {
            file = new File("server" + File.separator + in.readUTF());
            text = in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try (FileOutputStream fos = new FileOutputStream(file, true)) {
            byte[] toWrite = text.getBytes("UTF-8");
            fos.write(toWrite);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() throws IOException {
        File file = null;
        String text = null;
        try {
            file = new File("server" + File.separator + in.readUTF());
            text = in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileOutputStream fos = new FileOutputStream(file, false)) {
            byte[] toWrite = text.getBytes("UTF-8");
            fos.write(toWrite);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showServerFilesList() {
        List<String> filesList;
        try {
            filesList = fileManagerDatabase.listOfFiles();
            int size = filesList.size();
            out.writeInt(size);

            for (String name : filesList) {
                out.writeUTF(name);
            }
        } catch (SQLException | IOException e) {
            System.out.println("error in show files");
        }
    }

}
