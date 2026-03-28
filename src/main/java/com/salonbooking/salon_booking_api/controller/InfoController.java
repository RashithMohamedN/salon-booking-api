package com.salonbooking.salon_booking_api.controller;

import com.salonbooking.salon_booking_api.config.BusinessConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/info")
public class InfoController {

    @Autowired
    private BusinessConfig businessConfig;

    // GET /api/info — returns business info to React
    @GetMapping
    public ResponseEntity<?> getBusinessInfo() {
        return ResponseEntity.ok(Map.of(
                "name",      businessConfig.getBusinessName(),
                "phone",     businessConfig.getBusinessPhone(),
                "whatsapp",  businessConfig.getBusinessWhatsapp(),
                "address",   businessConfig.getBusinessAddress()
        ));
    }
}