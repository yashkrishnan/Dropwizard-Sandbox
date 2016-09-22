package com.test.sandbox.views;

import com.test.sandbox.models.User;
import io.dropwizard.views.View;

import java.nio.charset.Charset;

public class IndexView extends View {

    private User user;

    public IndexView(String templateName) {
        super(templateName);
    }

    public IndexView(String templateName, Charset charset) {
        super(templateName, charset);
    }
}
