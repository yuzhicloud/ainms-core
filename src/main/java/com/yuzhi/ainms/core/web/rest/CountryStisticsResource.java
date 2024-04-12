package com.yuzhi.ainms.core.web.rest;

import com.yuzhi.ainms.core.domain.CountryStistics;
import com.yuzhi.ainms.core.repository.CountryStisticsRepository;
import com.yuzhi.ainms.core.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.yuzhi.ainms.core.domain.CountryStistics}.
 */
@RestController
@RequestMapping("/api/country-stistics")
@Transactional
public class CountryStisticsResource {

  private final Logger log = LoggerFactory.getLogger(
    CountryStisticsResource.class
  );

  private static final String ENTITY_NAME = "countryStistics";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final CountryStisticsRepository countryStisticsRepository;

  public CountryStisticsResource(
    CountryStisticsRepository countryStisticsRepository
  ) {
    this.countryStisticsRepository = countryStisticsRepository;
  }

  /**
   * {@code POST  /country-stistics} : Create a new countryStistics.
   *
   * @param countryStistics the countryStistics to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new countryStistics, or with status {@code 400 (Bad Request)} if the countryStistics has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("")
  public ResponseEntity<CountryStistics> createCountryStistics(
    @Valid @RequestBody CountryStistics countryStistics
  ) throws URISyntaxException {
    log.debug("REST request to save CountryStistics : {}", countryStistics);
    if (countryStistics.getId() != null) {
      throw new BadRequestAlertException(
        "A new countryStistics cannot already have an ID",
        ENTITY_NAME,
        "idexists"
      );
    }
    countryStistics = countryStisticsRepository.save(countryStistics);
    return ResponseEntity.created(
      new URI("/api/country-stistics/" + countryStistics.getId())
    )
      .headers(
        HeaderUtil.createEntityCreationAlert(
          applicationName,
          true,
          ENTITY_NAME,
          countryStistics.getId().toString()
        )
      )
      .body(countryStistics);
  }

  /**
   * {@code PUT  /country-stistics/:id} : Updates an existing countryStistics.
   *
   * @param id the id of the countryStistics to save.
   * @param countryStistics the countryStistics to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated countryStistics,
   * or with status {@code 400 (Bad Request)} if the countryStistics is not valid,
   * or with status {@code 500 (Internal Server Error)} if the countryStistics couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/{id}")
  public ResponseEntity<CountryStistics> updateCountryStistics(
    @PathVariable(value = "id", required = false) final Long id,
    @Valid @RequestBody CountryStistics countryStistics
  ) throws URISyntaxException {
    log.debug(
      "REST request to update CountryStistics : {}, {}",
      id,
      countryStistics
    );
    if (countryStistics.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, countryStistics.getId())) {
      throw new BadRequestAlertException(
        "Invalid ID",
        ENTITY_NAME,
        "idinvalid"
      );
    }

    if (!countryStisticsRepository.existsById(id)) {
      throw new BadRequestAlertException(
        "Entity not found",
        ENTITY_NAME,
        "idnotfound"
      );
    }

    countryStistics = countryStisticsRepository.save(countryStistics);
    return ResponseEntity.ok()
      .headers(
        HeaderUtil.createEntityUpdateAlert(
          applicationName,
          true,
          ENTITY_NAME,
          countryStistics.getId().toString()
        )
      )
      .body(countryStistics);
  }

  /**
   * {@code PATCH  /country-stistics/:id} : Partial updates given fields of an existing countryStistics, field will ignore if it is null
   *
   * @param id the id of the countryStistics to save.
   * @param countryStistics the countryStistics to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated countryStistics,
   * or with status {@code 400 (Bad Request)} if the countryStistics is not valid,
   * or with status {@code 404 (Not Found)} if the countryStistics is not found,
   * or with status {@code 500 (Internal Server Error)} if the countryStistics couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(
    value = "/{id}",
    consumes = { "application/json", "application/merge-patch+json" }
  )
  public ResponseEntity<CountryStistics> partialUpdateCountryStistics(
    @PathVariable(value = "id", required = false) final Long id,
    @NotNull @RequestBody CountryStistics countryStistics
  ) throws URISyntaxException {
    log.debug(
      "REST request to partial update CountryStistics partially : {}, {}",
      id,
      countryStistics
    );
    if (countryStistics.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, countryStistics.getId())) {
      throw new BadRequestAlertException(
        "Invalid ID",
        ENTITY_NAME,
        "idinvalid"
      );
    }

    if (!countryStisticsRepository.existsById(id)) {
      throw new BadRequestAlertException(
        "Entity not found",
        ENTITY_NAME,
        "idnotfound"
      );
    }

    Optional<CountryStistics> result = countryStisticsRepository
      .findById(countryStistics.getId())
      .map(existingCountryStistics -> {
        if (countryStistics.getName() != null) {
          existingCountryStistics.setName(countryStistics.getName());
        }
        if (countryStistics.getTotalCount() != null) {
          existingCountryStistics.setTotalCount(
            countryStistics.getTotalCount()
          );
        }
        if (countryStistics.getOnlineCount() != null) {
          existingCountryStistics.setOnlineCount(
            countryStistics.getOnlineCount()
          );
        }
        if (countryStistics.getOfflineCount() != null) {
          existingCountryStistics.setOfflineCount(
            countryStistics.getOfflineCount()
          );
        }
        if (countryStistics.getOtherCount() != null) {
          existingCountryStistics.setOtherCount(
            countryStistics.getOtherCount()
          );
        }
        if (countryStistics.getStatisticDate() != null) {
          existingCountryStistics.setStatisticDate(
            countryStistics.getStatisticDate()
          );
        }
        if (countryStistics.getStatisticTime() != null) {
          existingCountryStistics.setStatisticTime(
            countryStistics.getStatisticTime()
          );
        }

        return existingCountryStistics;
      })
      .map(countryStisticsRepository::save);

    return ResponseUtil.wrapOrNotFound(
      result,
      HeaderUtil.createEntityUpdateAlert(
        applicationName,
        true,
        ENTITY_NAME,
        countryStistics.getId().toString()
      )
    );
  }

  /**
   * {@code GET  /country-stistics} : get all the countryStistics.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of countryStistics in body.
   */
  @GetMapping("")
  public List<CountryStistics> getAllCountryStistics() {
    log.debug("REST request to get all CountryStistics");
    return countryStisticsRepository.findAll();
  }

  /**
   * {@code GET  /country-stistics/:id} : get the "id" countryStistics.
   *
   * @param id the id of the countryStistics to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the countryStistics, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/{id}")
  public ResponseEntity<CountryStistics> getCountryStistics(
    @PathVariable("id") Long id
  ) {
    log.debug("REST request to get CountryStistics : {}", id);
    Optional<CountryStistics> countryStistics =
      countryStisticsRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(countryStistics);
  }

  /**
   * {@code DELETE  /country-stistics/:id} : delete the "id" countryStistics.
   *
   * @param id the id of the countryStistics to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCountryStistics(
    @PathVariable("id") Long id
  ) {
    log.debug("REST request to delete CountryStistics : {}", id);
    countryStisticsRepository.deleteById(id);
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
