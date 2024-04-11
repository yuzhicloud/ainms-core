package com.yuzhi.ainms.core.web.rest;

import com.yuzhi.ainms.core.domain.NCEAPStatus;
import com.yuzhi.ainms.core.repository.NCEAPStatusRepository;
import com.yuzhi.ainms.core.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.yuzhi.ainms.core.domain.NCEAPStatus}.
 */
@RestController
@RequestMapping("/api/nceap-statuses")
@Transactional
public class NCEAPStatusResource {

  private final Logger log = LoggerFactory.getLogger(NCEAPStatusResource.class);

  private static final String ENTITY_NAME = "nCEAPStatus";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final NCEAPStatusRepository nCEAPStatusRepository;

  public NCEAPStatusResource(NCEAPStatusRepository nCEAPStatusRepository) {
    this.nCEAPStatusRepository = nCEAPStatusRepository;
  }

  /**
   * {@code POST  /nceap-statuses} : Create a new nCEAPStatus.
   *
   * @param nCEAPStatus the nCEAPStatus to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nCEAPStatus, or with status {@code 400 (Bad Request)} if the nCEAPStatus has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("")
  public ResponseEntity<NCEAPStatus> createNCEAPStatus(
    @RequestBody NCEAPStatus nCEAPStatus
  ) throws URISyntaxException {
    log.debug("REST request to save NCEAPStatus : {}", nCEAPStatus);
    if (nCEAPStatus.getId() != null) {
      throw new BadRequestAlertException(
        "A new nCEAPStatus cannot already have an ID",
        ENTITY_NAME,
        "idexists"
      );
    }
    nCEAPStatus = nCEAPStatusRepository.save(nCEAPStatus);
    return ResponseEntity.created(
      new URI("/api/nceap-statuses/" + nCEAPStatus.getId())
    )
      .headers(
        HeaderUtil.createEntityCreationAlert(
          applicationName,
          true,
          ENTITY_NAME,
          nCEAPStatus.getId().toString()
        )
      )
      .body(nCEAPStatus);
  }

  /**
   * {@code PUT  /nceap-statuses/:id} : Updates an existing nCEAPStatus.
   *
   * @param id the id of the nCEAPStatus to save.
   * @param nCEAPStatus the nCEAPStatus to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nCEAPStatus,
   * or with status {@code 400 (Bad Request)} if the nCEAPStatus is not valid,
   * or with status {@code 500 (Internal Server Error)} if the nCEAPStatus couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/{id}")
  public ResponseEntity<NCEAPStatus> updateNCEAPStatus(
    @PathVariable(value = "id", required = false) final Integer id,
    @RequestBody NCEAPStatus nCEAPStatus
  ) throws URISyntaxException {
    log.debug("REST request to update NCEAPStatus : {}, {}", id, nCEAPStatus);
    if (nCEAPStatus.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, nCEAPStatus.getId())) {
      throw new BadRequestAlertException(
        "Invalid ID",
        ENTITY_NAME,
        "idinvalid"
      );
    }

    if (!nCEAPStatusRepository.existsById(id)) {
      throw new BadRequestAlertException(
        "Entity not found",
        ENTITY_NAME,
        "idnotfound"
      );
    }

    nCEAPStatus = nCEAPStatusRepository.save(nCEAPStatus);
    return ResponseEntity.ok()
      .headers(
        HeaderUtil.createEntityUpdateAlert(
          applicationName,
          true,
          ENTITY_NAME,
          nCEAPStatus.getId().toString()
        )
      )
      .body(nCEAPStatus);
  }

  /**
   * {@code PATCH  /nceap-statuses/:id} : Partial updates given fields of an existing nCEAPStatus, field will ignore if it is null
   *
   * @param id the id of the nCEAPStatus to save.
   * @param nCEAPStatus the nCEAPStatus to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nCEAPStatus,
   * or with status {@code 400 (Bad Request)} if the nCEAPStatus is not valid,
   * or with status {@code 404 (Not Found)} if the nCEAPStatus is not found,
   * or with status {@code 500 (Internal Server Error)} if the nCEAPStatus couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(
    value = "/{id}",
    consumes = { "application/json", "application/merge-patch+json" }
  )
  public ResponseEntity<NCEAPStatus> partialUpdateNCEAPStatus(
    @PathVariable(value = "id", required = false) final Integer id,
    @RequestBody NCEAPStatus nCEAPStatus
  ) throws URISyntaxException {
    log.debug(
      "REST request to partial update NCEAPStatus partially : {}, {}",
      id,
      nCEAPStatus
    );
    if (nCEAPStatus.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, nCEAPStatus.getId())) {
      throw new BadRequestAlertException(
        "Invalid ID",
        ENTITY_NAME,
        "idinvalid"
      );
    }

    if (!nCEAPStatusRepository.existsById(id)) {
      throw new BadRequestAlertException(
        "Entity not found",
        ENTITY_NAME,
        "idnotfound"
      );
    }

    Optional<NCEAPStatus> result = nCEAPStatusRepository
      .findById(nCEAPStatus.getId())
      .map(existingNCEAPStatus -> {
        if (nCEAPStatus.getApSn() != null) {
          existingNCEAPStatus.setApSn(nCEAPStatus.getApSn());
        }
        if (nCEAPStatus.getApStatus() != null) {
          existingNCEAPStatus.setApStatus(nCEAPStatus.getApStatus());
        }

        return existingNCEAPStatus;
      })
      .map(nCEAPStatusRepository::save);

    return ResponseUtil.wrapOrNotFound(
      result,
      HeaderUtil.createEntityUpdateAlert(
        applicationName,
        true,
        ENTITY_NAME,
        nCEAPStatus.getId().toString()
      )
    );
  }

  /**
   * {@code GET  /nceap-statuses} : get all the nCEAPStatuses.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nCEAPStatuses in body.
   */
  @GetMapping("")
  public List<NCEAPStatus> getAllNCEAPStatuses() {
    log.debug("REST request to get all NCEAPStatuses");
    return nCEAPStatusRepository.findAll();
  }

  /**
   * {@code GET  /nceap-statuses/:id} : get the "id" nCEAPStatus.
   *
   * @param id the id of the nCEAPStatus to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nCEAPStatus, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/{id}")
  public ResponseEntity<NCEAPStatus> getNCEAPStatus(
    @PathVariable("id") Integer id
  ) {
    log.debug("REST request to get NCEAPStatus : {}", id);
    Optional<NCEAPStatus> nCEAPStatus = nCEAPStatusRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(nCEAPStatus);
  }

  /**
   * {@code DELETE  /nceap-statuses/:id} : delete the "id" nCEAPStatus.
   *
   * @param id the id of the nCEAPStatus to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteNCEAPStatus(
    @PathVariable("id") Integer id
  ) {
    log.debug("REST request to delete NCEAPStatus : {}", id);
    nCEAPStatusRepository.deleteById(id);
    return ResponseEntity.noContent()
      .headers(
        HeaderUtil.createEntityDeletionAlert(
          applicationName,
          true,
          ENTITY_NAME,
          id.toString()
        )
      )
      .build();
  }
}
