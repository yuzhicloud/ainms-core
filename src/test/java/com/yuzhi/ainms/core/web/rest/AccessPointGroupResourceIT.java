package com.yuzhi.ainms.core.web.rest;

import static com.yuzhi.ainms.core.domain.AccessPointGroupAsserts.*;
import static com.yuzhi.ainms.core.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuzhi.ainms.core.IntegrationTest;
import com.yuzhi.ainms.core.domain.AccessPointGroup;
import com.yuzhi.ainms.core.repository.AccessPointGroupRepository;
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
 * Integration tests for the {@link AccessPointGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccessPointGroupResourceIT {

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String UPDATED_NAME = "BBBBBBBBBB";

  private static final String ENTITY_API_URL = "/api/access-point-groups";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong longCount = new AtomicLong(
    random.nextInt() + (2 * Integer.MAX_VALUE)
  );

  @Autowired
  private ObjectMapper om;

  @Autowired
  private AccessPointGroupRepository accessPointGroupRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restAccessPointGroupMockMvc;

  private AccessPointGroup accessPointGroup;

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static AccessPointGroup createEntity(EntityManager em) {
    AccessPointGroup accessPointGroup = new AccessPointGroup()
      .name(DEFAULT_NAME);
    return accessPointGroup;
  }

  /**
   * Create an updated entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static AccessPointGroup createUpdatedEntity(EntityManager em) {
    AccessPointGroup accessPointGroup = new AccessPointGroup()
      .name(UPDATED_NAME);
    return accessPointGroup;
  }

  @BeforeEach
  public void initTest() {
    accessPointGroup = createEntity(em);
  }

  @Test
  @Transactional
  void createAccessPointGroup() throws Exception {
    long databaseSizeBeforeCreate = getRepositoryCount();
    // Create the AccessPointGroup
    var returnedAccessPointGroup = om.readValue(
      restAccessPointGroupMockMvc
        .perform(
          post(ENTITY_API_URL)
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsBytes(accessPointGroup))
        )
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString(),
      AccessPointGroup.class
    );

    // Validate the AccessPointGroup in the database
    assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
    assertAccessPointGroupUpdatableFieldsEquals(
      returnedAccessPointGroup,
      getPersistedAccessPointGroup(returnedAccessPointGroup)
    );
  }

  @Test
  @Transactional
  void createAccessPointGroupWithExistingId() throws Exception {
    // Create the AccessPointGroup with an existing ID
    accessPointGroup.setId(1L);

    long databaseSizeBeforeCreate = getRepositoryCount();

    // An entity with an existing ID cannot be created, so this API call must fail
    restAccessPointGroupMockMvc
      .perform(
        post(ENTITY_API_URL)
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(accessPointGroup))
      )
      .andExpect(status().isBadRequest());

    // Validate the AccessPointGroup in the database
    assertSameRepositoryCount(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void checkNameIsRequired() throws Exception {
    long databaseSizeBeforeTest = getRepositoryCount();
    // set the field null
    accessPointGroup.setName(null);

    // Create the AccessPointGroup, which fails.

    restAccessPointGroupMockMvc
      .perform(
        post(ENTITY_API_URL)
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(accessPointGroup))
      )
      .andExpect(status().isBadRequest());

    assertSameRepositoryCount(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void getAllAccessPointGroups() throws Exception {
    // Initialize the database
    accessPointGroupRepository.saveAndFlush(accessPointGroup);

    // Get all the accessPointGroupList
    restAccessPointGroupMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(
        jsonPath("$.[*].id").value(hasItem(accessPointGroup.getId().intValue()))
      )
      .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
  }

  @Test
  @Transactional
  void getAccessPointGroup() throws Exception {
    // Initialize the database
    accessPointGroupRepository.saveAndFlush(accessPointGroup);

    // Get the accessPointGroup
    restAccessPointGroupMockMvc
      .perform(get(ENTITY_API_URL_ID, accessPointGroup.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(accessPointGroup.getId().intValue()))
      .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
  }

  @Test
  @Transactional
  void getNonExistingAccessPointGroup() throws Exception {
    // Get the accessPointGroup
    restAccessPointGroupMockMvc
      .perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
      .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putExistingAccessPointGroup() throws Exception {
    // Initialize the database
    accessPointGroupRepository.saveAndFlush(accessPointGroup);

    long databaseSizeBeforeUpdate = getRepositoryCount();

    // Update the accessPointGroup
    AccessPointGroup updatedAccessPointGroup = accessPointGroupRepository
      .findById(accessPointGroup.getId())
      .orElseThrow();
    // Disconnect from session so that the updates on updatedAccessPointGroup are not directly saved in db
    em.detach(updatedAccessPointGroup);
    updatedAccessPointGroup.name(UPDATED_NAME);

    restAccessPointGroupMockMvc
      .perform(
        put(ENTITY_API_URL_ID, updatedAccessPointGroup.getId())
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(updatedAccessPointGroup))
      )
      .andExpect(status().isOk());

    // Validate the AccessPointGroup in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
    assertPersistedAccessPointGroupToMatchAllProperties(
      updatedAccessPointGroup
    );
  }

  @Test
  @Transactional
  void putNonExistingAccessPointGroup() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    accessPointGroup.setId(longCount.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restAccessPointGroupMockMvc
      .perform(
        put(ENTITY_API_URL_ID, accessPointGroup.getId())
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(accessPointGroup))
      )
      .andExpect(status().isBadRequest());

    // Validate the AccessPointGroup in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchAccessPointGroup() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    accessPointGroup.setId(longCount.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restAccessPointGroupMockMvc
      .perform(
        put(ENTITY_API_URL_ID, longCount.incrementAndGet())
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(accessPointGroup))
      )
      .andExpect(status().isBadRequest());

    // Validate the AccessPointGroup in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamAccessPointGroup() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    accessPointGroup.setId(longCount.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restAccessPointGroupMockMvc
      .perform(
        put(ENTITY_API_URL)
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(accessPointGroup))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the AccessPointGroup in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdateAccessPointGroupWithPatch() throws Exception {
    // Initialize the database
    accessPointGroupRepository.saveAndFlush(accessPointGroup);

    long databaseSizeBeforeUpdate = getRepositoryCount();

    // Update the accessPointGroup using partial update
    AccessPointGroup partialUpdatedAccessPointGroup = new AccessPointGroup();
    partialUpdatedAccessPointGroup.setId(accessPointGroup.getId());

    restAccessPointGroupMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedAccessPointGroup.getId())
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(partialUpdatedAccessPointGroup))
      )
      .andExpect(status().isOk());

    // Validate the AccessPointGroup in the database

    assertSameRepositoryCount(databaseSizeBeforeUpdate);
    assertAccessPointGroupUpdatableFieldsEquals(
      createUpdateProxyForBean(
        partialUpdatedAccessPointGroup,
        accessPointGroup
      ),
      getPersistedAccessPointGroup(accessPointGroup)
    );
  }

  @Test
  @Transactional
  void fullUpdateAccessPointGroupWithPatch() throws Exception {
    // Initialize the database
    accessPointGroupRepository.saveAndFlush(accessPointGroup);

    long databaseSizeBeforeUpdate = getRepositoryCount();

    // Update the accessPointGroup using partial update
    AccessPointGroup partialUpdatedAccessPointGroup = new AccessPointGroup();
    partialUpdatedAccessPointGroup.setId(accessPointGroup.getId());

    partialUpdatedAccessPointGroup.name(UPDATED_NAME);

    restAccessPointGroupMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedAccessPointGroup.getId())
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(partialUpdatedAccessPointGroup))
      )
      .andExpect(status().isOk());

    // Validate the AccessPointGroup in the database

    assertSameRepositoryCount(databaseSizeBeforeUpdate);
    assertAccessPointGroupUpdatableFieldsEquals(
      partialUpdatedAccessPointGroup,
      getPersistedAccessPointGroup(partialUpdatedAccessPointGroup)
    );
  }

  @Test
  @Transactional
  void patchNonExistingAccessPointGroup() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    accessPointGroup.setId(longCount.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restAccessPointGroupMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, accessPointGroup.getId())
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(accessPointGroup))
      )
      .andExpect(status().isBadRequest());

    // Validate the AccessPointGroup in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchAccessPointGroup() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    accessPointGroup.setId(longCount.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restAccessPointGroupMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(accessPointGroup))
      )
      .andExpect(status().isBadRequest());

    // Validate the AccessPointGroup in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamAccessPointGroup() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    accessPointGroup.setId(longCount.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restAccessPointGroupMockMvc
      .perform(
        patch(ENTITY_API_URL)
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(accessPointGroup))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the AccessPointGroup in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deleteAccessPointGroup() throws Exception {
    // Initialize the database
    accessPointGroupRepository.saveAndFlush(accessPointGroup);

    long databaseSizeBeforeDelete = getRepositoryCount();

    // Delete the accessPointGroup
    restAccessPointGroupMockMvc
      .perform(
        delete(ENTITY_API_URL_ID, accessPointGroup.getId())
          .with(csrf())
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
  }

  protected long getRepositoryCount() {
    return accessPointGroupRepository.count();
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

  protected AccessPointGroup getPersistedAccessPointGroup(
    AccessPointGroup accessPointGroup
  ) {
    return accessPointGroupRepository
      .findById(accessPointGroup.getId())
      .orElseThrow();
  }

  protected void assertPersistedAccessPointGroupToMatchAllProperties(
    AccessPointGroup expectedAccessPointGroup
  ) {
    assertAccessPointGroupAllPropertiesEquals(
      expectedAccessPointGroup,
      getPersistedAccessPointGroup(expectedAccessPointGroup)
    );
  }

  protected void assertPersistedAccessPointGroupToMatchUpdatableProperties(
    AccessPointGroup expectedAccessPointGroup
  ) {
    assertAccessPointGroupAllUpdatablePropertiesEquals(
      expectedAccessPointGroup,
      getPersistedAccessPointGroup(expectedAccessPointGroup)
    );
  }
}
