package client.frames;

import users.User;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class RegisterFrame extends JFrame {
    private final JTextField usernameField;
    private final JTextField emailField;
    private final JTextField passwordField;
    private final JButton registerButton;

    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;

    User user;

    public RegisterFrame(Socket socket, DataInputStream in, DataOutputStream out) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        setTitle("Registration");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        JLabel usernameLabel = new JLabel("Username: ");
        JLabel emailLabel = new JLabel("Email: ");
        JLabel cityLabel = new JLabel("Password:");

        usernameField = new JTextField(15);
        emailField = new JTextField(15);
        passwordField = new JTextField(15);
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
        contentPane.add(cityLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        contentPane.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        contentPane.add(emailLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        contentPane.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER; // Центрировать кнопку
        contentPane.add(registerButton, gbc);

        setContentPane(contentPane);
        pack();
        usernameField.setEnabled(true);
        emailField.setEnabled(true);
        passwordField.setEnabled(true);
        registerButton.addActionListener(e -> performRegister());
    }


    private void performRegister() {
        try {
            out.writeUTF("register");
            out.writeUTF(usernameField.getText());
            out.writeUTF(passwordField.getText());
            out.writeUTF(emailField.getText());
            out.flush();
            String answer = in.readUTF();
            if (answer.equals("fail")) {
                Error();
            } else if (answer.equals("success")) {
                SwingUtilities.invokeLater(() -> new MainFrame(socket).setVisible(true));
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void Error() {
        JOptionPane.showMessageDialog(this, "Error adding new user", "Error", JOptionPane.ERROR_MESSAGE);
    }

}
