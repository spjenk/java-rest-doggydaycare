package com.litereaction.doggydaycare.repository;

import com.litereaction.doggydaycare.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findById(long id);

}