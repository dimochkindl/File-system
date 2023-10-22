package client.frames;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class LoginFrame extends JFrame {
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;

    private final JButton registerButton;

    public LoginFrame(Socket socket, DataInputStream in, DataOutputStream out) {
        setResizable(false);
        this.socket = socket;
        this.in = in;
        this.out = out;


        setTitle("Enter");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel usernameLabel = new JLabel("Username: ");
        JLabel passwordLabel = new JLabel("Password: ");
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Enter");
        registerButton = new JButton("Register");

        JPanel contentPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);


        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        contentPane.add(usernameLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        contentPane.add(usernameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        contentPane.add(passwordLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        contentPane.add(passwordField, gbc);


        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        contentPane.add(loginButton, gbc);
        gbc.anchor = GridBagConstraints.LINE_END;
        contentPane.add(registerButton, gbc);

        setContentPane(contentPane);
        pack();

        usernameField.addActionListener(e -> passwordField.requestFocusInWindow());

        passwordField.addActionListener(e -> performLogin());


        loginButton.addActionListener(e -> performLogin());

        registerButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new RegisterFrame().setVisible(true));
        });
    }

    private void unknownUser() {
        JOptionPane.showMessageDialog(this, "Username and Password doesn't match", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void performLogin() {
        try {
            out.writeUTF(usernameField.getText());
            out.writeUTF(new String(passwordField.getPassword()));
            out.flush();
            String answer = in.readUTF();
            if (answer.equals("unknown")) {
                unknownUser();
            } else if (answer.equals("confirmed")) {
                dispose();
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
