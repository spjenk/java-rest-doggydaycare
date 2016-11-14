package com.litereaction.doggydaycare.repository;

import com.litereaction.doggydaycare.Model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface OwnerRepository extends JpaRepository<Owner, Long> {

    public List<Owner> findByName(String name);

    public List<Owner> findByEmail(String email);
}