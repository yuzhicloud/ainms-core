package com.yuzhi.ainms.core.web.rest;

import static com.yuzhi.ainms.core.domain.ProvinceStisticsAsserts.*;
import static com.yuzhi.ainms.core.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuzhi.ainms.core.IntegrationTest;
import com.yuzhi.ainms.core.domain.ProvinceStistics;
import com.yuzhi.ainms.core.repository.ProvinceStisticsRepository;
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
 * Integration tests for the {@link ProvinceStisticsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProvinceStisticsResourceIT {

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

  private static final String ENTITY_API_URL = "/api/province-stistics";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong longCount = new AtomicLong(
    random.nextInt() + (2 * Integer.MAX_VALUE)
  );

  @Autowired
  private ObjectMapper om;

  @Autowired
  private ProvinceStisticsRepository provinceStisticsRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restProvinceStisticsMockMvc;

  private ProvinceStistics provinceStistics;

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static ProvinceStistics createEntity(EntityManager em) {
    ProvinceStistics provinceStistics = new ProvinceStistics()
      .name(DEFAULT_NAME)
      .totalCount(DEFAULT_TOTAL_COUNT)
      .onlineCount(DEFAULT_ONLINE_COUNT)
      .offlineCount(DEFAULT_OFFLINE_COUNT)
      .otherCount(DEFAULT_OTHER_COUNT)
      .statisticDate(DEFAULT_STATISTIC_DATE)
      .statisticTime(DEFAULT_STATISTIC_TIME);
    return provinceStistics;
  }

  /**
   * Create an updated entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static ProvinceStistics createUpdatedEntity(EntityManager em) {
    ProvinceStistics provinceStistics = new ProvinceStistics()
      .name(UPDATED_NAME)
      .totalCount(UPDATED_TOTAL_COUNT)
      .onlineCount(UPDATED_ONLINE_COUNT)
      .offlineCount(UPDATED_OFFLINE_COUNT)
      .otherCount(UPDATED_OTHER_COUNT)
      .statisticDate(UPDATED_STATISTIC_DATE)
      .statisticTime(UPDATED_STATISTIC_TIME);
    return provinceStistics;
  }

  @BeforeEach
  public void initTest() {
    provinceStistics = createEntity(em);
  }

  @Test
  @Transactional
  void createProvinceStistics() throws Exception {
    long databaseSizeBeforeCreate = getRepositoryCount();
    // Create the ProvinceStistics
    var returnedProvinceStistics = om.readValue(
      restProvinceStisticsMockMvc
        .perform(
          post(ENTITY_API_URL)
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsBytes(provinceStistics))
        )
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString(),
      ProvinceStistics.class
    );

    // Validate the ProvinceStistics in the database
    assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
    assertProvinceStisticsUpdatableFieldsEquals(
      returnedProvinceStistics,
      getPersistedProvinceStistics(returnedProvinceStistics)
    );
  }

  @Test
  @Transactional
  void createProvinceStisticsWithExistingId() throws Exception {
    // Create the ProvinceStistics with an existing ID
    provinceStistics.setId(1L);

    long databaseSizeBeforeCreate = getRepositoryCount();

    // An entity with an existing ID cannot be created, so this API call must fail
    restProvinceStisticsMockMvc
      .perform(
        post(ENTITY_API_URL)
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(provinceStistics))
      )
      .andExpect(status().isBadRequest());

    // Validate the ProvinceStistics in the database
    assertSameRepositoryCount(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void checkNameIsRequired() throws Exception {
    long databaseSizeBeforeTest = getRepositoryCount();
    // set the field null
    provinceStistics.setName(null);

    // Create the ProvinceStistics, which fails.

    restProvinceStisticsMockMvc
      .perform(
        post(ENTITY_API_URL)
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(provinceStistics))
      )
      .andExpect(status().isBadRequest());

    assertSameRepositoryCount(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void getAllProvinceStistics() throws Exception {
    // Initialize the database
    provinceStisticsRepository.saveAndFlush(provinceStistics);

    // Get all the provinceStisticsList
    restProvinceStisticsMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(
        jsonPath("$.[*].id").value(hasItem(provinceStistics.getId().intValue()))
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
  void getProvinceStistics() throws Exception {
    // Initialize the database
    provinceStisticsRepository.saveAndFlush(provinceStistics);

    // Get the provinceStistics
    restProvinceStisticsMockMvc
      .perform(get(ENTITY_API_URL_ID, provinceStistics.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(provinceStistics.getId().intValue()))
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
  void getNonExistingProvinceStistics() throws Exception {
    // Get the provinceStistics
    restProvinceStisticsMockMvc
      .perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
      .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putExistingProvinceStistics() throws Exception {
    // Initialize the database
    provinceStisticsRepository.saveAndFlush(provinceStistics);

    long databaseSizeBeforeUpdate = getRepositoryCount();

    // Update the provinceStistics
    ProvinceStistics updatedProvinceStistics = provinceStisticsRepository
      .findById(provinceStistics.getId())
      .orElseThrow();
    // Disconnect from session so that the updates on updatedProvinceStistics are not directly saved in db
    em.detach(updatedProvinceStistics);
    updatedProvinceStistics
      .name(UPDATED_NAME)
      .totalCount(UPDATED_TOTAL_COUNT)
      .onlineCount(UPDATED_ONLINE_COUNT)
      .offlineCount(UPDATED_OFFLINE_COUNT)
      .otherCount(UPDATED_OTHER_COUNT)
      .statisticDate(UPDATED_STATISTIC_DATE)
      .statisticTime(UPDATED_STATISTIC_TIME);

    restProvinceStisticsMockMvc
      .perform(
        put(ENTITY_API_URL_ID, updatedProvinceStistics.getId())
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(updatedProvinceStistics))
      )
      .andExpect(status().isOk());

    // Validate the ProvinceStistics in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
    assertPersistedProvinceStisticsToMatchAllProperties(
      updatedProvinceStistics
    );
  }

  @Test
  @Transactional
  void putNonExistingProvinceStistics() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    provinceStistics.setId(longCount.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restProvinceStisticsMockMvc
      .perform(
        put(ENTITY_API_URL_ID, provinceStistics.getId())
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(provinceStistics))
      )
      .andExpect(status().isBadRequest());

    // Validate the ProvinceStistics in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchProvinceStistics() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    provinceStistics.setId(longCount.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restProvinceStisticsMockMvc
      .perform(
        put(ENTITY_API_URL_ID, longCount.incrementAndGet())
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(provinceStistics))
      )
      .andExpect(status().isBadRequest());

    // Validate the ProvinceStistics in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamProvinceStistics() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    provinceStistics.setId(longCount.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restProvinceStisticsMockMvc
      .perform(
        put(ENTITY_API_URL)
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(provinceStistics))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the ProvinceStistics in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdateProvinceStisticsWithPatch() throws Exception {
    // Initialize the database
    provinceStisticsRepository.saveAndFlush(provinceStistics);

    long databaseSizeBeforeUpdate = getRepositoryCount();

    // Update the provinceStistics using partial update
    ProvinceStistics partialUpdatedProvinceStistics = new ProvinceStistics();
    partialUpdatedProvinceStistics.setId(provinceStistics.getId());

    partialUpdatedProvinceStistics
      .onlineCount(UPDATED_ONLINE_COUNT)
      .offlineCount(UPDATED_OFFLINE_COUNT)
      .statisticTime(UPDATED_STATISTIC_TIME);

    restProvinceStisticsMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedProvinceStistics.getId())
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(partialUpdatedProvinceStistics))
      )
      .andExpect(status().isOk());

    // Validate the ProvinceStistics in the database

    assertSameRepositoryCount(databaseSizeBeforeUpdate);
    assertProvinceStisticsUpdatableFieldsEquals(
      createUpdateProxyForBean(
        partialUpdatedProvinceStistics,
        provinceStistics
      ),
      getPersistedProvinceStistics(provinceStistics)
    );
  }

  @Test
  @Transactional
  void fullUpdateProvinceStisticsWithPatch() throws Exception {
    // Initialize the database
    provinceStisticsRepository.saveAndFlush(provinceStistics);

    long databaseSizeBeforeUpdate = getRepositoryCount();

    // Update the provinceStistics using partial update
    ProvinceStistics partialUpdatedProvinceStistics = new ProvinceStistics();
    partialUpdatedProvinceStistics.setId(provinceStistics.getId());

    partialUpdatedProvinceStistics
      .name(UPDATED_NAME)
      .totalCount(UPDATED_TOTAL_COUNT)
      .onlineCount(UPDATED_ONLINE_COUNT)
      .offlineCount(UPDATED_OFFLINE_COUNT)
      .otherCount(UPDATED_OTHER_COUNT)
      .statisticDate(UPDATED_STATISTIC_DATE)
      .statisticTime(UPDATED_STATISTIC_TIME);

    restProvinceStisticsMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedProvinceStistics.getId())
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(partialUpdatedProvinceStistics))
      )
      .andExpect(status().isOk());

    // Validate the ProvinceStistics in the database

    assertSameRepositoryCount(databaseSizeBeforeUpdate);
    assertProvinceStisticsUpdatableFieldsEquals(
      partialUpdatedProvinceStistics,
      getPersistedProvinceStistics(partialUpdatedProvinceStistics)
    );
  }

  @Test
  @Transactional
  void patchNonExistingProvinceStistics() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    provinceStistics.setId(longCount.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restProvinceStisticsMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, provinceStistics.getId())
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(provinceStistics))
      )
      .andExpect(status().isBadRequest());

    // Validate the ProvinceStistics in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchProvinceStistics() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    provinceStistics.setId(longCount.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restProvinceStisticsMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(provinceStistics))
      )
      .andExpect(status().isBadRequest());

    // Validate the ProvinceStistics in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamProvinceStistics() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    provinceStistics.setId(longCount.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restProvinceStisticsMockMvc
      .perform(
        patch(ENTITY_API_URL)
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(provinceStistics))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the ProvinceStistics in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deleteProvinceStistics() throws Exception {
    // Initialize the database
    provinceStisticsRepository.saveAndFlush(provinceStistics);

    long databaseSizeBeforeDelete = getRepositoryCount();

    // Delete the provinceStistics
    restProvinceStisticsMockMvc
      .perform(
        delete(ENTITY_API_URL_ID, provinceStistics.getId())
          .with(csrf())
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
  }

  protected long getRepositoryCount() {
    return provinceStisticsRepository.count();
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

  protected ProvinceStistics getPersistedProvinceStistics(
    ProvinceStistics provinceStistics
  ) {
    return provinceStisticsRepository
      .findById(provinceStistics.getId())
      .orElseThrow();
  }

  protected void assertPersistedProvinceStisticsToMatchAllProperties(
    ProvinceStistics expectedProvinceStistics
  ) {
    assertProvinceStisticsAllPropertiesEquals(
      expectedProvinceStistics,
      getPersistedProvinceStistics(expectedProvinceStistics)
    );
  }

  protected void assertPersistedProvinceStisticsToMatchUpdatableProperties(
    ProvinceStistics expectedProvinceStistics
  ) {
    assertProvinceStisticsAllUpdatablePropertiesEquals(
      expectedProvinceStistics,
      getPersistedProvinceStistics(expectedProvinceStistics)
    );
  }
}
