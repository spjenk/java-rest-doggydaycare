package com.litereaction.doggydaycare.repository;

import com.litereaction.doggydaycare.Model.Caregiver;
import com.litereaction.doggydaycare.Model.Paws;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface CaregiverRepository extends CrudRepository<Caregiver, Long> {

}