package com.ecom.Utility;

import jakarta.servlet.http.HttpServletRequest;

public class ResetPasswordUrlGenerator {

    public static String generateResetPasswordUrl() {
        String url="http://localhost:8080/reset_password?token=";
        return url;
    }
}
