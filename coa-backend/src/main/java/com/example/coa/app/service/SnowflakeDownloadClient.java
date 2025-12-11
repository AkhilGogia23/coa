package com.example.coa.app.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class SnowflakeDownloadClient {

    private final RestTemplate restTemplate;

    public SnowflakeDownloadClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public byte[] downloadPresigned(String url) {
        try {
            URI uri = URI.create(url);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/pdf, application/octet-stream");
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<byte[]> response = restTemplate.exchange(uri, org.springframework.http.HttpMethod.GET, entity, byte[].class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }
            throw new RuntimeException("Presigned download failed HTTP " + response.getStatusCode());
        } catch (Exception e) {
            throw new RuntimeException("Error downloading presigned URL: " + e.getMessage(), e);
        }
    }
}
