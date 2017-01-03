package com.example.sandbox.email;

import com.example.sandbox.constants.EmailConstants;
import com.example.sandbox.constants.StatusConstants;
import com.example.sandbox.constants.TextConstants;
import com.example.sandbox.models.genericmodels.responsemodels.BaseResponse;
import com.example.sandbox.utilities.WebUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.Callable;

/**
 * Class for email based communications
 */
public class Email implements Callable {
    private static final Logger logger = LoggerFactory.getLogger(Email.class);
    private static Session session = null;
    private static Transport transport = null;

    private synchronized static void createEmailTransport() {
        if (session == null) {
            logger.info("Email session is null, creating new email session");
            createEmailSession();
        }
        if (transport == null) {
            logger.info("Email transport is null, creating new email transport");
            try {
                logger.info("Getting email transport from email session");
                transport = session.getTransport(EmailConstants.TRANSPORT_PROTOCOL);
                // Enter your correct gmail UserID and Password
                logger.info("Connecting new email transport");
                transport.connect(EmailConstants.TRANSPORT_HOST, EmailConstants.EMAIL_FROM, EmailConstants.EMAIL_PASSWORD);
                logger.info("Connected new email transport");
            } catch (MessagingException e) {
                logger.error("Error creating new email transport");
                logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
                logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
            }
        } else if (!transport.isConnected()) {
            logger.info("Email transport is not connected, creating new email transport");
            transport = null;
            createEmailTransport();
        }
    }

