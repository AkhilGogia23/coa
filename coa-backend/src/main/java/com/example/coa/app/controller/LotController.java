package com.example.coa.app.controller;

import com.example.coa.app.dto.LotFullResponse;
import com.example.coa.app.dto.LotResponse;
import com.example.coa.app.service.DownloadService;
import com.example.coa.app.service.LotService;
import com.example.coa.app.service.MasterControlService;
import com.example.coa.app.service.SnowflakeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lots")
@CrossOrigin(origins = "*")
public class LotController {

    private final LotService lotService;
    private final SnowflakeService snowflakeService;
    private final MasterControlService masterControlService;
    private final DownloadService downloadService;
    public LotController(
            LotService lotService,
            SnowflakeService snowflakeService,
            MasterControlService masterControlService,
            DownloadService downloadService
    ) {
        this.lotService = lotService;
        this.snowflakeService = snowflakeService;
        this.masterControlService = masterControlService;
        this.downloadService=downloadService;
    }


    @GetMapping("/{lotNumber}")
    public ResponseEntity<LotResponse> getLotByNumber(@PathVariable String lotNumber) {
        LotResponse response = lotService.getLotFromSnowflake(lotNumber);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{lotNumber}/full")
    public ResponseEntity<LotFullResponse> getLotFull(
            @PathVariable String lotNumber,
            @RequestParam Integer productionRecordId
    ) {
        LotFullResponse response = lotService.getFullLot(lotNumber, productionRecordId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/db-health")
    public ResponseEntity<String> dbHealth() {
        String warehouse = snowflakeService.testConnection();
        return ResponseEntity.ok("Connected to warehouse: " + warehouse);
    }


    @GetMapping("/mastercontrol/data-captures")
    public ResponseEntity<Object> masterControlDataCaptures(
            @RequestParam Integer productionRecordId
    ) {
        Object body = masterControlService.fetchDataCapturesForProductionRecord(productionRecordId);
        return ResponseEntity.ok(body);
    }
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadPdf(@RequestParam String url) {
        byte[] file = downloadService.downloadByUrl(url);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"coa.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(file.length)
                .body(file);
    }


}
