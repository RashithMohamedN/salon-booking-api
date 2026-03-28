package com.salonbooking.salon_booking_api.repository;


import com.salonbooking.salon_booking_api.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByOrderByCreatedAtDesc();
    List<Booking> findByCustomerPhone(String phone);
}