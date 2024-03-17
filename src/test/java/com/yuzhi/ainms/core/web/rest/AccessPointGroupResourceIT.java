package com.yuzhi.ainms.core.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.yuzhi.ainms.core.IntegrationTest;
import com.yuzhi.ainms.core.domain.AccessPointGroup;
import com.yuzhi.ainms.core.repository.AccessPointGroupRepository;
import com.yuzhi.ainms.core.service.AccessPointGroupService;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AccessPointGroupResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AccessPointGroupResourceIT {

    private static final Long DEFAULT_APG_ID = 1L;
    private static final Long UPDATED_APG_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/access-point-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AccessPointGroupRepository accessPointGroupRepository;

    @Mock
    private AccessPointGroupRepository accessPointGroupRepositoryMock;

    @Mock
    private AccessPointGroupService accessPointGroupServiceMock;

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
        AccessPointGroup accessPointGroup = new AccessPointGroup().apgId(DEFAULT_APG_ID).name(DEFAULT_NAME);
        return accessPointGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccessPointGroup createUpdatedEntity(EntityManager em) {
        AccessPointGroup accessPointGroup = new AccessPointGroup().apgId(UPDATED_APG_ID).name(UPDATED_NAME);
        return accessPointGroup;
    }

    @BeforeEach
    public void initTest() {
        accessPointGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createAccessPointGroup() throws Exception {
        int databaseSizeBeforeCreate = accessPointGroupRepository.findAll().size();
        // Create the AccessPointGroup
        restAccessPointGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accessPointGroup))
            )
            .andExpect(status().isCreated());

        // Validate the AccessPointGroup in the database
        List<AccessPointGroup> accessPointGroupList = accessPointGroupRepository.findAll();
        assertThat(accessPointGroupList).hasSize(databaseSizeBeforeCreate + 1);
        AccessPointGroup testAccessPointGroup = accessPointGroupList.get(accessPointGroupList.size() - 1);
        assertThat(testAccessPointGroup.getApgId()).isEqualTo(DEFAULT_APG_ID);
        assertThat(testAccessPointGroup.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createAccessPointGroupWithExistingId() throws Exception {
        // Create the AccessPointGroup with an existing ID
        accessPointGroup.setId(1L);

        int databaseSizeBeforeCreate = accessPointGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccessPointGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accessPointGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccessPointGroup in the database
        List<AccessPointGroup> accessPointGroupList = accessPointGroupRepository.findAll();
        assertThat(accessPointGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = accessPointGroupRepository.findAll().size();
        // set the field null
        accessPointGroup.setName(null);

        // Create the AccessPointGroup, which fails.

        restAccessPointGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accessPointGroup))
            )
            .andExpect(status().isBadRequest());

        List<AccessPointGroup> accessPointGroupList = accessPointGroupRepository.findAll();
        assertThat(accessPointGroupList).hasSize(databaseSizeBeforeTest);
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
            .andExpect(jsonPath("$.[*].id").value(hasItem(accessPointGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].apgId").value(hasItem(DEFAULT_APG_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAccessPointGroupsWithEagerRelationshipsIsEnabled() throws Exception {
        when(accessPointGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAccessPointGroupMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(accessPointGroupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAccessPointGroupsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(accessPointGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAccessPointGroupMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(accessPointGroupRepositoryMock, times(1)).findAll(any(Pageable.class));
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
            .andExpect(jsonPath("$.apgId").value(DEFAULT_APG_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingAccessPointGroup() throws Exception {
        // Get the accessPointGroup
        restAccessPointGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAccessPointGroup() throws Exception {
        // Initialize the database
        accessPointGroupRepository.saveAndFlush(accessPointGroup);

        int databaseSizeBeforeUpdate = accessPointGroupRepository.findAll().size();

        // Update the accessPointGroup
        AccessPointGroup updatedAccessPointGroup = accessPointGroupRepository.findById(accessPointGroup.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAccessPointGroup are not directly saved in db
        em.detach(updatedAccessPointGroup);
        updatedAccessPointGroup.apgId(UPDATED_APG_ID).name(UPDATED_NAME);

        restAccessPointGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAccessPointGroup.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAccessPointGroup))
            )
            .andExpect(status().isOk());

        // Validate the AccessPointGroup in the database
        List<AccessPointGroup> accessPointGroupList = accessPointGroupRepository.findAll();
        assertThat(accessPointGroupList).hasSize(databaseSizeBeforeUpdate);
        AccessPointGroup testAccessPointGroup = accessPointGroupList.get(accessPointGroupList.size() - 1);
        assertThat(testAccessPointGroup.getApgId()).isEqualTo(UPDATED_APG_ID);
        assertThat(testAccessPointGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingAccessPointGroup() throws Exception {
        int databaseSizeBeforeUpdate = accessPointGroupRepository.findAll().size();
        accessPointGroup.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccessPointGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accessPointGroup.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accessPointGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccessPointGroup in the database
        List<AccessPointGroup> accessPointGroupList = accessPointGroupRepository.findAll();
        assertThat(accessPointGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccessPointGroup() throws Exception {
        int databaseSizeBeforeUpdate = accessPointGroupRepository.findAll().size();
        accessPointGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccessPointGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accessPointGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccessPointGroup in the database
        List<AccessPointGroup> accessPointGroupList = accessPointGroupRepository.findAll();
        assertThat(accessPointGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccessPointGroup() throws Exception {
        int databaseSizeBeforeUpdate = accessPointGroupRepository.findAll().size();
        accessPointGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccessPointGroupMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accessPointGroup))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccessPointGroup in the database
        List<AccessPointGroup> accessPointGroupList = accessPointGroupRepository.findAll();
        assertThat(accessPointGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAccessPointGroupWithPatch() throws Exception {
        // Initialize the database
        accessPointGroupRepository.saveAndFlush(accessPointGroup);

        int databaseSizeBeforeUpdate = accessPointGroupRepository.findAll().size();

        // Update the accessPointGroup using partial update
        AccessPointGroup partialUpdatedAccessPointGroup = new AccessPointGroup();
        partialUpdatedAccessPointGroup.setId(accessPointGroup.getId());

        partialUpdatedAccessPointGroup.name(UPDATED_NAME);

        restAccessPointGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccessPointGroup.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccessPointGroup))
            )
            .andExpect(status().isOk());

        // Validate the AccessPointGroup in the database
        List<AccessPointGroup> accessPointGroupList = accessPointGroupRepository.findAll();
        assertThat(accessPointGroupList).hasSize(databaseSizeBeforeUpdate);
        AccessPointGroup testAccessPointGroup = accessPointGroupList.get(accessPointGroupList.size() - 1);
        assertThat(testAccessPointGroup.getApgId()).isEqualTo(DEFAULT_APG_ID);
        assertThat(testAccessPointGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateAccessPointGroupWithPatch() throws Exception {
        // Initialize the database
        accessPointGroupRepository.saveAndFlush(accessPointGroup);

        int databaseSizeBeforeUpdate = accessPointGroupRepository.findAll().size();

        // Update the accessPointGroup using partial update
        AccessPointGroup partialUpdatedAccessPointGroup = new AccessPointGroup();
        partialUpdatedAccessPointGroup.setId(accessPointGroup.getId());

        partialUpdatedAccessPointGroup.apgId(UPDATED_APG_ID).name(UPDATED_NAME);

        restAccessPointGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccessPointGroup.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccessPointGroup))
            )
            .andExpect(status().isOk());

        // Validate the AccessPointGroup in the database
        List<AccessPointGroup> accessPointGroupList = accessPointGroupRepository.findAll();
        assertThat(accessPointGroupList).hasSize(databaseSizeBeforeUpdate);
        AccessPointGroup testAccessPointGroup = accessPointGroupList.get(accessPointGroupList.size() - 1);
        assertThat(testAccessPointGroup.getApgId()).isEqualTo(UPDATED_APG_ID);
        assertThat(testAccessPointGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingAccessPointGroup() throws Exception {
        int databaseSizeBeforeUpdate = accessPointGroupRepository.findAll().size();
        accessPointGroup.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccessPointGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accessPointGroup.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accessPointGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccessPointGroup in the database
        List<AccessPointGroup> accessPointGroupList = accessPointGroupRepository.findAll();
        assertThat(accessPointGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccessPointGroup() throws Exception {
        int databaseSizeBeforeUpdate = accessPointGroupRepository.findAll().size();
        accessPointGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccessPointGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accessPointGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccessPointGroup in the database
        List<AccessPointGroup> accessPointGroupList = accessPointGroupRepository.findAll();
        assertThat(accessPointGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccessPointGroup() throws Exception {
        int databaseSizeBeforeUpdate = accessPointGroupRepository.findAll().size();
        accessPointGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccessPointGroupMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accessPointGroup))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccessPointGroup in the database
        List<AccessPointGroup> accessPointGroupList = accessPointGroupRepository.findAll();
        assertThat(accessPointGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAccessPointGroup() throws Exception {
        // Initialize the database
        accessPointGroupRepository.saveAndFlush(accessPointGroup);

        int databaseSizeBeforeDelete = accessPointGroupRepository.findAll().size();

        // Delete the accessPointGroup
        restAccessPointGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, accessPointGroup.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AccessPointGroup> accessPointGroupList = accessPointGroupRepository.findAll();
        assertThat(accessPointGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
