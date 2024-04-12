package com.yuzhi.ainms.core.web.rest;

import static com.yuzhi.ainms.core.domain.CountryStisticsAsserts.*;
import static com.yuzhi.ainms.core.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuzhi.ainms.core.IntegrationTest;
import com.yuzhi.ainms.core.domain.CountryStistics;
import com.yuzhi.ainms.core.repository.CountryStisticsRepository;
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
 * Integration tests for the {@link CountryStisticsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CountryStisticsResourceIT {

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

  private static final String ENTITY_API_URL = "/api/country-stistics";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong longCount = new AtomicLong(
    random.nextInt() + (2 * Integer.MAX_VALUE)
  );

  @Autowired
  private ObjectMapper om;

  @Autowired
  private CountryStisticsRepository countryStisticsRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restCountryStisticsMockMvc;

  private CountryStistics countryStistics;

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static CountryStistics createEntity(EntityManager em) {
    CountryStistics countryStistics = new CountryStistics()
      .name(DEFAULT_NAME)
      .totalCount(DEFAULT_TOTAL_COUNT)
      .onlineCount(DEFAULT_ONLINE_COUNT)
      .offlineCount(DEFAULT_OFFLINE_COUNT)
      .otherCount(DEFAULT_OTHER_COUNT)
      .statisticDate(DEFAULT_STATISTIC_DATE)
      .statisticTime(DEFAULT_STATISTIC_TIME);
    return countryStistics;
  }

  /**
   * Create an updated entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static CountryStistics createUpdatedEntity(EntityManager em) {
    CountryStistics countryStistics = new CountryStistics()
      .name(UPDATED_NAME)
      .totalCount(UPDATED_TOTAL_COUNT)
      .onlineCount(UPDATED_ONLINE_COUNT)
      .offlineCount(UPDATED_OFFLINE_COUNT)
      .otherCount(UPDATED_OTHER_COUNT)
      .statisticDate(UPDATED_STATISTIC_DATE)
      .statisticTime(UPDATED_STATISTIC_TIME);
    return countryStistics;
  }

  @BeforeEach
  public void initTest() {
    countryStistics = createEntity(em);
  }

  @Test
  @Transactional
  void createCountryStistics() throws Exception {
    long databaseSizeBeforeCreate = getRepositoryCount();
    // Create the CountryStistics
    var returnedCountryStistics = om.readValue(
      restCountryStisticsMockMvc
        .perform(
          post(ENTITY_API_URL)
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsBytes(countryStistics))
        )
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString(),
      CountryStistics.class
    );

    // Validate the CountryStistics in the database
    assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
    assertCountryStisticsUpdatableFieldsEquals(
      returnedCountryStistics,
      getPersistedCountryStistics(returnedCountryStistics)
    );
  }

  @Test
  @Transactional
  void createCountryStisticsWithExistingId() throws Exception {
    // Create the CountryStistics with an existing ID
    countryStistics.setId(1L);

    long databaseSizeBeforeCreate = getRepositoryCount();

    // An entity with an existing ID cannot be created, so this API call must fail
    restCountryStisticsMockMvc
      .perform(
        post(ENTITY_API_URL)
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(countryStistics))
      )
      .andExpect(status().isBadRequest());

    // Validate the CountryStistics in the database
    assertSameRepositoryCount(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void checkNameIsRequired() throws Exception {
    long databaseSizeBeforeTest = getRepositoryCount();
    // set the field null
    countryStistics.setName(null);

    // Create the CountryStistics, which fails.

    restCountryStisticsMockMvc
      .perform(
        post(ENTITY_API_URL)
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(countryStistics))
      )
      .andExpect(status().isBadRequest());

    assertSameRepositoryCount(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void getAllCountryStistics() throws Exception {
    // Initialize the database
    countryStisticsRepository.saveAndFlush(countryStistics);

    // Get all the countryStisticsList
    restCountryStisticsMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(
        jsonPath("$.[*].id").value(hasItem(countryStistics.getId().intValue()))
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
  void getCountryStistics() throws Exception {
    // Initialize the database
    countryStisticsRepository.saveAndFlush(countryStistics);

    // Get the countryStistics
    restCountryStisticsMockMvc
      .perform(get(ENTITY_API_URL_ID, countryStistics.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(countryStistics.getId().intValue()))
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
  void getNonExistingCountryStistics() throws Exception {
    // Get the countryStistics
    restCountryStisticsMockMvc
      .perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
      .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putExistingCountryStistics() throws Exception {
    // Initialize the database
    countryStisticsRepository.saveAndFlush(countryStistics);

    long databaseSizeBeforeUpdate = getRepositoryCount();

    // Update the countryStistics
    CountryStistics updatedCountryStistics = countryStisticsRepository
      .findById(countryStistics.getId())
      .orElseThrow();
    // Disconnect from session so that the updates on updatedCountryStistics are not directly saved in db
    em.detach(updatedCountryStistics);
    updatedCountryStistics
      .name(UPDATED_NAME)
      .totalCount(UPDATED_TOTAL_COUNT)
      .onlineCount(UPDATED_ONLINE_COUNT)
      .offlineCount(UPDATED_OFFLINE_COUNT)
      .otherCount(UPDATED_OTHER_COUNT)
      .statisticDate(UPDATED_STATISTIC_DATE)
      .statisticTime(UPDATED_STATISTIC_TIME);

    restCountryStisticsMockMvc
      .perform(
        put(ENTITY_API_URL_ID, updatedCountryStistics.getId())
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(updatedCountryStistics))
      )
      .andExpect(status().isOk());

    // Validate the CountryStistics in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
    assertPersistedCountryStisticsToMatchAllProperties(updatedCountryStistics);
  }

  @Test
  @Transactional
  void putNonExistingCountryStistics() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    countryStistics.setId(longCount.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restCountryStisticsMockMvc
      .perform(
        put(ENTITY_API_URL_ID, countryStistics.getId())
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(countryStistics))
      )
      .andExpect(status().isBadRequest());

    // Validate the CountryStistics in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchCountryStistics() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    countryStistics.setId(longCount.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restCountryStisticsMockMvc
      .perform(
        put(ENTITY_API_URL_ID, longCount.incrementAndGet())
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(countryStistics))
      )
      .andExpect(status().isBadRequest());

    // Validate the CountryStistics in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamCountryStistics() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    countryStistics.setId(longCount.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restCountryStisticsMockMvc
      .perform(
        put(ENTITY_API_URL)
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(countryStistics))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the CountryStistics in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdateCountryStisticsWithPatch() throws Exception {
    // Initialize the database
    countryStisticsRepository.saveAndFlush(countryStistics);

    long databaseSizeBeforeUpdate = getRepositoryCount();

    // Update the countryStistics using partial update
    CountryStistics partialUpdatedCountryStistics = new CountryStistics();
    partialUpdatedCountryStistics.setId(countryStistics.getId());

    partialUpdatedCountryStistics
      .totalCount(UPDATED_TOTAL_COUNT)
      .offlineCount(UPDATED_OFFLINE_COUNT)
      .otherCount(UPDATED_OTHER_COUNT)
      .statisticTime(UPDATED_STATISTIC_TIME);

    restCountryStisticsMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedCountryStistics.getId())
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(partialUpdatedCountryStistics))
      )
      .andExpect(status().isOk());

    // Validate the CountryStistics in the database

    assertSameRepositoryCount(databaseSizeBeforeUpdate);
    assertCountryStisticsUpdatableFieldsEquals(
      createUpdateProxyForBean(partialUpdatedCountryStistics, countryStistics),
      getPersistedCountryStistics(countryStistics)
    );
  }

  @Test
  @Transactional
  void fullUpdateCountryStisticsWithPatch() throws Exception {
    // Initialize the database
    countryStisticsRepository.saveAndFlush(countryStistics);

    long databaseSizeBeforeUpdate = getRepositoryCount();

    // Update the countryStistics using partial update
    CountryStistics partialUpdatedCountryStistics = new CountryStistics();
    partialUpdatedCountryStistics.setId(countryStistics.getId());

    partialUpdatedCountryStistics
      .name(UPDATED_NAME)
      .totalCount(UPDATED_TOTAL_COUNT)
      .onlineCount(UPDATED_ONLINE_COUNT)
      .offlineCount(UPDATED_OFFLINE_COUNT)
      .otherCount(UPDATED_OTHER_COUNT)
      .statisticDate(UPDATED_STATISTIC_DATE)
      .statisticTime(UPDATED_STATISTIC_TIME);

    restCountryStisticsMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedCountryStistics.getId())
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(partialUpdatedCountryStistics))
      )
      .andExpect(status().isOk());

    // Validate the CountryStistics in the database

    assertSameRepositoryCount(databaseSizeBeforeUpdate);
    assertCountryStisticsUpdatableFieldsEquals(
      partialUpdatedCountryStistics,
      getPersistedCountryStistics(partialUpdatedCountryStistics)
    );
  }

  @Test
  @Transactional
  void patchNonExistingCountryStistics() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    countryStistics.setId(longCount.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restCountryStisticsMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, countryStistics.getId())
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(countryStistics))
      )
      .andExpect(status().isBadRequest());

    // Validate the CountryStistics in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchCountryStistics() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    countryStistics.setId(longCount.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restCountryStisticsMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(countryStistics))
      )
      .andExpect(status().isBadRequest());

    // Validate the CountryStistics in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamCountryStistics() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    countryStistics.setId(longCount.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restCountryStisticsMockMvc
      .perform(
        patch(ENTITY_API_URL)
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(countryStistics))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the CountryStistics in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deleteCountryStistics() throws Exception {
    // Initialize the database
    countryStisticsRepository.saveAndFlush(countryStistics);

    long databaseSizeBeforeDelete = getRepositoryCount();

    // Delete the countryStistics
    restCountryStisticsMockMvc
      .perform(
        delete(ENTITY_API_URL_ID, countryStistics.getId())
          .with(csrf())
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
  }

  protected long getRepositoryCount() {
    return countryStisticsRepository.count();
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

  protected CountryStistics getPersistedCountryStistics(
    CountryStistics countryStistics
  ) {
    return countryStisticsRepository
      .findById(countryStistics.getId())
      .orElseThrow();
  }

  protected void assertPersistedCountryStisticsToMatchAllProperties(
    CountryStistics expectedCountryStistics
  ) {
    assertCountryStisticsAllPropertiesEquals(
      expectedCountryStistics,
      getPersistedCountryStistics(expectedCountryStistics)
    );
  }

  protected void assertPersistedCountryStisticsToMatchUpdatableProperties(
    CountryStistics expectedCountryStistics
  ) {
    assertCountryStisticsAllUpdatablePropertiesEquals(
      expectedCountryStistics,
      getPersistedCountryStistics(expectedCountryStistics)
    );
  }
}
