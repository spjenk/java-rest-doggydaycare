package com.litereaction.doggydaycare.repository;

import com.litereaction.doggydaycare.Model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface PetRepository extends JpaRepository<Pet, Long> {

    public List<Pet> findByName(String name);

}