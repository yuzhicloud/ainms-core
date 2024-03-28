package com.yuzhi.ainms.core.service;

import com.yuzhi.ainms.core.domain.AccessPoint;
import com.yuzhi.ainms.core.repository.AccessPointRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.yuzhi.ainms.core.domain.AccessPoint}.
 */
@Service
@Transactional
public class AccessPointService {

  private final Logger log = LoggerFactory.getLogger(AccessPointService.class);

  private final AccessPointRepository accessPointRepository;

  public AccessPointService(AccessPointRepository accessPointRepository) {
    this.accessPointRepository = accessPointRepository;
  }

  /**
   * Save a accessPoint.
   *
   * @param accessPoint the entity to save.
   * @return the persisted entity.
   */
  public AccessPoint save(AccessPoint accessPoint) {
    log.debug("Request to save AccessPoint : {}", accessPoint);
    return accessPointRepository.save(accessPoint);
  }

  /**
   * Update a accessPoint.
   *
   * @param accessPoint the entity to save.
   * @return the persisted entity.
   */
  public AccessPoint update(AccessPoint accessPoint) {
    log.debug("Request to update AccessPoint : {}", accessPoint);
    return accessPointRepository.save(accessPoint);
  }

  /**
   * Partially update a accessPoint.
   *
   * @param accessPoint the entity to update partially.
   * @return the persisted entity.
   */
  public Optional<AccessPoint> partialUpdate(AccessPoint accessPoint) {
    log.debug("Request to partially update AccessPoint : {}", accessPoint);

    return accessPointRepository
      .findById(accessPoint.getId())
      .map(existingAccessPoint -> {
        if (accessPoint.getNedn() != null) {
          existingAccessPoint.setNedn(accessPoint.getNedn());
        }
        if (accessPoint.getNeid() != null) {
          existingAccessPoint.setNeid(accessPoint.getNeid());
        }
        if (accessPoint.getAliasname() != null) {
          existingAccessPoint.setAliasname(accessPoint.getAliasname());
        }
        if (accessPoint.getNename() != null) {
          existingAccessPoint.setNename(accessPoint.getNename());
        }
        if (accessPoint.getNecategory() != null) {
          existingAccessPoint.setNecategory(accessPoint.getNecategory());
        }
        if (accessPoint.getNetype() != null) {
          existingAccessPoint.setNetype(accessPoint.getNetype());
        }
        if (accessPoint.getNevendorname() != null) {
          existingAccessPoint.setNevendorname(accessPoint.getNevendorname());
        }
        if (accessPoint.getNeesn() != null) {
          existingAccessPoint.setNeesn(accessPoint.getNeesn());
        }
        if (accessPoint.getNeip() != null) {
          existingAccessPoint.setNeip(accessPoint.getNeip());
        }
        if (accessPoint.getNemac() != null) {
          existingAccessPoint.setNemac(accessPoint.getNemac());
        }
        if (accessPoint.getVersion() != null) {
          existingAccessPoint.setVersion(accessPoint.getVersion());
        }
        if (accessPoint.getNestate() != null) {
          existingAccessPoint.setNestate(accessPoint.getNestate());
        }
        if (accessPoint.getCreatetime() != null) {
          existingAccessPoint.setCreatetime(accessPoint.getCreatetime());
        }
        if (accessPoint.getNeiptype() != null) {
          existingAccessPoint.setNeiptype(accessPoint.getNeiptype());
        }
        if (accessPoint.getSubnet() != null) {
          existingAccessPoint.setSubnet(accessPoint.getSubnet());
        }
        if (accessPoint.getNeosversion() != null) {
          existingAccessPoint.setNeosversion(accessPoint.getNeosversion());
        }

        return existingAccessPoint;
      })
      .map(accessPointRepository::save);
  }

  /**
   * Get all the accessPoints.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public Page<AccessPoint> findAll(Pageable pageable) {
    log.debug("Request to get all AccessPoints");
    return accessPointRepository.findAll(pageable);
  }

  /**
   * Get one accessPoint by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<AccessPoint> findOne(Long id) {
    log.debug("Request to get AccessPoint : {}", id);
    return accessPointRepository.findById(id);
  }

  /**
   * Delete the accessPoint by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete AccessPoint : {}", id);
    accessPointRepository.deleteById(id);
  }
}
