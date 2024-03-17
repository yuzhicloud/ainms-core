package com.yuzhi.ainms.core.web.rest;

import com.yuzhi.ainms.core.domain.AccessController;
import com.yuzhi.ainms.core.repository.AccessControllerRepository;
import com.yuzhi.ainms.core.service.AccessControllerService;
import com.yuzhi.ainms.core.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.yuzhi.ainms.core.domain.AccessController}.
 */
@RestController
@RequestMapping("/api/access-controllers")
public class AccessControllerResource {

    private final Logger log = LoggerFactory.getLogger(AccessControllerResource.class);

    private static final String ENTITY_NAME = "accessController";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccessControllerService accessControllerService;

    private final AccessControllerRepository accessControllerRepository;

    public AccessControllerResource(
        AccessControllerService accessControllerService,
        AccessControllerRepository accessControllerRepository
    ) {
        this.accessControllerService = accessControllerService;
        this.accessControllerRepository = accessControllerRepository;
    }

    /**
     * {@code POST  /access-controllers} : Create a new accessController.
     *
     * @param accessController the accessController to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accessController, or with status {@code 400 (Bad Request)} if the accessController has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AccessController> createAccessController(@RequestBody AccessController accessController)
        throws URISyntaxException {
        log.debug("REST request to save AccessController : {}", accessController);
        if (accessController.getId() != null) {
            throw new BadRequestAlertException("A new accessController cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccessController result = accessControllerService.save(accessController);
        return ResponseEntity
            .created(new URI("/api/access-controllers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /access-controllers/:id} : Updates an existing accessController.
     *
     * @param id the id of the accessController to save.
     * @param accessController the accessController to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accessController,
     * or with status {@code 400 (Bad Request)} if the accessController is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accessController couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AccessController> updateAccessController(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccessController accessController
    ) throws URISyntaxException {
        log.debug("REST request to update AccessController : {}, {}", id, accessController);
        if (accessController.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accessController.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accessControllerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AccessController result = accessControllerService.update(accessController);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, accessController.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /access-controllers/:id} : Partial updates given fields of an existing accessController, field will ignore if it is null
     *
     * @param id the id of the accessController to save.
     * @param accessController the accessController to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accessController,
     * or with status {@code 400 (Bad Request)} if the accessController is not valid,
     * or with status {@code 404 (Not Found)} if the accessController is not found,
     * or with status {@code 500 (Internal Server Error)} if the accessController couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccessController> partialUpdateAccessController(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccessController accessController
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccessController partially : {}, {}", id, accessController);
        if (accessController.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accessController.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accessControllerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccessController> result = accessControllerService.partialUpdate(accessController);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, accessController.getId().toString())
        );
    }

    /**
     * {@code GET  /access-controllers} : get all the accessControllers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accessControllers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AccessController>> getAllAccessControllers(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of AccessControllers");
        Page<AccessController> page = accessControllerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /access-controllers/:id} : get the "id" accessController.
     *
     * @param id the id of the accessController to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accessController, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccessController> getAccessController(@PathVariable("id") Long id) {
        log.debug("REST request to get AccessController : {}", id);
        Optional<AccessController> accessController = accessControllerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accessController);
    }

    /**
     * {@code DELETE  /access-controllers/:id} : delete the "id" accessController.
     *
     * @param id the id of the accessController to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccessController(@PathVariable("id") Long id) {
        log.debug("REST request to delete AccessController : {}", id);
        accessControllerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
