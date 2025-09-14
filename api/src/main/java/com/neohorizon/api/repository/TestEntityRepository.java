package com.neohorizon.api.repository;

import com.neohorizon.api.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestEntityRepository extends JpaRepository<TestEntity, Long> {

}
