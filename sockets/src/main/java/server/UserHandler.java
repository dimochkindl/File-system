package server;

import server.files_operations.OperationsHandler;
import users.UserAuthenticator;

import java.io.*;
import java.net.Socket;

public class UserHandler implements Runnable {

    private int userId;

    private final Socket socket;

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
            while (true) {
                String command = in.readUTF();
                switch (command) {
                    case "login": {
                        handleLogin();
                        break;
                    }
                    case "register": {
                        handleRegister();
                        break;
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void handleLogin() throws IOException {
        UserAuthenticator userAuthenticator = new UserAuthenticator(in.readUTF(), in.readUTF());
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

    private void handleRegister(){

    }

    private void switchCommands() throws IOException {
        OperationsHandler handler = new OperationsHandler(in,out, userId, socket);
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

            }
            if ("delete".equals(command)) {
                handler.delete();
            }
            if("showFilesList".equals(command)){
                handler.showServerFilesList();
            }
        }
    }

}
