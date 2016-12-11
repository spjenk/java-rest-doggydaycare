package com.litereaction.doggydaycare.repository;

import com.litereaction.doggydaycare.model.Availability;
import com.litereaction.doggydaycare.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional
public interface AvailabilityRepository extends JpaRepository<Availability, String> {

    Optional<Availability> findById(String id);

    public List<Availability> findByBookingDate(LocalDate bookingDate);

    public List<Availability> findByBookingDateAndTenant(LocalDate bookingDate, Tenant tenant);

}