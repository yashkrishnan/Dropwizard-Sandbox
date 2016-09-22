package com.test.sandbox.resources.resourcehelpers;

import com.test.sandbox.models.genericmodels.SessionVariables;
import com.test.sandbox.views.IndexView;
import com.test.sandbox.views.LoginView;
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
