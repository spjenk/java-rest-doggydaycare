package com.litereaction.doggydaycare.repository;

import com.litereaction.doggydaycare.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public interface PetRepository extends JpaRepository<Pet, Long> {

    public List<Pet> findByName(String name);

    Optional<Pet> findById(long id);

}