import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Properties;

public class Gmail {
    Session newSession = null;
    MimeMessage mimeMessage = null;

    public static void sendMail(String email_id, String subject, String body) throws MessagingException, IOException {
        Gmail mail = new Gmail();
        mail.setupServerProperties();
        mail.draftEmail(email_id, subject, body);
        mail.send();
    }

    public void setupServerProperties() {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");
        newSession = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("your-email@gmail.com", "your-password");
            }
        });
    }

    public MimeMessage draftEmail(String email_id, String subject, String body) throws AddressException, MessagingException, IOException {
        String[] emailRecipients = {email_id};
        String emailSubject = subject;
        String emailBody = body;
        mimeMessage = new MimeMessage(newSession);

        for (int i = 0; i < emailRecipients.length; i++) {
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(emailRecipients[i]));
        }
        mimeMessage.setSubject(emailSubject);

        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(emailBody, "text/html");
        Multipart multiPart = new MimeMultipart();
        multiPart.addBodyPart(bodyPart);
        mimeMessage.setContent(multiPart);

        return mimeMessage;
    }

    public void send() throws MessagingException {
        String fromUser = "virtualsandha@gmail.com";
        String fromUserPassword = "hivmgtzwavzausaa";

        Transport transport = newSession.getTransport("smtp");
        transport.connect("smtp.gmail.com", fromUser, fromUserPassword);
        transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
        transport.close();

        System.out.println("Email successfully sent!");
    }
}

//import javax.mail.Message;
//import javax.mail.internet.InternetAddress;
//import javax.mail.MessagingException;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.AddressException;
//import javax.mail.internet.MimeBodyPart;
//import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimeMultipart;
//import java.io.IOException;
//import java.util.*;
//public class Gmail {
//    Session newSession = null;
//    MimeMessage mimeMessage = null;
//
//    public static void main(String [] args) throws MessagingException, IOException {
//        Gmail mail = new Gmail();
//        mail.setupServerProperties();
//        mail.draftEmail();
//        mail.sendEmail();
//    }
//    public void setupServerProperties() {
//        Properties properties = System.getProperties();
//        properties.put("mail.smtp.port", "547");
//        properties.put("mail.smtp.auth", "true");
//        properties.put("mail.smtp.starttls.enable", "true");
//        newSession = Session.getDefaultInstance(properties, null);
//    }
//    public MimeMessage draftEmail() throws AddressException, MessagingException, IOException {
//        String[] emailRecipients = {"aravindofficial2722@gmail.com", "rajdweepmondal@gmail.com"};
//        String emailSubject = "Testing";
//        String emailBody = "Finally I am able to send email through java, and this is one of which!";
//        mimeMessage = new MimeMessage(newSession);
//
//        for (int i = 0; i < emailRecipients.length; i++) {
//            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(emailRecipients[i]));
//        }
//        mimeMessage.setSubject(emailSubject);
//
//        MimeBodyPart bodyPart = new MimeBodyPart();
//        bodyPart.setContent(emailBody, "html/text");
//        MimeMultipart multiPart = new MimeMultipart();
//        multiPart.addBodyPart(bodyPart);
//        mimeMessage.setContent(multiPart);
//        return mimeMessage;
//    }
//    public void sendEmail() throws MessagingException {
//        String fromUser = "arijitaravind6@gmail.com";
//        String fromUserPassword = "Aravind@chrome.com";
//        String emailHost = "smtp.gmail.com";
//        Transport transport = newSession.getTransport("smtp");
//        transport.connect(emailHost, fromUser, fromUserPassword);
//        transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
//        transport.close();
//        System.out.println("Email successfully sent!");
//    }
//}
