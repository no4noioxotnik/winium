package com.open.broker.stepDefinition;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

public class MailSender
{
    final String senderEmailID = "typesendermailid@gmail.com";
    final String senderPassword = "typesenderpassword";
    final String emailSMTPserver = "internalrelay.open.ru";
    final String emailServerPort = "25";
    String receiverEmailID = null;
    static String emailSubject = "Test Mail";
    static String emailBody = ":)";
    public MailSender(String receiverEmailID, String emailSubject, String emailBody)
    {
        this.receiverEmailID=receiverEmailID;
        this.emailSubject=emailSubject;
        this.emailBody=emailBody;
        Properties props = new Properties();
        props.put("mail.smtp.user",senderEmailID);
        props.put("mail.smtp.host", emailSMTPserver);
        props.put("mail.smtp.port", emailServerPort);
//        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.socketFactory.port", emailServerPort);
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        SecurityManager security = System.getSecurityManager();

        System.out.println("Security Manager " + security);

        try
        {
            Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);
            MimeMessage msg = new MimeMessage(session);
            msg.setText(emailBody);
            msg.setSubject(emailSubject);
            msg.setFrom(new InternetAddress(senderEmailID));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(receiverEmailID));
            Transport.send(msg);
            System.out.println("Message send Successfully:)");
        }
        catch (Exception mex)
        {
            mex.printStackTrace();
        }
    }
    public class SMTPAuthenticator extends javax.mail.Authenticator
    {
        public PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication(senderEmailID, senderPassword);
        }
    }
    public static void main(String[] args)
    {
        MailSender mailSender=new
                MailSender("ragimov@OPEN.RU",emailSubject,emailBody);
    }
}