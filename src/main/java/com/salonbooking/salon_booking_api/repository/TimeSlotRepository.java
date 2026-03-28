package com.salonbooking.salon_booking_api.repository;


import com.salonbooking.salon_booking_api.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Integer> {

    // Get all active slots for a specific date
    List<TimeSlot> findBySlotDateAndIsActiveTrueOrderBySlotTime(LocalDate slotDate);

    // Get available slots — slots that have fewer bookings than max_bookings
    @Query("""
        SELECT t FROM TimeSlot t
        WHERE t.slotDate = :date
        AND t.isActive = true
        AND (SELECT COUNT(b) FROM Booking b
             WHERE b.slot.id = t.id
             AND b.status != 'CANCELLED') < t.maxBookings
        ORDER BY t.slotTime
    """)
    List<TimeSlot> findAvailableSlotsByDate(@Param("date") LocalDate date);
}
