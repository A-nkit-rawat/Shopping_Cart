package com.ecom.service;

public interface EmailService {
    public void sendEmail(String to, String subject, String body)throws Exception;
}
