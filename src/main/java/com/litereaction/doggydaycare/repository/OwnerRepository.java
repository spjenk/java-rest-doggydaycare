package com.litereaction.doggydaycare.repository;

import com.litereaction.doggydaycare.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public interface OwnerRepository extends JpaRepository<Owner, Long> {

    public List<Owner> findByName(String name);

    public List<Owner> findByEmail(String email);

    Optional<Owner> findById(long id);
}