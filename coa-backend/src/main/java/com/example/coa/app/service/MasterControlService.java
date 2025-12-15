package com.example.coa.app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
public class MasterControlService {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String apiKey;


    public MasterControlService(
            RestTemplate restTemplate,
            @Value("${master-control.base-url}") String baseUrl,
            @Value("${master-control.api-key}") String apiKey   // 👈 use ONE consistent property name
    ) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }


    public Object fetchDataCaptures(
            Integer productionRecordId,
            Long startEpoch,
            Long endEpoch,
            int currentPage,
            int itemsPerPage,
            boolean onlyPrimary,
            boolean onlyCurrent
    ) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(baseUrl + "/v1/manufacturing/execution/production-record-data-captures")
                .queryParam("currentPage", currentPage)
                .queryParam("itemsPerPage", itemsPerPage);

        if (startEpoch != null && endEpoch != null) {
            builder.queryParam("start", startEpoch)
                    .queryParam("end", endEpoch);
        }

        if (productionRecordId != null) {
            builder.queryParam("productionRecordId", productionRecordId);
        }

        if (onlyPrimary) {
            builder.queryParam("onlyPrimary", true);
        }

        if (onlyCurrent) {
            builder.queryParam("onlyCurrent", true);
        }

        URI uri = builder.build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(null, headers);

        try {
            ResponseEntity<Object> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    Object.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.err.println("MasterControl API error " + e.getStatusCode()
                    + " on GET " + uri + " --> " + e.getResponseBodyAsString());
            throw e;
        }
    }


    public Object fetchDataCapturesForProductionRecord(Integer productionRecordId) {
        return fetchDataCaptures(
                productionRecordId,
                null,
                null,
                0,
                100,
                true,
                true
        );
    }


    public byte[] downloadPdf(String url) {
        try {
            URI uri = URI.create(url);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.setAccept(List.of(org.springframework.http.MediaType.APPLICATION_PDF, org.springframework.http.MediaType.APPLICATION_OCTET_STREAM));
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<byte[]> response = restTemplate.exchange(uri, org.springframework.http.HttpMethod.GET, entity, byte[].class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new RuntimeException("MasterControl download failed HTTP " + response.getStatusCode());
            }
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            System.err.println("MasterControl download error: " + e.getStatusCode() + " -> " + e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error downloading MasterControl PDF: " + e.getMessage(), e);
        }
    }

}
