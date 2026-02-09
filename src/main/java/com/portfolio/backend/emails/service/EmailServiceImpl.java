package com.portfolio.backend.emails.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Value("${brevo.api-key}")
    private String apiKey;

    @Value("${brevo.sender-email}")
    private String senderEmail;

    @Value("${brevo.sender-name}")
    private String senderName;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    @Async
    public void sendSimpleMessage(String to, String subject, String text) {
        log.info("=== BREVO API: Iniciando envio de email ===");
        log.info("   To: {}", to);
        log.info("   From: {} <{}>", senderName, senderEmail);
        log.info("   Subject: {}", subject);

        try {
            // Escapar caracteres especiales para JSON
            String escapedText = text.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");

            String escapedSubject = subject.replace("\\", "\\\\")
                    .replace("\"", "\\\"");

            String jsonBody = String.format(
                    "{\"sender\":{\"name\":\"%s\",\"email\":\"%s\"},\"to\":[{\"email\":\"%s\"}],\"subject\":\"%s\",\"textContent\":\"%s\"}",
                    senderName, senderEmail, to, escapedSubject, escapedText);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
                    .header("accept", "application/json")
                    .header("api-key", apiKey)
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201 || response.statusCode() == 200) {
                log.info("=== EMAIL ENVIADO EXITOSAMENTE via Brevo API ===");
                log.info("   Response: {}", response.body());
            } else {
                log.error("=== ERROR BREVO API: Status {} ===", response.statusCode());
                log.error("   Response body: {}", response.body());
            }
        } catch (Exception e) {
            log.error("=== ERROR ENVIANDO EMAIL via Brevo API ===");
            log.error("   Destinatario: {}", to);
            log.error("   Error: {}", e.getMessage());
            log.error("   Stack trace:", e);
        }
    }
}
