package com.yuzhi.ainms.core.web.rest;

import static com.yuzhi.ainms.core.domain.NCEAPStatusAsserts.*;
import static com.yuzhi.ainms.core.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuzhi.ainms.core.IntegrationTest;
import com.yuzhi.ainms.core.domain.NCEAPStatus;
import com.yuzhi.ainms.core.repository.NCEAPStatusRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link NCEAPStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NCEAPStatusResourceIT {

  private static final String DEFAULT_AP_SN = "AAAAAAAAAA";
  private static final String UPDATED_AP_SN = "BBBBBBBBBB";

  private static final Integer DEFAULT_AP_STATUS = 1;
  private static final Integer UPDATED_AP_STATUS = 2;

  private static final String ENTITY_API_URL = "/api/nceap-statuses";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicInteger intCount = new AtomicInteger(
    random.nextInt() + (2 * Short.MAX_VALUE)
  );

  @Autowired
  private ObjectMapper om;

  @Autowired
  private NCEAPStatusRepository nCEAPStatusRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restNCEAPStatusMockMvc;

  private NCEAPStatus nCEAPStatus;

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static NCEAPStatus createEntity(EntityManager em) {
    NCEAPStatus nCEAPStatus = new NCEAPStatus()
      .apSn(DEFAULT_AP_SN)
      .apStatus(DEFAULT_AP_STATUS);
    return nCEAPStatus;
  }

  /**
   * Create an updated entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static NCEAPStatus createUpdatedEntity(EntityManager em) {
    NCEAPStatus nCEAPStatus = new NCEAPStatus()
      .apSn(UPDATED_AP_SN)
      .apStatus(UPDATED_AP_STATUS);
    return nCEAPStatus;
  }

  @BeforeEach
  public void initTest() {
    nCEAPStatus = createEntity(em);
  }

  @Test
  @Transactional
  void createNCEAPStatus() throws Exception {
    long databaseSizeBeforeCreate = getRepositoryCount();
    // Create the NCEAPStatus
    var returnedNCEAPStatus = om.readValue(
      restNCEAPStatusMockMvc
        .perform(
          post(ENTITY_API_URL)
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsBytes(nCEAPStatus))
        )
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString(),
      NCEAPStatus.class
    );

    // Validate the NCEAPStatus in the database
    assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
    assertNCEAPStatusUpdatableFieldsEquals(
      returnedNCEAPStatus,
      getPersistedNCEAPStatus(returnedNCEAPStatus)
    );
  }

  @Test
  @Transactional
  void createNCEAPStatusWithExistingId() throws Exception {
    // Create the NCEAPStatus with an existing ID
    nCEAPStatus.setId(1);

    long databaseSizeBeforeCreate = getRepositoryCount();

    // An entity with an existing ID cannot be created, so this API call must fail
    restNCEAPStatusMockMvc
      .perform(
        post(ENTITY_API_URL)
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(nCEAPStatus))
      )
      .andExpect(status().isBadRequest());

    // Validate the NCEAPStatus in the database
    assertSameRepositoryCount(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void getAllNCEAPStatuses() throws Exception {
    // Initialize the database
    nCEAPStatusRepository.saveAndFlush(nCEAPStatus);

    // Get all the nCEAPStatusList
    restNCEAPStatusMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(
        jsonPath("$.[*].id").value(hasItem(nCEAPStatus.getId().intValue()))
      )
      .andExpect(jsonPath("$.[*].apSn").value(hasItem(DEFAULT_AP_SN)))
      .andExpect(jsonPath("$.[*].apStatus").value(hasItem(DEFAULT_AP_STATUS)));
  }

  @Test
  @Transactional
  void getNCEAPStatus() throws Exception {
    // Initialize the database
    nCEAPStatusRepository.saveAndFlush(nCEAPStatus);

    // Get the nCEAPStatus
    restNCEAPStatusMockMvc
      .perform(get(ENTITY_API_URL_ID, nCEAPStatus.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(nCEAPStatus.getId().intValue()))
      .andExpect(jsonPath("$.apSn").value(DEFAULT_AP_SN))
      .andExpect(jsonPath("$.apStatus").value(DEFAULT_AP_STATUS));
  }

  @Test
  @Transactional
  void getNonExistingNCEAPStatus() throws Exception {
    // Get the nCEAPStatus
    restNCEAPStatusMockMvc
      .perform(get(ENTITY_API_URL_ID, Integer.MAX_VALUE))
      .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putExistingNCEAPStatus() throws Exception {
    // Initialize the database
    nCEAPStatusRepository.saveAndFlush(nCEAPStatus);

    long databaseSizeBeforeUpdate = getRepositoryCount();

    // Update the nCEAPStatus
    NCEAPStatus updatedNCEAPStatus = nCEAPStatusRepository
      .findById(nCEAPStatus.getId())
      .orElseThrow();
    // Disconnect from session so that the updates on updatedNCEAPStatus are not directly saved in db
    em.detach(updatedNCEAPStatus);
    updatedNCEAPStatus.apSn(UPDATED_AP_SN).apStatus(UPDATED_AP_STATUS);

    restNCEAPStatusMockMvc
      .perform(
        put(ENTITY_API_URL_ID, updatedNCEAPStatus.getId())
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(updatedNCEAPStatus))
      )
      .andExpect(status().isOk());

    // Validate the NCEAPStatus in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
    assertPersistedNCEAPStatusToMatchAllProperties(updatedNCEAPStatus);
  }

  @Test
  @Transactional
  void putNonExistingNCEAPStatus() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    nCEAPStatus.setId(intCount.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restNCEAPStatusMockMvc
      .perform(
        put(ENTITY_API_URL_ID, nCEAPStatus.getId())
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(nCEAPStatus))
      )
      .andExpect(status().isBadRequest());

    // Validate the NCEAPStatus in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchNCEAPStatus() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    nCEAPStatus.setId(intCount.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restNCEAPStatusMockMvc
      .perform(
        put(ENTITY_API_URL_ID, intCount.incrementAndGet())
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(nCEAPStatus))
      )
      .andExpect(status().isBadRequest());

    // Validate the NCEAPStatus in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamNCEAPStatus() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    nCEAPStatus.setId(intCount.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restNCEAPStatusMockMvc
      .perform(
        put(ENTITY_API_URL)
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(nCEAPStatus))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the NCEAPStatus in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdateNCEAPStatusWithPatch() throws Exception {
    // Initialize the database
    nCEAPStatusRepository.saveAndFlush(nCEAPStatus);

    long databaseSizeBeforeUpdate = getRepositoryCount();

    // Update the nCEAPStatus using partial update
    NCEAPStatus partialUpdatedNCEAPStatus = new NCEAPStatus();
    partialUpdatedNCEAPStatus.setId(nCEAPStatus.getId());

    partialUpdatedNCEAPStatus.apSn(UPDATED_AP_SN);

    restNCEAPStatusMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedNCEAPStatus.getId())
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(partialUpdatedNCEAPStatus))
      )
      .andExpect(status().isOk());

    // Validate the NCEAPStatus in the database

    assertSameRepositoryCount(databaseSizeBeforeUpdate);
    assertNCEAPStatusUpdatableFieldsEquals(
      createUpdateProxyForBean(partialUpdatedNCEAPStatus, nCEAPStatus),
      getPersistedNCEAPStatus(nCEAPStatus)
    );
  }

  @Test
  @Transactional
  void fullUpdateNCEAPStatusWithPatch() throws Exception {
    // Initialize the database
    nCEAPStatusRepository.saveAndFlush(nCEAPStatus);

    long databaseSizeBeforeUpdate = getRepositoryCount();

    // Update the nCEAPStatus using partial update
    NCEAPStatus partialUpdatedNCEAPStatus = new NCEAPStatus();
    partialUpdatedNCEAPStatus.setId(nCEAPStatus.getId());

    partialUpdatedNCEAPStatus.apSn(UPDATED_AP_SN).apStatus(UPDATED_AP_STATUS);

    restNCEAPStatusMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedNCEAPStatus.getId())
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(partialUpdatedNCEAPStatus))
      )
      .andExpect(status().isOk());

    // Validate the NCEAPStatus in the database

    assertSameRepositoryCount(databaseSizeBeforeUpdate);
    assertNCEAPStatusUpdatableFieldsEquals(
      partialUpdatedNCEAPStatus,
      getPersistedNCEAPStatus(partialUpdatedNCEAPStatus)
    );
  }

  @Test
  @Transactional
  void patchNonExistingNCEAPStatus() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    nCEAPStatus.setId(intCount.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restNCEAPStatusMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, nCEAPStatus.getId())
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(nCEAPStatus))
      )
      .andExpect(status().isBadRequest());

    // Validate the NCEAPStatus in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchNCEAPStatus() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    nCEAPStatus.setId(intCount.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restNCEAPStatusMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, intCount.incrementAndGet())
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(nCEAPStatus))
      )
      .andExpect(status().isBadRequest());

    // Validate the NCEAPStatus in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamNCEAPStatus() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    nCEAPStatus.setId(intCount.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restNCEAPStatusMockMvc
      .perform(
        patch(ENTITY_API_URL)
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(nCEAPStatus))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the NCEAPStatus in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deleteNCEAPStatus() throws Exception {
    // Initialize the database
    nCEAPStatusRepository.saveAndFlush(nCEAPStatus);

    long databaseSizeBeforeDelete = getRepositoryCount();

    // Delete the nCEAPStatus
    restNCEAPStatusMockMvc
      .perform(
        delete(ENTITY_API_URL_ID, nCEAPStatus.getId())
          .with(csrf())
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
  }

  protected long getRepositoryCount() {
    return nCEAPStatusRepository.count();
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

  protected NCEAPStatus getPersistedNCEAPStatus(NCEAPStatus nCEAPStatus) {
    return nCEAPStatusRepository.findById(nCEAPStatus.getId()).orElseThrow();
  }

  protected void assertPersistedNCEAPStatusToMatchAllProperties(
    NCEAPStatus expectedNCEAPStatus
  ) {
    assertNCEAPStatusAllPropertiesEquals(
      expectedNCEAPStatus,
      getPersistedNCEAPStatus(expectedNCEAPStatus)
    );
  }

  protected void assertPersistedNCEAPStatusToMatchUpdatableProperties(
    NCEAPStatus expectedNCEAPStatus
  ) {
    assertNCEAPStatusAllUpdatablePropertiesEquals(
      expectedNCEAPStatus,
      getPersistedNCEAPStatus(expectedNCEAPStatus)
    );
  }
}
