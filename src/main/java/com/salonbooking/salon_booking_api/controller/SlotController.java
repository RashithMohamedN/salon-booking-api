package com.salonbooking.salon_booking_api.controller;

import com.salonbooking.salon_booking_api.model.TimeSlot;
import com.salonbooking.salon_booking_api.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/slots")
public class SlotController {

    @Autowired
    private TimeSlotRepository slotRepository;

    // GET /api/slots/available?date=2024-12-25
    // Returns only slots that still have capacity
    @GetMapping("/available")
    public ResponseEntity<List<TimeSlot>> getAvailableSlots(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<TimeSlot> slots = slotRepository.findAvailableSlotsByDate(date);
        return ResponseEntity.ok(slots);
    }

    // GET /api/slots?date=2024-12-25 — all slots for a date (admin use)
    @GetMapping
    public ResponseEntity<List<TimeSlot>> getAllSlotsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<TimeSlot> slots = slotRepository
                .findBySlotDateAndIsActiveTrueOrderBySlotTime(date);
        return ResponseEntity.ok(slots);
    }

    // DELETE /api/slots/{id} — admin deactivates a slot
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSlot(@PathVariable Integer id) {
        return slotRepository.findById(id).map(slot -> {
            slot.setIsActive(false);
            slotRepository.save(slot);
            return ResponseEntity.ok("Slot deactivated");
        }).orElse(ResponseEntity.notFound().build());
    }


    // POST /api/slots/generate — admin generates slots for a date
    @GetMapping("/generate")
    public ResponseEntity<String> generateSlots(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        // Standard time slots: 9am to 6pm, every 1 hour
        String[] times = {
                "09:00", "10:00", "11:00", "12:00",
                "13:00", "14:00", "15:00", "16:00", "17:00"
        };

        for (String time : times) {
            TimeSlot slot = new TimeSlot();
            slot.setSlotDate(date);
            slot.setSlotTime(LocalTime.parse(time));
            slot.setMaxBookings(1);
            slot.setIsActive(true);
            slotRepository.save(slot);
        }

        return ResponseEntity.ok("Slots generated for " + date);
    }
}
