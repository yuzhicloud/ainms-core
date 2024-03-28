package com.yuzhi.ainms.core.web.rest;

import static com.yuzhi.ainms.core.domain.AccessControllerAsserts.*;
import static com.yuzhi.ainms.core.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuzhi.ainms.core.IntegrationTest;
import com.yuzhi.ainms.core.domain.AccessController;
import com.yuzhi.ainms.core.repository.AccessControllerRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AccessControllerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccessControllerResourceIT {

  private static final String DEFAULT_NEDN = "AAAAAAAAAA";
  private static final String UPDATED_NEDN = "BBBBBBBBBB";

  private static final Integer DEFAULT_NEID = 1;
  private static final Integer UPDATED_NEID = 2;

  private static final String DEFAULT_ALIASNAME = "AAAAAAAAAA";
  private static final String UPDATED_ALIASNAME = "BBBBBBBBBB";

  private static final String DEFAULT_NENAME = "AAAAAAAAAA";
  private static final String UPDATED_NENAME = "BBBBBBBBBB";

  private static final String DEFAULT_NECATEGORY = "AAAAAAAAAA";
  private static final String UPDATED_NECATEGORY = "BBBBBBBBBB";

  private static final String DEFAULT_NETYPE = "AAAAAAAAAA";
  private static final String UPDATED_NETYPE = "BBBBBBBBBB";

  private static final String DEFAULT_NEVENDORNAME = "AAAAAAAAAA";
  private static final String UPDATED_NEVENDORNAME = "BBBBBBBBBB";

  private static final String DEFAULT_NEESN = "AAAAAAAAAA";
  private static final String UPDATED_NEESN = "BBBBBBBBBB";

  private static final String DEFAULT_NEIP = "AAAAAAAAAA";
  private static final String UPDATED_NEIP = "BBBBBBBBBB";

  private static final String DEFAULT_NEMAC = "AAAAAAAAAA";
  private static final String UPDATED_NEMAC = "BBBBBBBBBB";

  private static final String DEFAULT_VERSION = "AAAAAAAAAA";
  private static final String UPDATED_VERSION = "BBBBBBBBBB";

  private static final Integer DEFAULT_NESTATE = 1;
  private static final Integer UPDATED_NESTATE = 2;

  private static final String DEFAULT_CREATETIME = "AAAAAAAAAA";
  private static final String UPDATED_CREATETIME = "BBBBBBBBBB";

  private static final Integer DEFAULT_NEIPTYPE = 1;
  private static final Integer UPDATED_NEIPTYPE = 2;

  private static final String DEFAULT_SUBNET = "AAAAAAAAAA";
  private static final String UPDATED_SUBNET = "BBBBBBBBBB";

  private static final String DEFAULT_NEOSVERSION = "AAAAAAAAAA";
  private static final String UPDATED_NEOSVERSION = "BBBBBBBBBB";

  private static final String ENTITY_API_URL = "/api/access-controllers";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong longCount = new AtomicLong(
    random.nextInt() + (2 * Integer.MAX_VALUE)
  );

  @Autowired
  private ObjectMapper om;

  @Autowired
  private AccessControllerRepository accessControllerRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restAccessControllerMockMvc;

  private AccessController accessController;

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static AccessController createEntity(EntityManager em) {
    AccessController accessController = new AccessController()
      .nedn(DEFAULT_NEDN)
      .neid(DEFAULT_NEID)
      .aliasname(DEFAULT_ALIASNAME)
      .nename(DEFAULT_NENAME)
      .necategory(DEFAULT_NECATEGORY)
      .netype(DEFAULT_NETYPE)
      .nevendorname(DEFAULT_NEVENDORNAME)
      .neesn(DEFAULT_NEESN)
      .neip(DEFAULT_NEIP)
      .nemac(DEFAULT_NEMAC)
      .version(DEFAULT_VERSION)
      .nestate(DEFAULT_NESTATE)
      .createtime(DEFAULT_CREATETIME)
      .neiptype(DEFAULT_NEIPTYPE)
      .subnet(DEFAULT_SUBNET)
      .neosversion(DEFAULT_NEOSVERSION);
    return accessController;
  }

  /**
   * Create an updated entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static AccessController createUpdatedEntity(EntityManager em) {
    AccessController accessController = new AccessController()
      .nedn(UPDATED_NEDN)
      .neid(UPDATED_NEID)
      .aliasname(UPDATED_ALIASNAME)
      .nename(UPDATED_NENAME)
      .necategory(UPDATED_NECATEGORY)
      .netype(UPDATED_NETYPE)
      .nevendorname(UPDATED_NEVENDORNAME)
      .neesn(UPDATED_NEESN)
      .neip(UPDATED_NEIP)
      .nemac(UPDATED_NEMAC)
      .version(UPDATED_VERSION)
      .nestate(UPDATED_NESTATE)
      .createtime(UPDATED_CREATETIME)
      .neiptype(UPDATED_NEIPTYPE)
      .subnet(UPDATED_SUBNET)
      .neosversion(UPDATED_NEOSVERSION);
    return accessController;
  }

  @BeforeEach
  public void initTest() {
    accessController = createEntity(em);
  }

  @Test
  @Transactional
  void createAccessController() throws Exception {
    long databaseSizeBeforeCreate = getRepositoryCount();
    // Create the AccessController
    var returnedAccessController = om.readValue(
      restAccessControllerMockMvc
        .perform(
          post(ENTITY_API_URL)
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsBytes(accessController))
        )
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString(),
      AccessController.class
    );

    // Validate the AccessController in the database
    assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
    assertAccessControllerUpdatableFieldsEquals(
      returnedAccessController,
      getPersistedAccessController(returnedAccessController)
    );
  }

  @Test
  @Transactional
  void createAccessControllerWithExistingId() throws Exception {
    // Create the AccessController with an existing ID
    accessController.setId(1L);

    long databaseSizeBeforeCreate = getRepositoryCount();

    // An entity with an existing ID cannot be created, so this API call must fail
    restAccessControllerMockMvc
      .perform(
        post(ENTITY_API_URL)
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(accessController))
      )
      .andExpect(status().isBadRequest());

    // Validate the AccessController in the database
    assertSameRepositoryCount(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void getAllAccessControllers() throws Exception {
    // Initialize the database
    accessControllerRepository.saveAndFlush(accessController);

    // Get all the accessControllerList
    restAccessControllerMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(
        jsonPath("$.[*].id").value(hasItem(accessController.getId().intValue()))
      )
      .andExpect(jsonPath("$.[*].nedn").value(hasItem(DEFAULT_NEDN)))
      .andExpect(jsonPath("$.[*].neid").value(hasItem(DEFAULT_NEID)))
      .andExpect(jsonPath("$.[*].aliasname").value(hasItem(DEFAULT_ALIASNAME)))
      .andExpect(jsonPath("$.[*].nename").value(hasItem(DEFAULT_NENAME)))
      .andExpect(
        jsonPath("$.[*].necategory").value(hasItem(DEFAULT_NECATEGORY))
      )
      .andExpect(jsonPath("$.[*].netype").value(hasItem(DEFAULT_NETYPE)))
      .andExpect(
        jsonPath("$.[*].nevendorname").value(hasItem(DEFAULT_NEVENDORNAME))
      )
      .andExpect(jsonPath("$.[*].neesn").value(hasItem(DEFAULT_NEESN)))
      .andExpect(jsonPath("$.[*].neip").value(hasItem(DEFAULT_NEIP)))
      .andExpect(jsonPath("$.[*].nemac").value(hasItem(DEFAULT_NEMAC)))
      .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
      .andExpect(jsonPath("$.[*].nestate").value(hasItem(DEFAULT_NESTATE)))
      .andExpect(
        jsonPath("$.[*].createtime").value(hasItem(DEFAULT_CREATETIME))
      )
      .andExpect(jsonPath("$.[*].neiptype").value(hasItem(DEFAULT_NEIPTYPE)))
      .andExpect(jsonPath("$.[*].subnet").value(hasItem(DEFAULT_SUBNET)))
      .andExpect(
        jsonPath("$.[*].neosversion").value(hasItem(DEFAULT_NEOSVERSION))
      );
  }

  @Test
  @Transactional
  void getAccessController() throws Exception {
    // Initialize the database
    accessControllerRepository.saveAndFlush(accessController);

    // Get the accessController
    restAccessControllerMockMvc
      .perform(get(ENTITY_API_URL_ID, accessController.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(accessController.getId().intValue()))
      .andExpect(jsonPath("$.nedn").value(DEFAULT_NEDN))
      .andExpect(jsonPath("$.neid").value(DEFAULT_NEID))
      .andExpect(jsonPath("$.aliasname").value(DEFAULT_ALIASNAME))
      .andExpect(jsonPath("$.nename").value(DEFAULT_NENAME))
      .andExpect(jsonPath("$.necategory").value(DEFAULT_NECATEGORY))
      .andExpect(jsonPath("$.netype").value(DEFAULT_NETYPE))
      .andExpect(jsonPath("$.nevendorname").value(DEFAULT_NEVENDORNAME))
      .andExpect(jsonPath("$.neesn").value(DEFAULT_NEESN))
      .andExpect(jsonPath("$.neip").value(DEFAULT_NEIP))
      .andExpect(jsonPath("$.nemac").value(DEFAULT_NEMAC))
      .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
      .andExpect(jsonPath("$.nestate").value(DEFAULT_NESTATE))
      .andExpect(jsonPath("$.createtime").value(DEFAULT_CREATETIME))
      .andExpect(jsonPath("$.neiptype").value(DEFAULT_NEIPTYPE))
      .andExpect(jsonPath("$.subnet").value(DEFAULT_SUBNET))
      .andExpect(jsonPath("$.neosversion").value(DEFAULT_NEOSVERSION));
  }

  @Test
  @Transactional
  void getNonExistingAccessController() throws Exception {
    // Get the accessController
    restAccessControllerMockMvc
      .perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
      .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putExistingAccessController() throws Exception {
    // Initialize the database
    accessControllerRepository.saveAndFlush(accessController);

    long databaseSizeBeforeUpdate = getRepositoryCount();

    // Update the accessController
    AccessController updatedAccessController = accessControllerRepository
      .findById(accessController.getId())
      .orElseThrow();
    // Disconnect from session so that the updates on updatedAccessController are not directly saved in db
    em.detach(updatedAccessController);
    updatedAccessController
      .nedn(UPDATED_NEDN)
      .neid(UPDATED_NEID)
      .aliasname(UPDATED_ALIASNAME)
      .nename(UPDATED_NENAME)
      .necategory(UPDATED_NECATEGORY)
      .netype(UPDATED_NETYPE)
      .nevendorname(UPDATED_NEVENDORNAME)
      .neesn(UPDATED_NEESN)
      .neip(UPDATED_NEIP)
      .nemac(UPDATED_NEMAC)
      .version(UPDATED_VERSION)
      .nestate(UPDATED_NESTATE)
      .createtime(UPDATED_CREATETIME)
      .neiptype(UPDATED_NEIPTYPE)
      .subnet(UPDATED_SUBNET)
      .neosversion(UPDATED_NEOSVERSION);

    restAccessControllerMockMvc
      .perform(
        put(ENTITY_API_URL_ID, updatedAccessController.getId())
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(updatedAccessController))
      )
      .andExpect(status().isOk());

    // Validate the AccessController in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
    assertPersistedAccessControllerToMatchAllProperties(
      updatedAccessController
    );
  }

  @Test
  @Transactional
  void putNonExistingAccessController() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    accessController.setId(longCount.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restAccessControllerMockMvc
      .perform(
        put(ENTITY_API_URL_ID, accessController.getId())
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(accessController))
      )
      .andExpect(status().isBadRequest());

    // Validate the AccessController in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchAccessController() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    accessController.setId(longCount.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restAccessControllerMockMvc
      .perform(
        put(ENTITY_API_URL_ID, longCount.incrementAndGet())
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(accessController))
      )
      .andExpect(status().isBadRequest());

    // Validate the AccessController in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamAccessController() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    accessController.setId(longCount.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restAccessControllerMockMvc
      .perform(
        put(ENTITY_API_URL)
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(accessController))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the AccessController in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdateAccessControllerWithPatch() throws Exception {
    // Initialize the database
    accessControllerRepository.saveAndFlush(accessController);

    long databaseSizeBeforeUpdate = getRepositoryCount();

    // Update the accessController using partial update
    AccessController partialUpdatedAccessController = new AccessController();
    partialUpdatedAccessController.setId(accessController.getId());

    partialUpdatedAccessController
      .nedn(UPDATED_NEDN)
      .aliasname(UPDATED_ALIASNAME)
      .nename(UPDATED_NENAME)
      .necategory(UPDATED_NECATEGORY)
      .netype(UPDATED_NETYPE)
      .nemac(UPDATED_NEMAC)
      .version(UPDATED_VERSION)
      .createtime(UPDATED_CREATETIME)
      .neiptype(UPDATED_NEIPTYPE);

    restAccessControllerMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedAccessController.getId())
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(partialUpdatedAccessController))
      )
      .andExpect(status().isOk());

    // Validate the AccessController in the database

    assertSameRepositoryCount(databaseSizeBeforeUpdate);
    assertAccessControllerUpdatableFieldsEquals(
      createUpdateProxyForBean(
        partialUpdatedAccessController,
        accessController
      ),
      getPersistedAccessController(accessController)
    );
  }

  @Test
  @Transactional
  void fullUpdateAccessControllerWithPatch() throws Exception {
    // Initialize the database
    accessControllerRepository.saveAndFlush(accessController);

    long databaseSizeBeforeUpdate = getRepositoryCount();

    // Update the accessController using partial update
    AccessController partialUpdatedAccessController = new AccessController();
    partialUpdatedAccessController.setId(accessController.getId());

    partialUpdatedAccessController
      .nedn(UPDATED_NEDN)
      .neid(UPDATED_NEID)
      .aliasname(UPDATED_ALIASNAME)
      .nename(UPDATED_NENAME)
      .necategory(UPDATED_NECATEGORY)
      .netype(UPDATED_NETYPE)
      .nevendorname(UPDATED_NEVENDORNAME)
      .neesn(UPDATED_NEESN)
      .neip(UPDATED_NEIP)
      .nemac(UPDATED_NEMAC)
      .version(UPDATED_VERSION)
      .nestate(UPDATED_NESTATE)
      .createtime(UPDATED_CREATETIME)
      .neiptype(UPDATED_NEIPTYPE)
      .subnet(UPDATED_SUBNET)
      .neosversion(UPDATED_NEOSVERSION);

    restAccessControllerMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedAccessController.getId())
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(partialUpdatedAccessController))
      )
      .andExpect(status().isOk());

    // Validate the AccessController in the database

    assertSameRepositoryCount(databaseSizeBeforeUpdate);
    assertAccessControllerUpdatableFieldsEquals(
      partialUpdatedAccessController,
      getPersistedAccessController(partialUpdatedAccessController)
    );
  }

  @Test
  @Transactional
  void patchNonExistingAccessController() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    accessController.setId(longCount.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restAccessControllerMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, accessController.getId())
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(accessController))
      )
      .andExpect(status().isBadRequest());

    // Validate the AccessController in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchAccessController() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    accessController.setId(longCount.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restAccessControllerMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(accessController))
      )
      .andExpect(status().isBadRequest());

    // Validate the AccessController in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamAccessController() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    accessController.setId(longCount.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restAccessControllerMockMvc
      .perform(
        patch(ENTITY_API_URL)
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(accessController))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the AccessController in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deleteAccessController() throws Exception {
    // Initialize the database
    accessControllerRepository.saveAndFlush(accessController);

    long databaseSizeBeforeDelete = getRepositoryCount();

    // Delete the accessController
    restAccessControllerMockMvc
      .perform(
        delete(ENTITY_API_URL_ID, accessController.getId())
          .with(csrf())
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
  }

  protected long getRepositoryCount() {
    return accessControllerRepository.count();
  }

  protected void assertIncrementedRepositoryCount(long countBefore) {
    assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
  }

  protected void assertDecrementedRepositoryCount(long countBefore) {
    assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
  }

  protected void assertSameRepositoryCount(long countBefore) {
    assertThat(countBefore).isEqualTo(getRepositoryCount());
  }

  protected AccessController getPersistedAccessController(
    AccessController accessController
  ) {
    return accessControllerRepository
      .findById(accessController.getId())
      .orElseThrow();
  }

  protected void assertPersistedAccessControllerToMatchAllProperties(
    AccessController expectedAccessController
  ) {
    assertAccessControllerAllPropertiesEquals(
      expectedAccessController,
      getPersistedAccessController(expectedAccessController)
    );
  }

  protected void assertPersistedAccessControllerToMatchUpdatableProperties(
    AccessController expectedAccessController
  ) {
    assertAccessControllerAllUpdatablePropertiesEquals(
      expectedAccessController,
      getPersistedAccessController(expectedAccessController)
    );
  }
}
