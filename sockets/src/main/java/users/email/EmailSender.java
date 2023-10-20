package users.email;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {
    private final String username = "dikual007@gmail.com";
    private final String password = "ahhlgfzgmlxoovvk";

    public void sendEmail() throws MessagingException {
        String host = "smtp.gmail.com";
        int port = 587;

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.mailtrap.io");
        prop.put("mail.smtp.port", "2525");
        prop.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");

        Session session = Session.getInstance(prop, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("dikur10@example.com"));
        message.setSubject("Email Subject");
        message.setText("Email Content");

        Transport.send(message);
        System.out.println("Email sent successfully");
    }

    public static void main(String[] args) {
        try {
            EmailSender emailSender = new EmailSender();
            emailSender.sendEmail();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

