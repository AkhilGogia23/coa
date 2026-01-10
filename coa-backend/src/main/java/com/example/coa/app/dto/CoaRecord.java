package com.example.coa.app.dto;

import java.time.LocalDateTime;

public class CoaRecord {

    private String fileName;
    private String fullText;
    private String lotNumber;
    private LocalDateTime lastModified;
    private String partNumber;

    public String getPartNumber() { return partNumber; }
    public void setPartNumber(String partNumber) { this.partNumber = partNumber; }


    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFullText() { return fullText; }
    public void setFullText(String fullText) { this.fullText = fullText; }

    public String getLotNumber() { return lotNumber; }
    public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }

    public LocalDateTime getLastModified() { return lastModified; }
    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }
}