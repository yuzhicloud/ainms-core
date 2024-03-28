package com.yuzhi.ainms.core.web.rest;

import com.yuzhi.ainms.core.domain.PowerPlant;
import com.yuzhi.ainms.core.repository.PowerPlantRepository;
import com.yuzhi.ainms.core.service.PowerPlantService;
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
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.yuzhi.ainms.core.domain.PowerPlant}.
 */
@RestController
@RequestMapping("/api/power-plants")
public class PowerPlantResource {

  private final Logger log = LoggerFactory.getLogger(PowerPlantResource.class);

  private static final String ENTITY_NAME = "powerPlant";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final PowerPlantService powerPlantService;

  private final PowerPlantRepository powerPlantRepository;

  public PowerPlantResource(
    PowerPlantService powerPlantService,
    PowerPlantRepository powerPlantRepository
  ) {
    this.powerPlantService = powerPlantService;
    this.powerPlantRepository = powerPlantRepository;
  }

  /**
   * {@code POST  /power-plants} : Create a new powerPlant.
   *
   * @param powerPlant the powerPlant to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new powerPlant, or with status {@code 400 (Bad Request)} if the powerPlant has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("")
  public ResponseEntity<PowerPlant> createPowerPlant(
    @RequestBody PowerPlant powerPlant
  ) throws URISyntaxException {
    log.debug("REST request to save PowerPlant : {}", powerPlant);
    if (powerPlant.getId() != null) {
      throw new BadRequestAlertException(
        "A new powerPlant cannot already have an ID",
        ENTITY_NAME,
        "idexists"
      );
    }
    powerPlant = powerPlantService.save(powerPlant);
    return ResponseEntity.created(
      new URI("/api/power-plants/" + powerPlant.getId())
    )
      .headers(
        HeaderUtil.createEntityCreationAlert(
          applicationName,
          true,
          ENTITY_NAME,
          powerPlant.getId().toString()
        )
      )
      .body(powerPlant);
  }

  /**
   * {@code PUT  /power-plants/:id} : Updates an existing powerPlant.
   *
   * @param id the id of the powerPlant to save.
   * @param powerPlant the powerPlant to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated powerPlant,
   * or with status {@code 400 (Bad Request)} if the powerPlant is not valid,
   * or with status {@code 500 (Internal Server Error)} if the powerPlant couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/{id}")
  public ResponseEntity<PowerPlant> updatePowerPlant(
    @PathVariable(value = "id", required = false) final Long id,
    @RequestBody PowerPlant powerPlant
  ) throws URISyntaxException {
    log.debug("REST request to update PowerPlant : {}, {}", id, powerPlant);
    if (powerPlant.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, powerPlant.getId())) {
      throw new BadRequestAlertException(
        "Invalid ID",
        ENTITY_NAME,
        "idinvalid"
      );
    }

    if (!powerPlantRepository.existsById(id)) {
      throw new BadRequestAlertException(
        "Entity not found",
        ENTITY_NAME,
        "idnotfound"
      );
    }

    powerPlant = powerPlantService.update(powerPlant);
    return ResponseEntity.ok()
      .headers(
        HeaderUtil.createEntityUpdateAlert(
          applicationName,
          true,
          ENTITY_NAME,
          powerPlant.getId().toString()
        )
      )
      .body(powerPlant);
  }

  /**
   * {@code PATCH  /power-plants/:id} : Partial updates given fields of an existing powerPlant, field will ignore if it is null
   *
   * @param id the id of the powerPlant to save.
   * @param powerPlant the powerPlant to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated powerPlant,
   * or with status {@code 400 (Bad Request)} if the powerPlant is not valid,
   * or with status {@code 404 (Not Found)} if the powerPlant is not found,
   * or with status {@code 500 (Internal Server Error)} if the powerPlant couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(
    value = "/{id}",
    consumes = { "application/json", "application/merge-patch+json" }
  )
  public ResponseEntity<PowerPlant> partialUpdatePowerPlant(
    @PathVariable(value = "id", required = false) final Long id,
    @RequestBody PowerPlant powerPlant
  ) throws URISyntaxException {
    log.debug(
      "REST request to partial update PowerPlant partially : {}, {}",
      id,
      powerPlant
    );
    if (powerPlant.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, powerPlant.getId())) {
      throw new BadRequestAlertException(
        "Invalid ID",
        ENTITY_NAME,
        "idinvalid"
      );
    }

    if (!powerPlantRepository.existsById(id)) {
      throw new BadRequestAlertException(
        "Entity not found",
        ENTITY_NAME,
        "idnotfound"
      );
    }

    Optional<PowerPlant> result = powerPlantService.partialUpdate(powerPlant);

    return ResponseUtil.wrapOrNotFound(
      result,
      HeaderUtil.createEntityUpdateAlert(
        applicationName,
        true,
        ENTITY_NAME,
        powerPlant.getId().toString()
      )
    );
  }

  /**
   * {@code GET  /power-plants} : get all the powerPlants.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of powerPlants in body.
   */
  @GetMapping("")
  public List<PowerPlant> getAllPowerPlants() {
    log.debug("REST request to get all PowerPlants");
    return powerPlantService.findAll();
  }

  /**
   * {@code GET  /power-plants/:id} : get the "id" powerPlant.
   *
   * @param id the id of the powerPlant to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the powerPlant, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/{id}")
  public ResponseEntity<PowerPlant> getPowerPlant(@PathVariable("id") Long id) {
    log.debug("REST request to get PowerPlant : {}", id);
    Optional<PowerPlant> powerPlant = powerPlantService.findOne(id);
    return ResponseUtil.wrapOrNotFound(powerPlant);
  }

  /**
   * {@code DELETE  /power-plants/:id} : delete the "id" powerPlant.
   *
   * @param id the id of the powerPlant to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePowerPlant(@PathVariable("id") Long id) {
    log.debug("REST request to delete PowerPlant : {}", id);
    powerPlantService.delete(id);
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
