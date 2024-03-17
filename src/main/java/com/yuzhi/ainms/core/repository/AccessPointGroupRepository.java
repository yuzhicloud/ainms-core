package com.yuzhi.ainms.core.repository;

import com.yuzhi.ainms.core.domain.AccessPointGroup;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AccessPointGroup entity.
 *
 * When extending this class, extend AccessPointGroupRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface AccessPointGroupRepository extends AccessPointGroupRepositoryWithBagRelationships, JpaRepository<AccessPointGroup, Long> {
    default Optional<AccessPointGroup> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<AccessPointGroup> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<AccessPointGroup> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
