package client.frames;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;

public class EmailFrame extends JFrame {
    private JFormattedTextField codeField;
    private JButton confirmButton;

    EmailFrame() throws ParseException {
        setTitle("Confirm the email");
        setLocationRelativeTo(null);
        confirmButton = new JButton("Confirm");

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        codeField = new JFormattedTextField();
        JButton button = new JButton("Submit");

        MaskFormatter codeMask = new MaskFormatter("####");
        codeMask.install(codeField);

        panel.add(codeField);
        panel.add(button);

        add(panel);
        pack();
        setVisible(true);
    }
}
