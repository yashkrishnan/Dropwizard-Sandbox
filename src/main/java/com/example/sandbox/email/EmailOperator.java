package com.example.sandbox.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

/**
 * Operator to handle email communications
 */
public class EmailOperator {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    public synchronized void sendEmail(String toEmail, String body, String contentType, String subject, String attachment, String attachmentName) throws UnsupportedEncodingException, MessagingException {
        EmailPoolManager emailPoolManager = new EmailPoolManager();
        Email email = emailPoolManager.borrowObjectFromPool();
        logger.info("Sending Email");
        email.sendEmail(toEmail, body, contentType, subject, attachment, attachmentName);
        emailPoolManager.returnObjectToPool(email);
    }


    public synchronized void sendMailGunEmail(String toEmail, String body, String contentType, String subject) throws UnsupportedEncodingException, MessagingException {
        EmailPoolManager emailPoolManager = new EmailPoolManager();
        Email email = emailPoolManager.borrowObjectFromPool();
        logger.info("Sending Email");
        email.sendMailGunEmail(toEmail, body, contentType, subject);
        emailPoolManager.returnObjectToPool(email);
    }
}
