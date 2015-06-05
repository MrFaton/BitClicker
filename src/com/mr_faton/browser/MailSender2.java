package com.mr_faton.browser;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

/**
 * Created by root on 05.06.2015.
 */
public class MailSender2 {
    public static void sendMail() throws Exception{
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtps");
        properties.put("mail.smtps.auth", "true");
        properties.put("mail.smtps.host", "smtp.gmail.com");
        properties.put("mail.smtps.user", "themrfaton@gmail.com");
        properties.put("password", "907257q2y");

        String from = "USAPresident@gmail.com";
//        String to = "genyka@gmail.com";
        String to = "firefly90@inbox.ru";
        String subject = "Приглашение!";
        String text = "Привет Жека! Приезжай к нам в штаты скорей!";

        Session mailSession = Session.getDefaultInstance(properties);
        mailSession.getDebug();
        MimeMessage message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);

        MimeMultipart multipart = new MimeMultipart("related");
        // first part  (the html)
        BodyPart messageBodyPart = new MimeBodyPart();
        String htmlText = "<H1>Hello</H1><img src=\"cid:image\">";
        messageBodyPart.setContent(htmlText, "text/html");

        // add it
        multipart.addBodyPart(messageBodyPart);

        // second part (the image)
        messageBodyPart = new MimeBodyPart();
        DataSource fds = new FileDataSource("C:\\ttttttttttt\\img2.jpg");
        messageBodyPart.setDataHandler(new DataHandler(fds));
        messageBodyPart.setHeader("Content-ID","<image>");

        // add it
        multipart.addBodyPart(messageBodyPart);

        message.setContent(multipart);

        Transport transport = mailSession.getTransport();
        try {
            transport.connect(null, properties.getProperty("password"));
            transport.sendMessage(message, message.getAllRecipients());
        } finally {
            transport.close();
        }
    }
}

class Test2 {
    public static void main(String[] args) throws Exception {
        MailSender2 mailSender2 = new MailSender2();
        mailSender2.sendMail();
    }
}