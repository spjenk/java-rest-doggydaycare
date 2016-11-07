package com.litereaction.doggydaycare.repository;

import javax.transaction.Transactional;

import com.litereaction.doggydaycare.Model.Paws;
import org.springframework.data.repository.CrudRepository;

@Transactional
public interface PawsRepository extends CrudRepository<Paws, Long> {

    public Paws findByName(String name);

}