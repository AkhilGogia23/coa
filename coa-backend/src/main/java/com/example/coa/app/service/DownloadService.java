package com.example.coa.app.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class DownloadService {

    private final MasterControlService masterControlService;
    private final SnowflakeDownloadClient snowflakeDownloadClient;
    private final String masterControlBase;

    public DownloadService(MasterControlService masterControlService,
                           SnowflakeDownloadClient snowflakeDownloadClient,
                           org.springframework.core.env.Environment env) {
        this.masterControlService = masterControlService;
        this.snowflakeDownloadClient = snowflakeDownloadClient;
        this.masterControlBase = env.getProperty("mastercontrol.base-url", "");
    }


    public byte[] downloadByUrl(String url) {
        String lower = url == null ? "" : url.toLowerCase();


        if (!masterControlBase.isBlank() && lower.contains(masterControlBase.toLowerCase())) {
            return masterControlService.downloadPdf(url);
        }


        if (lower.contains("mastercontrol") || lower.contains("mx.us-west-2.svc.mastercontrol.com")) {
            return masterControlService.downloadPdf(url);
        }


        if (lower.contains("amazonaws.com") ||
                lower.contains("s3.") ||
                lower.contains("x-amz-signature") ||
                lower.contains("x-amz-credential") ||
                lower.contains("cloudfront.net")) {
            return snowflakeDownloadClient.downloadPresigned(url);
        }


        if (lower.contains("snowflakecomputing.com") || lower.contains(".snowflake.")) {
            return snowflakeDownloadClient.downloadPresigned(url);
        }


        try {
            return snowflakeDownloadClient.downloadPresigned(url);
        } catch (Exception e) {

            return masterControlService.downloadPdf(url);
        }
    }
}
