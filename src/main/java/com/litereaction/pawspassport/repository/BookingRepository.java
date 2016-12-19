package com.litereaction.pawspassport.repository;

import com.litereaction.pawspassport.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findById(long id);

}