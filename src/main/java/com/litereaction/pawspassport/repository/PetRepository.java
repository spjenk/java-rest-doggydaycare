package com.litereaction.pawspassport.repository;

import com.litereaction.pawspassport.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public interface PetRepository extends JpaRepository<Pet, Long> {

    public List<Pet> findByName(String name);

    public List<Pet> findByOwnerId(long ownerId);

    Optional<Pet> findById(long id);

}