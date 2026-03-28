package com.salonbooking.salon_booking_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lombok.Data;

@Component
@Data
public class BusinessConfig {

    @Value("${app.business.name}")
    private String businessName;

    @Value("${app.business.phone}")
    private String businessPhone;

    @Value("${app.business.whatsapp}")
    private String businessWhatsapp;

    @Value("${app.business.address}")
    private String businessAddress;
}