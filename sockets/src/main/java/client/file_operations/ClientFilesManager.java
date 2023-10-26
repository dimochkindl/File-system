package client.file_operations;

import client.file_operations.interfaces.ClientFilesOperations;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
        File file = new File("client"+File.separator+filename);
        if(file.exists()){
            return file.delete();
        }else{
            System.out.println("doesn't exists");
        }
        return false;
    }

    @Override
    public List<String> readFile(String filename) {
        File file = new File("client" + File.separator+ filename);
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(file));
            List<String> readFile = new LinkedList<>();
            String line;
            while ((line = fileReader.readLine()) != null) {
                readFile.add(line);
            }
            return readFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void writeFile(String filename, String text) {
        File file = new File("client" + File.separator + filename);
        try (FileOutputStream fos = new FileOutputStream(file, true)) {
            byte[] toWrite = text.getBytes("UTF-8");
            fos.write(10);
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
    public void updateFile(String filename, String text) {
        File file = new File("client" + File.separator + filename);
        try (FileOutputStream fos = new FileOutputStream(file, false)) {
            byte[] toWrite = text.getBytes("UTF-8");
            fos.write(10);
            fos.write(toWrite);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
