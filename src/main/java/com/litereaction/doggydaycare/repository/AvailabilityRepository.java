package com.litereaction.doggydaycare.repository;

import com.litereaction.doggydaycare.Model.Availability;
import com.litereaction.doggydaycare.Model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface AvailabilityRepository extends JpaRepository<Availability, String> {

}