package com.example.coa.app.controller;

import com.example.coa.app.dto.LotResponse;
import com.example.coa.app.service.LotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lots")
@CrossOrigin(origins = "*") // allow Angular dev server
public class LotController {

    private final LotService lotService;

    public LotController(LotService lotService) {
        this.lotService = lotService;
    }

    @GetMapping("/{lotNumber}")
    public ResponseEntity<LotResponse> getLot(@PathVariable String lotNumber) {
        LotResponse response = lotService.getLot(lotNumber);

        // Later you can return 404 if not found. For now always return mock.
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok().body("{\"status\":\"ok\"}");
    }
}