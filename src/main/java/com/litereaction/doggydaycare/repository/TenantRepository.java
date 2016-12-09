package com.litereaction.doggydaycare.repository;

import com.litereaction.doggydaycare.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface TenantRepository extends JpaRepository<Tenant, String> {

    Optional<Tenant> findById(long id);

}