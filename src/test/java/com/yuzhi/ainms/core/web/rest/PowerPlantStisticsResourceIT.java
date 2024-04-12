package com.yuzhi.ainms.core.web.rest;

import static com.yuzhi.ainms.core.domain.PowerPlantStisticsAsserts.*;
import static com.yuzhi.ainms.core.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuzhi.ainms.core.IntegrationTest;
import com.yuzhi.ainms.core.domain.PowerPlantStistics;
import com.yuzhi.ainms.core.repository.PowerPlantStisticsRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link PowerPlantStisticsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PowerPlantStisticsResourceIT {

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String UPDATED_NAME = "BBBBBBBBBB";

  private static final Long DEFAULT_TOTAL_COUNT = 1L;
  private static final Long UPDATED_TOTAL_COUNT = 2L;

  private static final Long DEFAULT_ONLINE_COUNT = 1L;
  private static final Long UPDATED_ONLINE_COUNT = 2L;

  private static final Long DEFAULT_OFFLINE_COUNT = 1L;
  private static final Long UPDATED_OFFLINE_COUNT = 2L;

  private static final Long DEFAULT_OTHER_COUNT = 1L;
  private static final Long UPDATED_OTHER_COUNT = 2L;

  private static final LocalDate DEFAULT_STATISTIC_DATE = LocalDate.ofEpochDay(
    0L
  );
  private static final LocalDate UPDATED_STATISTIC_DATE = LocalDate.now(
    ZoneId.systemDefault()
  );

  private static final Instant DEFAULT_STATISTIC_TIME = Instant.ofEpochMilli(
    0L
  );
  private static final Instant UPDATED_STATISTIC_TIME = Instant.now()
    .truncatedTo(ChronoUnit.MILLIS);

  private static final String ENTITY_API_URL = "/api/power-plant-stistics";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong longCount = new AtomicLong(
    random.nextInt() + (2 * Integer.MAX_VALUE)
  );

  @Autowired
  private ObjectMapper om;

  @Autowired
  private PowerPlantStisticsRepository powerPlantStisticsRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restPowerPlantStisticsMockMvc;

  private PowerPlantStistics powerPlantStistics;

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static PowerPlantStistics createEntity(EntityManager em) {
    PowerPlantStistics powerPlantStistics = new PowerPlantStistics()
      .name(DEFAULT_NAME)
      .totalCount(DEFAULT_TOTAL_COUNT)
      .onlineCount(DEFAULT_ONLINE_COUNT)
      .offlineCount(DEFAULT_OFFLINE_COUNT)
      .otherCount(DEFAULT_OTHER_COUNT)
      .statisticDate(DEFAULT_STATISTIC_DATE)
      .statisticTime(DEFAULT_STATISTIC_TIME);
    return powerPlantStistics;
  }

  /**
   * Create an updated entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static PowerPlantStistics createUpdatedEntity(EntityManager em) {
    PowerPlantStistics powerPlantStistics = new PowerPlantStistics()
      .name(UPDATED_NAME)
      .totalCount(UPDATED_TOTAL_COUNT)
      .onlineCount(UPDATED_ONLINE_COUNT)
      .offlineCount(UPDATED_OFFLINE_COUNT)
      .otherCount(UPDATED_OTHER_COUNT)
      .statisticDate(UPDATED_STATISTIC_DATE)
      .statisticTime(UPDATED_STATISTIC_TIME);
    return powerPlantStistics;
  }

  @BeforeEach
  public void initTest() {
    powerPlantStistics = createEntity(em);
  }

  @Test
  @Transactional
  void createPowerPlantStistics() throws Exception {
    long databaseSizeBeforeCreate = getRepositoryCount();
    // Create the PowerPlantStistics
    var returnedPowerPlantStistics = om.readValue(
      restPowerPlantStisticsMockMvc
        .perform(
          post(ENTITY_API_URL)
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsBytes(powerPlantStistics))
        )
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString(),
      PowerPlantStistics.class
    );

    // Validate the PowerPlantStistics in the database
    assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
    assertPowerPlantStisticsUpdatableFieldsEquals(
      returnedPowerPlantStistics,
      getPersistedPowerPlantStistics(returnedPowerPlantStistics)
    );
  }

  @Test
  @Transactional
  void createPowerPlantStisticsWithExistingId() throws Exception {
    // Create the PowerPlantStistics with an existing ID
    powerPlantStistics.setId(1L);

    long databaseSizeBeforeCreate = getRepositoryCount();

    // An entity with an existing ID cannot be created, so this API call must fail
    restPowerPlantStisticsMockMvc
      .perform(
        post(ENTITY_API_URL)
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(powerPlantStistics))
      )
      .andExpect(status().isBadRequest());

    // Validate the PowerPlantStistics in the database
    assertSameRepositoryCount(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void checkNameIsRequired() throws Exception {
    long databaseSizeBeforeTest = getRepositoryCount();
    // set the field null
    powerPlantStistics.setName(null);

    // Create the PowerPlantStistics, which fails.

    restPowerPlantStisticsMockMvc
      .perform(
        post(ENTITY_API_URL)
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(powerPlantStistics))
      )
      .andExpect(status().isBadRequest());

    assertSameRepositoryCount(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void getAllPowerPlantStistics() throws Exception {
    // Initialize the database
    powerPlantStisticsRepository.saveAndFlush(powerPlantStistics);

    // Get all the powerPlantStisticsList
    restPowerPlantStisticsMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(
        jsonPath("$.[*].id").value(
          hasItem(powerPlantStistics.getId().intValue())
        )
      )
      .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
      .andExpect(
        jsonPath("$.[*].totalCount").value(
          hasItem(DEFAULT_TOTAL_COUNT.intValue())
        )
      )
      .andExpect(
        jsonPath("$.[*].onlineCount").value(
          hasItem(DEFAULT_ONLINE_COUNT.intValue())
        )
      )
      .andExpect(
        jsonPath("$.[*].offlineCount").value(
          hasItem(DEFAULT_OFFLINE_COUNT.intValue())
        )
      )
      .andExpect(
        jsonPath("$.[*].otherCount").value(
          hasItem(DEFAULT_OTHER_COUNT.intValue())
        )
      )
      .andExpect(
        jsonPath("$.[*].statisticDate").value(
          hasItem(DEFAULT_STATISTIC_DATE.toString())
        )
      )
      .andExpect(
        jsonPath("$.[*].statisticTime").value(
          hasItem(DEFAULT_STATISTIC_TIME.toString())
        )
      );
  }

  @Test
  @Transactional
  void getPowerPlantStistics() throws Exception {
    // Initialize the database
    powerPlantStisticsRepository.saveAndFlush(powerPlantStistics);

    // Get the powerPlantStistics
    restPowerPlantStisticsMockMvc
      .perform(get(ENTITY_API_URL_ID, powerPlantStistics.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(powerPlantStistics.getId().intValue()))
      .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
      .andExpect(jsonPath("$.totalCount").value(DEFAULT_TOTAL_COUNT.intValue()))
      .andExpect(
        jsonPath("$.onlineCount").value(DEFAULT_ONLINE_COUNT.intValue())
      )
      .andExpect(
        jsonPath("$.offlineCount").value(DEFAULT_OFFLINE_COUNT.intValue())
      )
      .andExpect(jsonPath("$.otherCount").value(DEFAULT_OTHER_COUNT.intValue()))
      .andExpect(
        jsonPath("$.statisticDate").value(DEFAULT_STATISTIC_DATE.toString())
      )
      .andExpect(
        jsonPath("$.statisticTime").value(DEFAULT_STATISTIC_TIME.toString())
      );
  }

  @Test
  @Transactional
  void getNonExistingPowerPlantStistics() throws Exception {
    // Get the powerPlantStistics
    restPowerPlantStisticsMockMvc
      .perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
      .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putExistingPowerPlantStistics() throws Exception {
    // Initialize the database
    powerPlantStisticsRepository.saveAndFlush(powerPlantStistics);

    long databaseSizeBeforeUpdate = getRepositoryCount();

    // Update the powerPlantStistics
    PowerPlantStistics updatedPowerPlantStistics = powerPlantStisticsRepository
      .findById(powerPlantStistics.getId())
      .orElseThrow();
    // Disconnect from session so that the updates on updatedPowerPlantStistics are not directly saved in db
    em.detach(updatedPowerPlantStistics);
    updatedPowerPlantStistics
      .name(UPDATED_NAME)
      .totalCount(UPDATED_TOTAL_COUNT)
      .onlineCount(UPDATED_ONLINE_COUNT)
      .offlineCount(UPDATED_OFFLINE_COUNT)
      .otherCount(UPDATED_OTHER_COUNT)
      .statisticDate(UPDATED_STATISTIC_DATE)
      .statisticTime(UPDATED_STATISTIC_TIME);

    restPowerPlantStisticsMockMvc
      .perform(
        put(ENTITY_API_URL_ID, updatedPowerPlantStistics.getId())
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(updatedPowerPlantStistics))
      )
      .andExpect(status().isOk());

    // Validate the PowerPlantStistics in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
    assertPersistedPowerPlantStisticsToMatchAllProperties(
      updatedPowerPlantStistics
    );
  }

  @Test
  @Transactional
  void putNonExistingPowerPlantStistics() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    powerPlantStistics.setId(longCount.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restPowerPlantStisticsMockMvc
      .perform(
        put(ENTITY_API_URL_ID, powerPlantStistics.getId())
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(powerPlantStistics))
      )
      .andExpect(status().isBadRequest());

    // Validate the PowerPlantStistics in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchPowerPlantStistics() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    powerPlantStistics.setId(longCount.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restPowerPlantStisticsMockMvc
      .perform(
        put(ENTITY_API_URL_ID, longCount.incrementAndGet())
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(powerPlantStistics))
      )
      .andExpect(status().isBadRequest());

    // Validate the PowerPlantStistics in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamPowerPlantStistics() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    powerPlantStistics.setId(longCount.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restPowerPlantStisticsMockMvc
      .perform(
        put(ENTITY_API_URL)
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(powerPlantStistics))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the PowerPlantStistics in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdatePowerPlantStisticsWithPatch() throws Exception {
    // Initialize the database
    powerPlantStisticsRepository.saveAndFlush(powerPlantStistics);

    long databaseSizeBeforeUpdate = getRepositoryCount();

    // Update the powerPlantStistics using partial update
    PowerPlantStistics partialUpdatedPowerPlantStistics =
      new PowerPlantStistics();
    partialUpdatedPowerPlantStistics.setId(powerPlantStistics.getId());

    partialUpdatedPowerPlantStistics
      .offlineCount(UPDATED_OFFLINE_COUNT)
      .otherCount(UPDATED_OTHER_COUNT);

    restPowerPlantStisticsMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedPowerPlantStistics.getId())
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(partialUpdatedPowerPlantStistics))
      )
      .andExpect(status().isOk());

    // Validate the PowerPlantStistics in the database

    assertSameRepositoryCount(databaseSizeBeforeUpdate);
    assertPowerPlantStisticsUpdatableFieldsEquals(
      createUpdateProxyForBean(
        partialUpdatedPowerPlantStistics,
        powerPlantStistics
      ),
      getPersistedPowerPlantStistics(powerPlantStistics)
    );
  }

  @Test
  @Transactional
  void fullUpdatePowerPlantStisticsWithPatch() throws Exception {
    // Initialize the database
    powerPlantStisticsRepository.saveAndFlush(powerPlantStistics);

    long databaseSizeBeforeUpdate = getRepositoryCount();

    // Update the powerPlantStistics using partial update
    PowerPlantStistics partialUpdatedPowerPlantStistics =
      new PowerPlantStistics();
    partialUpdatedPowerPlantStistics.setId(powerPlantStistics.getId());

    partialUpdatedPowerPlantStistics
      .name(UPDATED_NAME)
      .totalCount(UPDATED_TOTAL_COUNT)
      .onlineCount(UPDATED_ONLINE_COUNT)
      .offlineCount(UPDATED_OFFLINE_COUNT)
      .otherCount(UPDATED_OTHER_COUNT)
      .statisticDate(UPDATED_STATISTIC_DATE)
      .statisticTime(UPDATED_STATISTIC_TIME);

    restPowerPlantStisticsMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedPowerPlantStistics.getId())
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(partialUpdatedPowerPlantStistics))
      )
      .andExpect(status().isOk());

    // Validate the PowerPlantStistics in the database

    assertSameRepositoryCount(databaseSizeBeforeUpdate);
    assertPowerPlantStisticsUpdatableFieldsEquals(
      partialUpdatedPowerPlantStistics,
      getPersistedPowerPlantStistics(partialUpdatedPowerPlantStistics)
    );
  }

  @Test
  @Transactional
  void patchNonExistingPowerPlantStistics() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    powerPlantStistics.setId(longCount.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restPowerPlantStisticsMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, powerPlantStistics.getId())
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(powerPlantStistics))
      )
      .andExpect(status().isBadRequest());

    // Validate the PowerPlantStistics in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchPowerPlantStistics() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    powerPlantStistics.setId(longCount.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restPowerPlantStisticsMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(powerPlantStistics))
      )
      .andExpect(status().isBadRequest());

    // Validate the PowerPlantStistics in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamPowerPlantStistics() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    powerPlantStistics.setId(longCount.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restPowerPlantStisticsMockMvc
      .perform(
        patch(ENTITY_API_URL)
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(powerPlantStistics))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the PowerPlantStistics in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deletePowerPlantStistics() throws Exception {
    // Initialize the database
    powerPlantStisticsRepository.saveAndFlush(powerPlantStistics);

    long databaseSizeBeforeDelete = getRepositoryCount();

    // Delete the powerPlantStistics
    restPowerPlantStisticsMockMvc
      .perform(
        delete(ENTITY_API_URL_ID, powerPlantStistics.getId())
          .with(csrf())
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
  }

  protected long getRepositoryCount() {
    return powerPlantStisticsRepository.count();
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

  protected PowerPlantStistics getPersistedPowerPlantStistics(
    PowerPlantStistics powerPlantStistics
  ) {
    return powerPlantStisticsRepository
      .findById(powerPlantStistics.getId())
      .orElseThrow();
  }

  protected void assertPersistedPowerPlantStisticsToMatchAllProperties(
    PowerPlantStistics expectedPowerPlantStistics
  ) {
    assertPowerPlantStisticsAllPropertiesEquals(
      expectedPowerPlantStistics,
      getPersistedPowerPlantStistics(expectedPowerPlantStistics)
    );
  }

  protected void assertPersistedPowerPlantStisticsToMatchUpdatableProperties(
    PowerPlantStistics expectedPowerPlantStistics
  ) {
    assertPowerPlantStisticsAllUpdatablePropertiesEquals(
      expectedPowerPlantStistics,
      getPersistedPowerPlantStistics(expectedPowerPlantStistics)
    );
  }
}