    /**
     * Outgoing Mail (SMTP) Server requires
     * TLS or SSL: smtp.gmail.com
     * Use Authentication: Yes
     * Port for TLS/STARTTLS: 587
     */
    private synchronized static void createEmailSession() {
        logger.info("Adding email properties");
        Properties properties = new Properties();
        properties.put(EmailConstants.MAIL_SMTP_HOST, EmailConstants.TRANSPORT_HOST); //SMTP Host
        properties.put(EmailConstants.SSL_PORT, EmailConstants.SMTP_PORT_NUMBER); //SSL Port
        properties.put(EmailConstants.SSL_FACTORY, EmailConstants.SSL_FACTORY_VALUE); //SSL Factory Class
        properties.put(EmailConstants.MAIL_SMTP_AUTH, EmailConstants.MAIL_SMTP_AUTH_VALUE); //Enabling SMTP Authentication
        properties.put(EmailConstants.MAIL_SMTP_PORT, EmailConstants.SMTP_PORT_NUMBER); //SMTP Port
        //properties.put("mail.smtp.starttls.enable", "true");

        logger.info("Authenticating email session");
        Authenticator authenticator = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EmailConstants.EMAIL_FROM, EmailConstants.EMAIL_PASSWORD);
            }
        };

        if (session == null) {
            logger.info("Email session is null, getting default email session");
            session = Session.getDefaultInstance(properties, authenticator);
        }
        logger.info("Created new email session");
    }

    private synchronized MimeMessage buildMimeMessageWithoutAttachment(String toEmail, String body, String contentType, String subject) {
        if (session == null) {
            createEmailSession();
        }
        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            //set message headers
            mimeMessage.addHeader(EmailConstants.CONTENT_TYPE, EmailConstants.CHARSET);
            mimeMessage.addHeader(EmailConstants.FORMAT, EmailConstants.FLOWED);
            mimeMessage.addHeader(EmailConstants.CONTENT_TRANSFER_ENCODING, EmailConstants.EIGHT_BIT);
            mimeMessage.setFrom(new InternetAddress(EmailConstants.NO_REPLY_SITE, EmailConstants.NO_REPLY_TAG));
            mimeMessage.setReplyTo(InternetAddress.parse(EmailConstants.NO_REPLY_SITE, false));
            mimeMessage.setSubject(subject, TextConstants.ENCODING_UTF_8);
            mimeMessage.setContent(body, contentType);
            mimeMessage.setSentDate(new Date());
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
            logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
        }
        return mimeMessage;
    }

    private synchronized MimeMessage buildMimeMessageWithAttachment(String toEmail, String body, String contentType, String subject, String attachment, String attachmentName) {
        if (session == null) {
            createEmailSession();
        }
        // Create a default MimeMessage object
        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            // Set message headers
            // Set From: field of the message
            mimeMessage.setFrom(new InternetAddress(EmailConstants.NO_REPLY_SITE, EmailConstants.NO_REPLY_TAG));
            // Set To: field of the message
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            // Set Subject: field of the message
            mimeMessage.setSubject(subject);
            // Set Reply To: field of the message
            mimeMessage.setReplyTo(InternetAddress.parse(EmailConstants.NO_REPLY_SITE, false));
            // Set Send Date: field of the message
            mimeMessage.setSentDate(new Date());

            // Create message body part
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            // Set the message body
            mimeBodyPart.setContent(body, contentType);
            // Set the Content-Type header
            mimeBodyPart.addHeader(EmailConstants.CONTENT_TYPE, EmailConstants.CHARSET);
            // Set the format header
            mimeBodyPart.addHeader(EmailConstants.FORMAT, EmailConstants.FLOWED);
            // Set the Content-Transfer-Encoding header
            mimeBodyPart.addHeader(EmailConstants.CONTENT_TRANSFER_ENCODING, EmailConstants.EIGHT_BIT);

            // mimeBodyPart.setDisposition(Part.INLINE);

            BodyPart bodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(attachment);
            bodyPart.setDataHandler(new DataHandler(source));
            // Set name of attachment
            bodyPart.setFileName(attachmentName);

            // Create a multipart message
            Multipart multipart = new MimeMultipart();
            // Add Message to Multipart
            multipart.addBodyPart(mimeBodyPart);
            // Add Attachment to Multipart
            multipart.addBodyPart(bodyPart);

            // Add attachment to message
            mimeMessage.setContent(multipart);

        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
            logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
        }
        return mimeMessage;
    }

    /**
     * Utility method to send simple HTML email
     */
    synchronized BaseResponse sendEmail(String toEmail, String body, String contentType, String subject, String attachment, String attachmentName) {
        BaseResponse baseResponse = new BaseResponse();
        MimeMessage mimeMessage;
        if (attachment == null) {
            logger.info("Creating email content without attachment");
            mimeMessage = buildMimeMessageWithoutAttachment(toEmail, body, contentType, subject);
        } else {
            logger.info("Creating email content with attachment");
            mimeMessage = buildMimeMessageWithAttachment(toEmail, body, contentType, subject, attachment, attachmentName);
        }
        try {
            if (transport == null) {
                logger.info("Email transport is null, creating new email transport");
                createEmailTransport();
                logger.info("Sending email");
                transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            } else if (transport.isConnected()) {
                logger.info("Email transport is already connected");
                logger.info("Sending email");
                transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            } else {
                logger.info("Email transport is not connected, creating new email transport");
                createEmailTransport();
                logger.info("Sending email");
                transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            }
            logger.info("Email sent successfully");
            baseResponse.setStatus(StatusConstants.SUCCESS);
            baseResponse.setMessage(StatusConstants.SEND_EMAIL_SUCCESS);
        } catch (MessagingException e) {
            logger.error("Error sending email");
            logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
            logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
            baseResponse.setStatus(StatusConstants.FAILURE);
            baseResponse.setMessage(StatusConstants.SEND_EMAIL_FAILURE);
        }
        return baseResponse;
    }

    synchronized BaseResponse sendMailGunEmail(String toEmail, String body, String contentType, String subject) {
        String requestURL = "https://api.mailgun.net/v3/mail.upzel.io/messages";
        String authorization = DatatypeConverter.printBase64Binary("api:key-a12b3c4d567890e1f2gh3ij45kl678m9".getBytes());
        HashMap<String, String> headers = new HashMap<>();
        headers.put("authorization", "Basic " + authorization);

        HashMap<String, Object> fields = new HashMap<>();
        fields.put("from", "no_reply@example.me");
        fields.put("to", toEmail);
        fields.put("subject", subject);
        fields.put("html", body);

        String response = new WebUtilities().getPostResponse(requestURL, null, headers, null, fields);

        BaseResponse baseResponse = new BaseResponse();
        if (!response.isEmpty()) {
            //TODO Check content of response
            logger.info("Email sent successfully");
            baseResponse.setStatus(StatusConstants.SUCCESS);
            baseResponse.setMessage(StatusConstants.SEND_EMAIL_SUCCESS);
        } else {
            logger.error("Error sending email");
            baseResponse.setStatus(StatusConstants.FAILURE);
            baseResponse.setMessage(StatusConstants.SEND_EMAIL_FAILURE);
        }
        return baseResponse;
    }

    @Override
    public synchronized Object call() throws Exception {
        return null;
    }
}