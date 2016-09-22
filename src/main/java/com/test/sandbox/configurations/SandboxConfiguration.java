package com.test.sandbox.configurations;

import io.dropwizard.Configuration;

public class SandboxConfiguration extends Configuration {
    // Directory configurations
    private static String webDirectory;
    private static String resourceDirectory;

    // Database configurations
    private static String mongoHost;
    private static String mongoPort;

    // Server configurations
    private static String serverAddress;
    private static String serverHost;
    private static String serverPort;

    // Encryption configurations
    private static String codecKeyValue;

    // User Agent configurations
    private static String androidUserAgent;
    private static String iOSUserAgent;

    // Email configurations
    private static String noReplyTag;
    private static String noReplySite;
    private static String transportHost;
    private static String transportProtocol;
    private static String mailSMTPHost;
    private static String sslFactory;
    private static String sslFactoryValue;
    private static String mailSMTPAuth;
    private static String mailSMTPAuthValue;
    private static String sslPort;
    private static String mailSMTPPort;
    private static String smtpPortNumber;
    private static String supportEmail;
    private static String supportEmailPassword;
    private static String smtpMailRelay;

    // Push notification configurations
    private static String gcmAPIKey;
    private static String apnsCertificatePath;
    private static String apnsPushCertificatePassword;

    public static String getWebDirectory() {
        return webDirectory;
    }

    public void setWebDirectory(String webDirectory) {
        SandboxConfiguration.webDirectory = webDirectory;
    }

    public static String getResourceDirectory() {
        return resourceDirectory;
    }

    public void setResourceDirectory(String resourceDirectory) {
        SandboxConfiguration.resourceDirectory = resourceDirectory;
    }

    public static String getMongoHost() {
        return mongoHost;
    }

    public void setMongoHost(String mongoHost) {
        SandboxConfiguration.mongoHost = mongoHost;
    }

    public static String getMongoPort() {
        return mongoPort;
    }

    public void setMongoPort(String mongoPort) {
        SandboxConfiguration.mongoPort = mongoPort;
    }

    public static String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        SandboxConfiguration.serverAddress = serverAddress;
    }

    public static String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        SandboxConfiguration.serverHost = serverHost;
    }

    public static String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        SandboxConfiguration.serverPort = serverPort;
    }

    public static String getCodecKeyValue() {
        return codecKeyValue;
    }

    public void setCodecKeyValue(String codecKeyValue) {
        SandboxConfiguration.codecKeyValue = codecKeyValue;
    }

    public static String getAndroidUserAgent() {
        return androidUserAgent;
    }

    public void setAndroidUserAgent(String androidUserAgent) {
        SandboxConfiguration.androidUserAgent = androidUserAgent;
    }

    public static String getiOSUserAgent() {
        return iOSUserAgent;
    }

    public void setiOSUserAgent(String iOSUserAgent) {
        SandboxConfiguration.iOSUserAgent = iOSUserAgent;
    }

    public static String getNoReplyTag() {
        return noReplyTag;
    }

    public void setNoReplyTag(String noReplyTag) {
        SandboxConfiguration.noReplyTag = noReplyTag;
    }

    public static String getNoReplySite() {
        return noReplySite;
    }

    public void setNoReplySite(String noReplySite) {
        SandboxConfiguration.noReplySite = noReplySite;
    }

    public static String getTransportHost() {
        return transportHost;
    }

    public void setTransportHost(String transportHost) {
        SandboxConfiguration.transportHost = transportHost;
    }

    public static String getTransportProtocol() {
        return transportProtocol;
    }

    public void setTransportProtocol(String transportProtocol) {
        SandboxConfiguration.transportProtocol = transportProtocol;
    }

    public static String getMailSMTPHost() {
        return mailSMTPHost;
    }

    public void setMailSMTPHost(String mailSMTPHost) {
        SandboxConfiguration.mailSMTPHost = mailSMTPHost;
    }

    public static String getSslFactory() {
        return sslFactory;
    }

    public void setSslFactory(String sslFactory) {
        SandboxConfiguration.sslFactory = sslFactory;
    }

    public static String getSslFactoryValue() {
        return sslFactoryValue;
    }

    public void setSslFactoryValue(String sslFactoryValue) {
        SandboxConfiguration.sslFactoryValue = sslFactoryValue;
    }

    public static String getMailSMTPAuth() {
        return mailSMTPAuth;
    }

    public void setMailSMTPAuth(String mailSMTPAuth) {
        SandboxConfiguration.mailSMTPAuth = mailSMTPAuth;
    }

    public static String getMailSMTPAuthValue() {
        return mailSMTPAuthValue;
    }

    public void setMailSMTPAuthValue(String mailSMTPAuthValue) {
        SandboxConfiguration.mailSMTPAuthValue = mailSMTPAuthValue;
    }

    public static String getSslPort() {
        return sslPort;
    }

    public void setSslPort(String sslPort) {
        SandboxConfiguration.sslPort = sslPort;
    }

    public static String getMailSMTPPort() {
        return mailSMTPPort;
    }

    public void setMailSMTPPort(String mailSMTPPort) {
        SandboxConfiguration.mailSMTPPort = mailSMTPPort;
    }

    public static String getSmtpPortNumber() {
        return smtpPortNumber;
    }

    public void setSmtpPortNumber(String smtpPortNumber) {
        SandboxConfiguration.smtpPortNumber = smtpPortNumber;
    }

    public static String getSupportEmail() {
        return supportEmail;
    }

    public void setSupportEmail(String supportEmail) {
        SandboxConfiguration.supportEmail = supportEmail;
    }

    public static String getSupportEmailPassword() {
        return supportEmailPassword;
    }

    public void setSupportEmailPassword(String supportEmailPassword) {
        SandboxConfiguration.supportEmailPassword = supportEmailPassword;
    }

    public static String getSmtpMailRelay() {
        return smtpMailRelay;
    }

    public void setSmtpMailRelay(String smtpMailRelay) {
        SandboxConfiguration.smtpMailRelay = smtpMailRelay;
    }

    public static String getGcmAPIKey() {
        return gcmAPIKey;
    }

    public void setGcmAPIKey(String gcmAPIKey) {
        SandboxConfiguration.gcmAPIKey = gcmAPIKey;
    }

    public static String getApnsCertificatePath() {
        return apnsCertificatePath;
    }

    public void setApnsCertificatePath(String apnsCertificatePath) {
        SandboxConfiguration.apnsCertificatePath = apnsCertificatePath;
    }

    public static String getApnsPushCertificatePassword() {
        return apnsPushCertificatePassword;
    }

    public void setApnsPushCertificatePassword(String apnsPushCertificatePassword) {
        SandboxConfiguration.apnsPushCertificatePassword = apnsPushCertificatePassword;
    }
}
