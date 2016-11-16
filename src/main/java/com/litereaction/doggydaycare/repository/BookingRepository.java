package com.litereaction.doggydaycare.repository;

import com.litereaction.doggydaycare.Model.Booking;
import com.litereaction.doggydaycare.Model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Transactional
public interface BookingRepository extends JpaRepository<Booking, Long> {

    public List<Booking> findByDate(String date);

}