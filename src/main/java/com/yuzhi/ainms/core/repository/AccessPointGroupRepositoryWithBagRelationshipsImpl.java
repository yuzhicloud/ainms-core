package com.yuzhi.ainms.core.repository;

import com.yuzhi.ainms.core.domain.AccessPointGroup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class AccessPointGroupRepositoryWithBagRelationshipsImpl implements AccessPointGroupRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<AccessPointGroup> fetchBagRelationships(Optional<AccessPointGroup> accessPointGroup) {
        return accessPointGroup.map(this::fetchAccessPoints);
    }

    @Override
    public Page<AccessPointGroup> fetchBagRelationships(Page<AccessPointGroup> accessPointGroups) {
        return new PageImpl<>(
            fetchBagRelationships(accessPointGroups.getContent()),
            accessPointGroups.getPageable(),
            accessPointGroups.getTotalElements()
        );
    }

    @Override
    public List<AccessPointGroup> fetchBagRelationships(List<AccessPointGroup> accessPointGroups) {
        return Optional.of(accessPointGroups).map(this::fetchAccessPoints).orElse(Collections.emptyList());
    }

    AccessPointGroup fetchAccessPoints(AccessPointGroup result) {
        return entityManager
            .createQuery(
                "select accessPointGroup from AccessPointGroup accessPointGroup left join fetch accessPointGroup.accessPoints where accessPointGroup.id = :id",
                AccessPointGroup.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<AccessPointGroup> fetchAccessPoints(List<AccessPointGroup> accessPointGroups) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, accessPointGroups.size()).forEach(index -> order.put(accessPointGroups.get(index).getId(), index));
        List<AccessPointGroup> result = entityManager
            .createQuery(
                "select accessPointGroup from AccessPointGroup accessPointGroup left join fetch accessPointGroup.accessPoints where accessPointGroup in :accessPointGroups",
                AccessPointGroup.class
            )
            .setParameter("accessPointGroups", accessPointGroups)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
