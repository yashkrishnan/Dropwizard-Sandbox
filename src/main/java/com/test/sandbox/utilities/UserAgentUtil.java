package com.test.sandbox.utilities;

import com.test.sandbox.constants.SecurityConstants;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;

/**
 * The DetectSmartPhone class encapsulates information about a browser's connection to your web site. You can use it
 * to find out whether the browser asking for your site's content is probably running on a mobile device.The methods
 * were written so you can be as granular as you want.For example, enquiring whether it's as specific as an iPod Touch
 * or as general as a smartphone class device. The object's methods return true, or false.
 */
public class UserAgentUtil {
    public static String getUserAgentType(HttpServletRequest httpServletRequest) {
        String userAgentType;
        String userAgentHeader = httpServletRequest.getHeader(SecurityConstants.USER_AGENT);

        if (userAgentHeader.contains(SecurityConstants.USER_AGENT_ANDROID_APP)) {
            //request from Android
            userAgentType = SecurityConstants.APP_ANDROID;
        } else if (userAgentHeader.contains(SecurityConstants.USER_AGENT_IOS_APP)) {
            //request from iOS
            userAgentType = SecurityConstants.APP_IOS;
        } else {
            //this is probably a desktop
            userAgentType = SecurityConstants.WEB;
        }
        return userAgentType;
    }

    public static String getUserAgentType(ContainerRequestContext containerRequestContext) {
        String userAgentType;
        String userAgentHeader = containerRequestContext.getHeaderString(SecurityConstants.USER_AGENT);
        if (userAgentHeader.contains(SecurityConstants.USER_AGENT_ANDROID_APP)) {
            //request from Android
            userAgentType = SecurityConstants.APP_ANDROID;
        } else if (userAgentHeader.contains(SecurityConstants.USER_AGENT_IOS_APP) || userAgentHeader.contains(SecurityConstants.I_PHONE) || userAgentHeader.contains(SecurityConstants.I_PAD) || userAgentHeader.contains(SecurityConstants.OS_VERSION)) {
            //request from iOS
            userAgentType = SecurityConstants.APP_IOS;
        } else {
            //this is probably a desktop
            userAgentType = SecurityConstants.WEB;
        }
        return userAgentType;
    }
}