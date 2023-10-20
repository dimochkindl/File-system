package client;

import client.frames.LoginFrame;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class Client2 {
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;


    public Client2() throws IOException {
        socket = new Socket("localhost", 1234);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        //runClient();
        login();
    }

    public static void main(String[] args) throws IOException {
        new Client();
    }

    private void runClient() {
        JFrame frame = new JFrame("Cloud Storage");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(400, 300);

        JTextArea ta = new JTextArea();
        JButton uploadButton = new JButton("Upload");

        frame.getContentPane().add(BorderLayout.CENTER, ta);
        frame.getContentPane().add(BorderLayout.SOUTH, uploadButton);

        frame.setVisible(true);

        uploadButton.addActionListener(a -> {
            System.out.println(sendFile(ta.getText()));
        });
    }

    private String sendFile(String filename) {
        try {
            File file = new File("client" + File.separator + filename);
            if (file.exists()) {
                out.writeUTF("upload");
                out.writeUTF(filename);
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
                return "File isn't exist";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Some Error";
    }

    private void login() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginFrame(socket, in, out).setVisible(true);
            }
        });
    }
}
