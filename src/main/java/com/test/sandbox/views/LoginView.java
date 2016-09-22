package com.test.sandbox.views;

import io.dropwizard.views.View;

import java.nio.charset.Charset;

public class LoginView extends View {

    public LoginView(String templateName) {
        super(templateName);
    }

    public LoginView(String templateName, Charset charset) {
        super(templateName, charset);
    }
}
