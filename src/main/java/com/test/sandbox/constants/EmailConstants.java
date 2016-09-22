package com.test.sandbox.constants;

import com.test.sandbox.configurations.SandboxConfiguration;

/**
 * Collected constants of Email based functionality. All members of this class are immutable.
 */
public final class EmailConstants {
    public static final String CONTENT_TYPE = "Content-type";
    public static final String CHARSET = "text/HTML; charset=UTF-8";
    public static final String FORMAT = "format";
    public static final String FLOWED = "flowed";
    public static final String EIGHT_BIT = "8bit";
    public static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";

    public static final String NO_REPLY_TAG = SandboxConfiguration.getNoReplyTag();//"NoReply-AdPub"
    public static final String NO_REPLY_SITE = SandboxConfiguration.getNoReplySite(); //"no_reply@adpub.com"
    public static final String TEXT_HTML = "text/html";
    public static final String RESET_PASSWORD = "Reset password";

    public static final String TRANSPORT_HOST = SandboxConfiguration.getTransportHost();//"smtp.gmail.com"
    public static final String TRANSPORT_PROTOCOL = SandboxConfiguration.getTransportProtocol();//"smtp";
    public static final String SMTP_MAIL_RELAY = SandboxConfiguration.getSmtpMailRelay();//"smtp-relay.gmail.com"
    public static final String MAIL_SMTP_HOST = SandboxConfiguration.getMailSMTPHost();//"mail.smtp.host"
    public static final String SSL_FACTORY = SandboxConfiguration.getSslFactory();//"mail.smtp.socketFactory.class"
    public static final String SSL_FACTORY_VALUE = SandboxConfiguration.getSslFactoryValue();//"javax.net.ssl.SSLSocketFactory";
    public static final String MAIL_SMTP_AUTH = SandboxConfiguration.getMailSMTPAuth();//"mail.smtp.auth"
    public static final String MAIL_SMTP_AUTH_VALUE = SandboxConfiguration.getMailSMTPAuthValue();//"true"
    public static final String SSL_PORT = SandboxConfiguration.getSslPort();//"mail.smtp.socketFactory.port"
    public static final String MAIL_SMTP_PORT = SandboxConfiguration.getMailSMTPPort();//"mail.smtp.port"
    public static final String SMTP_PORT_NUMBER = SandboxConfiguration.getSmtpPortNumber();//"465"

    public static final String EMAIL_FROM = SandboxConfiguration.getSupportEmail(); //requires valid gmail id
    public static final String EMAIL_PASSWORD = SandboxConfiguration.getSupportEmailPassword(); // correct PASSWORD for gmail id
}
