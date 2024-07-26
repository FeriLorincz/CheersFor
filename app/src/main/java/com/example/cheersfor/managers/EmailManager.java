package com.example.cheersfor.managers;

import android.util.Log;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailManager {

    private static final String EMAIL_USERNAME = "ferilorincz12@gmail.com";
    private static final String EMAIL_PASSWORD = "Bh82AMD+";

    public void sendEmailNotification(String email, String message) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
                    }
                });

        try {
            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(EMAIL_USERNAME));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            mimeMessage.setSubject("CheersFor App Notification");
            mimeMessage.setText(message);

            Transport.send(mimeMessage);

            Log.d("Email", "Email sent successfully to: " + email);
        } catch (MessagingException e) {
            Log.e("Email", "Failed to send email", e);
        }
    }

    public void inviteColleague(String email) {
        String message = "Te invităm să te alături aplicației Cheers For!";
        sendEmailNotification(email, message);
    }
}
