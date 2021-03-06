package com.mr_faton.message.impl;

import com.mr_faton.message.MessageAPI;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Map;
import java.util.Properties;

/**
 * Created by root on 05.06.2015.
 */
public class EmailMessenger implements MessageAPI {
    private String mailFrom;
    private String mailFromPassword;
    private String mailTo;
    private static String image = "temp.jpg";



    @Override
    public void sendCaptcha() throws Exception{


        String subject = "New captcha!";

        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtps");
        properties.put("mail.smtps.auth", "true");
        properties.put("mail.smtps.host", "smtp.gmail.com");
        properties.put("mail.smtps.user", mailFrom);
        properties.put("password", mailFromPassword);



        Session mailSession = Session.getDefaultInstance(properties);
        mailSession.getDebug();
        MimeMessage message = new MimeMessage(mailSession);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
        message.setSubject(subject, "UTF-8");

        MimeMultipart multipart = new MimeMultipart("related");

        // first part  (the html)
        BodyPart messageBodyPart = new MimeBodyPart();
        String htmlText = "<H1>Resolve captcha please</H1><img src=\"cid:image\">";
        messageBodyPart.setContent(htmlText, "text/html");

        // add it
        multipart.addBodyPart(messageBodyPart);

        // second part (the image)
        messageBodyPart = new MimeBodyPart();
        DataSource fds = new FileDataSource(image);
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

    @Override
    public String getSolvedCaptcha() throws Exception{
        String solvedCaptcha = null;

        String imapHost = "imap.gmail.com";

        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        try {
            Session session = Session.getInstance(props, null);
            Store store = session.getStore("imaps");
            store.connect(imapHost, mailFrom, mailFromPassword);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            Message messages[] = inbox.getMessages();

            for (Message message : messages) {
                solvedCaptcha = message.getSubject();
                System.out.println(solvedCaptcha);
                System.out.println("delete message...");
                message.setFlag(Flags.Flag.DELETED, true);
            }

        } catch (Exception mex) {
            mex.printStackTrace();
        }
        return solvedCaptcha;
    }

    @Override
    public void setMailFrom(Map<String, String> mapMailFrom) {
        mailFrom = mapMailFrom.get("login");
        mailFromPassword = mapMailFrom.get("password");
    }

    @Override
    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }
}
class Test {
    public static void main(String[] args) throws Exception {
        MessageAPI email = new EmailMessenger();

//        email.typeCaptcha();

        email.getSolvedCaptcha();
    }
}