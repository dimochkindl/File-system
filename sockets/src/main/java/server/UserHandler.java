package server;

import server.files_operations.OperationsHandler;
import users.UserAuthenticator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class UserHandler implements Runnable {

    private final Socket socket;
    private int userId;
    private DataOutputStream out;
    private DataInputStream in;

    public UserHandler(Socket socket) {
        this.socket = socket;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("ReassignedVariable")
    public void run() {
        try {
            String command = in.readUTF();
            if (command.equals("login")) {
                handleLogin();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void handleLogin() throws IOException {
        String register = in.readUTF();
        if(register.equals("register")){
            handleRegister();
        }
        UserAuthenticator userAuthenticator = new UserAuthenticator(register, in.readUTF());
        if (!userAuthenticator.checkUser()) {
            out.writeUTF("unknown");
            out.flush();
            run();
        } else {
            out.writeUTF("confirmed");
            out.flush();
            userId = userAuthenticator.getUserId();
            switchCommands();
        }
    }

    private void handleRegister() {
        try {
            UserAuthenticator userAuthenticator = new UserAuthenticator(in.readUTF(), in.readUTF());
            String email = in.readUTF();
            if (!userAuthenticator.registerUser(email)) {
                out.writeUTF("fail");
                out.flush();
                run();
            } else {
                out.writeUTF("success");
                out.flush();
                userId = userAuthenticator.getUserId();
                switchCommands();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void switchCommands() throws IOException {
        OperationsHandler handler = new OperationsHandler(in, out, userId, socket);
        while (true) {
            String command = in.readUTF();
            if ("upload".equals(command)) {
                handler.upload();
            }
            if ("download".equals(command)) {
                handler.download();
            }
            if ("read".equals(command)) {
                handler.read();
            }
            if ("write".equals(command)) {
                handler.write();
            }
            if ("delete".equals(command)) {
                handler.delete();
            }
            if ("showFilesList".equals(command)) {
                handler.showServerFilesList();
            }
            if ("update".equals(command)) {
                handler.update();
            }
        }
    }

}
