package com.example.coa.app.service;
import com.example.coa.app.dto.CoaResultItem;
import com.example.coa.app.dto.LotResults;
import com.example.coa.app.dto.LotResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LotService {

    public LotResponse getLot(String lotNumber) {
        // TODO: later, call MasterControl + Snowflake

        // Mock data for now:
        CoaResultItem ingredient = new CoaResultItem(
                "Vitamin C",
                "ING-001",
                "https://example.com/vitc.pdf",
                true
        );

        CoaResultItem blend = new CoaResultItem(
                "Premix Blend A",
                "BLEND-045",
                "https://example.com/blend.pdf",
                true
        );

        CoaResultItem finished = new CoaResultItem(
                "Example Product",
                lotNumber,
                "https://example.com/finished.pdf",
                true
        );

        LotResults results = new LotResults(
                List.of(ingredient),
                List.of(blend),
                List.of(finished)
        );

        return new LotResponse(
                lotNumber,
                "Example Product",
                "SKU-123",
                "Released",
                "2025-01-15",
                "2027-01-15",
                results
        );
    }
}