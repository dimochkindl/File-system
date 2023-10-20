package client.frames;

import users.User;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private final JTextField usernameField;
    private final JTextField emailField;
    private final JTextField cityField;
    private final JButton sendCodeButton;
    User user;

    public RegisterFrame() {
        setTitle("Registration");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        JLabel usernameLabel = new JLabel("Username: ");
        JLabel emailLabel = new JLabel("Email: ");
        JLabel cityLabel = new JLabel("Password:");

        usernameField = new JTextField(15);
        emailField = new JTextField(15);
        cityField = new JTextField(15);
        sendCodeButton = new JButton("Register");

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
        contentPane.add(cityField, gbc);

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
        gbc.anchor = GridBagConstraints.CENTER;
        contentPane.add(sendCodeButton, gbc);

        setContentPane(contentPane);
        pack();

        sendCodeButton.addActionListener(e -> {

        });

    }

/*    private void sendCode() throws IOException, MessagingException {
        User user = new User(usernameField.getText(), emailField.getText(), cityField.getText());
        EmailSender emailSender = new EmailSender();
        emailSender.sendEmail();
        dispose();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    new EmailFrame();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }*/

}
