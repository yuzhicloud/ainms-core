package com.yuzhi.ainms.core.repository;

import com.yuzhi.ainms.core.domain.Province;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Province entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {
    Optional<Province> findById(Long id);
}
