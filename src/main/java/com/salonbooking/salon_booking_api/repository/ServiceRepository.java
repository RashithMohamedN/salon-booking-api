package com.salonbooking.salon_booking_api.repository;

import com.salonbooking.salon_booking_api.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Integer> {
    List<Service> findByIsActiveTrue();
}