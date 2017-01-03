package com.example.sandbox.utilities;

import com.google.gson.Gson;
import com.example.sandbox.constants.EmailConstants;
import com.example.sandbox.constants.MarkupConstants;
import com.example.sandbox.constants.StatusConstants;
import com.example.sandbox.constants.TextConstants;
import com.example.sandbox.email.EmailOperator;
import com.example.sandbox.models.genericmodels.responsemodels.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

/**
 * Util class for email based communications
 */
public class EmailUtil {

    private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);

    public static BaseResponse forgotPasswordEmail(final String toEmail, String passwordResetURL) {
        final String body = MarkupConstants.RESET_PASSWORD_EMAIL_PART_1 + passwordResetURL + MarkupConstants.RESET_PASSWORD_EMAIL_PART_2;
        BaseResponse baseResponse = new BaseResponse();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    new EmailOperator().sendEmail(toEmail, body, EmailConstants.TEXT_HTML, EmailConstants.RESET_PASSWORD, null, null);
                } catch (UnsupportedEncodingException | MessagingException e) {
                    logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
                    logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
                }
            }
        };
        ThreadUtil.executeCachedThread(runnable);
        baseResponse.setStatus(StatusConstants.SUCCESS);
        baseResponse.setMessage(StatusConstants.SEND_EMAIL_SUCCESS);
        Gson gson = new Gson();
        String baseResponseModelJSON = gson.toJson(baseResponse);
        return gson.fromJson(baseResponseModelJSON, BaseResponse.class);
    }
}
