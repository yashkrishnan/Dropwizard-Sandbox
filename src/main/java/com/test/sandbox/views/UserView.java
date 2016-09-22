package com.test.sandbox.views;

import io.dropwizard.views.View;

import java.nio.charset.Charset;

public class UserView extends View {
    public UserView(String templateName) {
        super(templateName);
    }

    protected UserView(String templateName, Charset charset) {
        super(templateName, charset);
    }
}
