package com.salonbooking.salon_booking_api.controller;

import com.salonbooking.salon_booking_api.model.Booking;
import com.salonbooking.salon_booking_api.model.Service;
import com.salonbooking.salon_booking_api.model.TimeSlot;
import com.salonbooking.salon_booking_api.repository.BookingRepository;
import com.salonbooking.salon_booking_api.repository.ServiceRepository;
import com.salonbooking.salon_booking_api.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired private BookingRepository bookingRepository;
    @Autowired private ServiceRepository serviceRepository;
    @Autowired private TimeSlotRepository slotRepository;

    // POST /api/bookings — customer creates a booking
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Map<String, Object> request) {
        try {
            String customerName  = (String) request.get("customerName");
            String customerPhone = (String) request.get("customerPhone");
            Integer serviceId    = (Integer) request.get("serviceId");
            Integer slotId       = (Integer) request.get("slotId");

            Service service = serviceRepository.findById(serviceId)
                    .orElseThrow(() -> new RuntimeException("Service not found"));
            TimeSlot slot = slotRepository.findById(slotId)
                    .orElseThrow(() -> new RuntimeException("Slot not found"));

            Booking booking = new Booking();
            booking.setCustomerName(customerName);
            booking.setCustomerPhone(customerPhone);
            booking.setService(service);
            booking.setSlot(slot);
            booking.setStatus(Booking.BookingStatus.PENDING);

            Booking saved = bookingRepository.save(booking);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/bookings — admin gets all bookings
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingRepository.findAllByOrderByCreatedAtDesc());
    }

    // PUT /api/bookings/{id}/status — admin updates booking status
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Integer id,
                                          @RequestBody Map<String, String> request) {
        String status = request.get("status");
        return bookingRepository.findById(id).map(booking -> {
            booking.setStatus(Booking.BookingStatus.valueOf(status));
            return ResponseEntity.ok(bookingRepository.save(booking));
        }).orElse(ResponseEntity.notFound().build());
    }
}