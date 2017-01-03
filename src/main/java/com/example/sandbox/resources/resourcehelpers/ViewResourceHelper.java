package com.example.sandbox.resources.resourcehelpers;

import com.example.sandbox.models.genericmodels.SessionVariables;
import com.example.sandbox.views.IndexView;
import com.example.sandbox.views.LoginView;
import io.dropwizard.views.View;

public class ViewResourceHelper {
    public View getIndex(SessionVariables sessionVariables) {
        boolean validAuthToken = sessionVariables.isValidAuthToken();
        if (validAuthToken) {
            return new IndexView("index.ftl");
        } else {
            return new LoginView("login.ftl");
        }
    }
}
