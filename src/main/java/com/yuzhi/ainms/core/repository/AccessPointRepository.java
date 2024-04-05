package com.yuzhi.ainms.core.repository;

import com.yuzhi.ainms.core.domain.AccessPoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Spring Data JPA repository for the AccessPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccessPointRepository extends JpaRepository<AccessPoint, Long> {
    @Query("SELECT ap FROM AccessPoint ap WHERE ap.group.id IN :groupIds")
    Page<AccessPoint> findByGroupIdsIn(@Param("groupIds") List<Long> groupIds, Pageable pageable);

    ////   List<AccessPoint> findByGroupIdIn(List<Long> groupIds);
}
