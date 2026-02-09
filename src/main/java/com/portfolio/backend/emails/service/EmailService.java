package com.portfolio.backend.emails.service;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
}
