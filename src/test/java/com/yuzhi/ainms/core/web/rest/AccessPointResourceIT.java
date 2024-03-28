package com.yuzhi.ainms.core.web.rest;

import static com.yuzhi.ainms.core.domain.AccessPointAsserts.*;
import static com.yuzhi.ainms.core.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuzhi.ainms.core.IntegrationTest;
import com.yuzhi.ainms.core.domain.AccessPoint;
import com.yuzhi.ainms.core.repository.AccessPointRepository;
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
 * Integration tests for the {@link AccessPointResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccessPointResourceIT {

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

  private static final String ENTITY_API_URL = "/api/access-points";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong longCount = new AtomicLong(
    random.nextInt() + (2 * Integer.MAX_VALUE)
  );

  @Autowired
  private ObjectMapper om;

  @Autowired
  private AccessPointRepository accessPointRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restAccessPointMockMvc;

  private AccessPoint accessPoint;

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static AccessPoint createEntity(EntityManager em) {
    AccessPoint accessPoint = new AccessPoint()
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
    return accessPoint;
  }

  /**
   * Create an updated entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static AccessPoint createUpdatedEntity(EntityManager em) {
    AccessPoint accessPoint = new AccessPoint()
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
    return accessPoint;
  }

  @BeforeEach
  public void initTest() {
    accessPoint = createEntity(em);
  }

  @Test
  @Transactional
  void createAccessPoint() throws Exception {
    long databaseSizeBeforeCreate = getRepositoryCount();
    // Create the AccessPoint
    var returnedAccessPoint = om.readValue(
      restAccessPointMockMvc
        .perform(
          post(ENTITY_API_URL)
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsBytes(accessPoint))
        )
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString(),
      AccessPoint.class
    );

    // Validate the AccessPoint in the database
    assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
    assertAccessPointUpdatableFieldsEquals(
      returnedAccessPoint,
      getPersistedAccessPoint(returnedAccessPoint)
    );
  }

  @Test
  @Transactional
  void createAccessPointWithExistingId() throws Exception {
    // Create the AccessPoint with an existing ID
    accessPoint.setId(1L);

    long databaseSizeBeforeCreate = getRepositoryCount();

    // An entity with an existing ID cannot be created, so this API call must fail
    restAccessPointMockMvc
      .perform(
        post(ENTITY_API_URL)
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(accessPoint))
      )
      .andExpect(status().isBadRequest());

    // Validate the AccessPoint in the database
    assertSameRepositoryCount(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void getAllAccessPoints() throws Exception {
    // Initialize the database
    accessPointRepository.saveAndFlush(accessPoint);

    // Get all the accessPointList
    restAccessPointMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(
        jsonPath("$.[*].id").value(hasItem(accessPoint.getId().intValue()))
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
  void getAccessPoint() throws Exception {
    // Initialize the database
    accessPointRepository.saveAndFlush(accessPoint);

    // Get the accessPoint
    restAccessPointMockMvc
      .perform(get(ENTITY_API_URL_ID, accessPoint.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(accessPoint.getId().intValue()))
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
  void getNonExistingAccessPoint() throws Exception {
    // Get the accessPoint
    restAccessPointMockMvc
      .perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
      .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putExistingAccessPoint() throws Exception {
    // Initialize the database
    accessPointRepository.saveAndFlush(accessPoint);

    long databaseSizeBeforeUpdate = getRepositoryCount();

    // Update the accessPoint
    AccessPoint updatedAccessPoint = accessPointRepository
      .findById(accessPoint.getId())
      .orElseThrow();
    // Disconnect from session so that the updates on updatedAccessPoint are not directly saved in db
    em.detach(updatedAccessPoint);
    updatedAccessPoint
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

    restAccessPointMockMvc
      .perform(
        put(ENTITY_API_URL_ID, updatedAccessPoint.getId())
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(updatedAccessPoint))
      )
      .andExpect(status().isOk());

    // Validate the AccessPoint in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
    assertPersistedAccessPointToMatchAllProperties(updatedAccessPoint);
  }

  @Test
  @Transactional
  void putNonExistingAccessPoint() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    accessPoint.setId(longCount.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restAccessPointMockMvc
      .perform(
        put(ENTITY_API_URL_ID, accessPoint.getId())
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(accessPoint))
      )
      .andExpect(status().isBadRequest());

    // Validate the AccessPoint in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchAccessPoint() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    accessPoint.setId(longCount.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restAccessPointMockMvc
      .perform(
        put(ENTITY_API_URL_ID, longCount.incrementAndGet())
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(accessPoint))
      )
      .andExpect(status().isBadRequest());

    // Validate the AccessPoint in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamAccessPoint() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    accessPoint.setId(longCount.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restAccessPointMockMvc
      .perform(
        put(ENTITY_API_URL)
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(om.writeValueAsBytes(accessPoint))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the AccessPoint in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdateAccessPointWithPatch() throws Exception {
    // Initialize the database
    accessPointRepository.saveAndFlush(accessPoint);

    long databaseSizeBeforeUpdate = getRepositoryCount();

    // Update the accessPoint using partial update
    AccessPoint partialUpdatedAccessPoint = new AccessPoint();
    partialUpdatedAccessPoint.setId(accessPoint.getId());

    partialUpdatedAccessPoint
      .neid(UPDATED_NEID)
      .aliasname(UPDATED_ALIASNAME)
      .nename(UPDATED_NENAME)
      .necategory(UPDATED_NECATEGORY)
      .netype(UPDATED_NETYPE)
      .nevendorname(UPDATED_NEVENDORNAME)
      .neip(UPDATED_NEIP)
      .nemac(UPDATED_NEMAC)
      .version(UPDATED_VERSION)
      .neiptype(UPDATED_NEIPTYPE)
      .subnet(UPDATED_SUBNET)
      .neosversion(UPDATED_NEOSVERSION);

    restAccessPointMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedAccessPoint.getId())
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(partialUpdatedAccessPoint))
      )
      .andExpect(status().isOk());

    // Validate the AccessPoint in the database

    assertSameRepositoryCount(databaseSizeBeforeUpdate);
    assertAccessPointUpdatableFieldsEquals(
      createUpdateProxyForBean(partialUpdatedAccessPoint, accessPoint),
      getPersistedAccessPoint(accessPoint)
    );
  }

  @Test
  @Transactional
  void fullUpdateAccessPointWithPatch() throws Exception {
    // Initialize the database
    accessPointRepository.saveAndFlush(accessPoint);

    long databaseSizeBeforeUpdate = getRepositoryCount();

    // Update the accessPoint using partial update
    AccessPoint partialUpdatedAccessPoint = new AccessPoint();
    partialUpdatedAccessPoint.setId(accessPoint.getId());

    partialUpdatedAccessPoint
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

    restAccessPointMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedAccessPoint.getId())
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(partialUpdatedAccessPoint))
      )
      .andExpect(status().isOk());

    // Validate the AccessPoint in the database

    assertSameRepositoryCount(databaseSizeBeforeUpdate);
    assertAccessPointUpdatableFieldsEquals(
      partialUpdatedAccessPoint,
      getPersistedAccessPoint(partialUpdatedAccessPoint)
    );
  }

  @Test
  @Transactional
  void patchNonExistingAccessPoint() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    accessPoint.setId(longCount.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restAccessPointMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, accessPoint.getId())
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(accessPoint))
      )
      .andExpect(status().isBadRequest());

    // Validate the AccessPoint in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchAccessPoint() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    accessPoint.setId(longCount.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restAccessPointMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(accessPoint))
      )
      .andExpect(status().isBadRequest());

    // Validate the AccessPoint in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamAccessPoint() throws Exception {
    long databaseSizeBeforeUpdate = getRepositoryCount();
    accessPoint.setId(longCount.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restAccessPointMockMvc
      .perform(
        patch(ENTITY_API_URL)
          .with(csrf())
          .contentType("application/merge-patch+json")
          .content(om.writeValueAsBytes(accessPoint))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the AccessPoint in the database
    assertSameRepositoryCount(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deleteAccessPoint() throws Exception {
    // Initialize the database
    accessPointRepository.saveAndFlush(accessPoint);

    long databaseSizeBeforeDelete = getRepositoryCount();

    // Delete the accessPoint
    restAccessPointMockMvc
      .perform(
        delete(ENTITY_API_URL_ID, accessPoint.getId())
          .with(csrf())
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
  }

  protected long getRepositoryCount() {
    return accessPointRepository.count();
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

  protected AccessPoint getPersistedAccessPoint(AccessPoint accessPoint) {
    return accessPointRepository.findById(accessPoint.getId()).orElseThrow();
  }

  protected void assertPersistedAccessPointToMatchAllProperties(
    AccessPoint expectedAccessPoint
  ) {
    assertAccessPointAllPropertiesEquals(
      expectedAccessPoint,
      getPersistedAccessPoint(expectedAccessPoint)
    );
  }

  protected void assertPersistedAccessPointToMatchUpdatableProperties(
    AccessPoint expectedAccessPoint
  ) {
    assertAccessPointAllUpdatablePropertiesEquals(
      expectedAccessPoint,
      getPersistedAccessPoint(expectedAccessPoint)
    );
  }
}
