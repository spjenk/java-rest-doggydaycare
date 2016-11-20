package com.litereaction.doggydaycare.repository;

import com.litereaction.doggydaycare.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface AvailabilityRepository extends JpaRepository<Availability, String> {

    Optional<Availability> findByDate(String date);

}