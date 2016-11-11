package com.litereaction.doggydaycare.repository;

import javax.transaction.Transactional;

import com.litereaction.doggydaycare.Model.Pet;
import org.springframework.data.repository.CrudRepository;

@Transactional
public interface PetRepository extends CrudRepository<Pet, Long> {

    public Iterable<Pet> findByName(String name);

}