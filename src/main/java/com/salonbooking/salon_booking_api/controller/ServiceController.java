package com.salonbooking.salon_booking_api.controller;

import com.salonbooking.salon_booking_api.model.Service;
import com.salonbooking.salon_booking_api.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    @Autowired
    private ServiceRepository serviceRepository;

    // GET /api/services — returns all active services (used by customer page)
    @GetMapping
    public ResponseEntity<List<Service>> getAllServices() {
        List<Service> services = serviceRepository.findByIsActiveTrue();
        return ResponseEntity.ok(services);
    }

    // GET /api/services/all — returns all services including inactive (used by admin)
    @GetMapping("/all")
    public ResponseEntity<List<Service>> getAllServicesAdmin() {
        List<Service> services = serviceRepository.findAll();
        return ResponseEntity.ok(services);
    }

    // POST /api/services — add new service (admin only)
    @PostMapping
    public ResponseEntity<Service> createService(@RequestBody Service service) {
        Service saved = serviceRepository.save(service);
        return ResponseEntity.ok(saved);
    }

    // PUT /api/services/{id} — update service (admin only)
    @PutMapping("/{id}")
    public ResponseEntity<Service> updateService(@PathVariable Integer id,
                                                 @RequestBody Service updated) {
        return serviceRepository.findById(id).map(service -> {
            service.setName(updated.getName());
            service.setDescription(updated.getDescription());
            service.setPrice(updated.getPrice());
            service.setDurationMinutes(updated.getDurationMinutes());
            service.setIsActive(updated.getIsActive());
            return ResponseEntity.ok(serviceRepository.save(service));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/services/{id} — soft delete (just marks inactive)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteService(@PathVariable Integer id) {
        return serviceRepository.findById(id).map(service -> {
            service.setIsActive(false);
            serviceRepository.save(service);
            return ResponseEntity.ok("Service deactivated");
        }).orElse(ResponseEntity.notFound().build());
    }
}
