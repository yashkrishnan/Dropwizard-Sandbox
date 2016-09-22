package com.test.sandbox.apis.googlegcm;

import com.test.sandbox.apis.googlegcm.models.GCMPushNotification;
import com.test.sandbox.apis.googlegcm.models.Notification;
import com.test.sandbox.apis.googlegcm.models.PushNotificationMessage;
import com.test.sandbox.constants.ApplicationConstants;
import com.test.sandbox.constants.TextConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * API class for performing GCM operations
 */
public class GoogleGCMUtil {
    private static HttpURLConnection httpURLConnection = null;
    //create an object of GoogleGCMUtil
    private static volatile GoogleGCMUtil googleGCMUtil;
    private final Logger logger = LoggerFactory.getLogger(GoogleGCMUtil.class);

    //make the constructor private so that this class cannot be instantiated
    private GoogleGCMUtil() {

    }

    //Get the only object available
    public synchronized static GoogleGCMUtil getInstance() {
        synchronized (GoogleGCMUtil.class) {
            // Double check
            if (googleGCMUtil == null) {
                googleGCMUtil = new GoogleGCMUtil();
            }
        }
        return googleGCMUtil;
    }

    public synchronized GCMPushNotification createGCMNotification(List<String> gcmTokens, PushNotificationMessage pushNotificationMessage) {
        GCMPushNotification gcmPushNotification = new GCMPushNotification();
        gcmPushNotification.setRegistration_ids(gcmTokens);
        gcmPushNotification.setData(pushNotificationMessage);

        gcmPushNotification.setContent_available(true);
        gcmPushNotification.setDelay_while_idle(true);
        gcmPushNotification.setPriority("high");
        //gcmPushNotification.setCollapse_key(null);
        //gcmPushNotification.setTime_to_live(null);

        Notification notification = new Notification();
        notification.setPriority("high");
        notification.setContent_available(true);
        notification.setTitle(pushNotificationMessage.getNotificationTitle());
        notification.setBody(pushNotificationMessage.getNotificationMessage());
        notification.setSound("default");
        notification.setBadge((int) pushNotificationMessage.getNewNotifications());
        gcmPushNotification.setNotification(notification);
        //notification.setIcon(null);
        return gcmPushNotification;
    }

    public synchronized void pushGCMNotifications(List<GCMPushNotification> gcmPushNotifications) {
        try {
            for (GCMPushNotification gcmPushNotification : gcmPushNotifications) {
                // 1. URL
                URL url = new URL("https://android.googleapis.com/gcm/send");

                // 2. Open connection
                httpURLConnection = (HttpURLConnection) url.openConnection();

                // 3. Specify POST method
                httpURLConnection.setRequestMethod("POST");

                // 4. Set the headers
                httpURLConnection.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON);
                String gcmAPIKey = ApplicationConstants.GCM_API_KEY;
                httpURLConnection.setRequestProperty("Authorization", "key=" + gcmAPIKey);
                httpURLConnection.setDoOutput(true);
                // 5. Add JSON data into POST request body
                // 5.1 Use Jackson object mapper to convert Content object into JSON
                ObjectMapper objectMapper = new ObjectMapper();

                // 5.2 Get connection output stream
                DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());

                // 5.3 Copy GCMContent "JSON" into
                objectMapper.writeValue(dataOutputStream, gcmPushNotification);

                // 5.4 Send the request
                dataOutputStream.flush();

                // 5.5 close
                dataOutputStream.close();

                // 6. Get the response
                int responseCode = httpURLConnection.getResponseCode();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String inputLine;
                StringBuilder responseStringBuilder = new StringBuilder();

                while ((inputLine = bufferedReader.readLine()) != null) {
                    responseStringBuilder.append(inputLine);
                }
                bufferedReader.close();

                // 7. Print result
                logger.info("\nGCM Response : " + responseStringBuilder.toString());
            }
            httpURLConnection.disconnect();
        } catch (IOException e) {
            logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
            logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
        }
    }
}
