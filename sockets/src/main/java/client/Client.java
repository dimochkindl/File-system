package client;

import client.frames.LoginFrame;
import client.frames.MainFrame;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;


    public Client() throws IOException {
        socket = new Socket("localhost", 1234);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        login();
    }

    public static void main(String[] args) throws IOException {
        new Client();
    }

    private void runClient() {
        SwingUtilities.invokeLater(() -> new MainFrame(socket).setVisible(true));
    }

    private void login() {
        try {
            out.writeUTF("login");
            out.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        LoginFrame loginFrame = new LoginFrame(socket, in, out);
        SwingUtilities.invokeLater(() -> loginFrame.setVisible(true));

        while (loginFrame.isDisplayable()) {
        }
        runClient();
    }

    private void register() {
        try {
            out.writeUTF("register");
            out.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        LoginFrame loginFrame = new LoginFrame(socket, in, out);
        SwingUtilities.invokeLater(() -> loginFrame.setVisible(true));

        while (loginFrame.isDisplayable()) {
        }
        runClient();
    }
}
