package com.yuzhi.ainms.core.web.rest;

import static com.yuzhi.ainms.core.domain.PowerPlantAsserts.*;
import static com.yuzhi.ainms.core.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuzhi.ainms.core.IntegrationTest;
import com.yuzhi.ainms.core.domain.PowerPlant;
import com.yuzhi.ainms.core.repository.PowerPlantRepository;
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
 * Integration tests for the {@link PowerPlantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PowerPlantResourceIT {

    private static final String DEFAULT_POWER_PLANT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_POWER_PLANT_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/power-plants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PowerPlantRepository powerPlantRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPowerPlantMockMvc;

    private PowerPlant powerPlant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PowerPlant createEntity(EntityManager em) {
        PowerPlant powerPlant = new PowerPlant().powerPlantName(DEFAULT_POWER_PLANT_NAME);
        return powerPlant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PowerPlant createUpdatedEntity(EntityManager em) {
        PowerPlant powerPlant = new PowerPlant().powerPlantName(UPDATED_POWER_PLANT_NAME);
        return powerPlant;
    }

    @BeforeEach
    public void initTest() {
        powerPlant = createEntity(em);
    }

    @Test
    @Transactional
    void createPowerPlant() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PowerPlant
        var returnedPowerPlant = om.readValue(
            restPowerPlantMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(powerPlant))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PowerPlant.class
        );

        // Validate the PowerPlant in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPowerPlantUpdatableFieldsEquals(returnedPowerPlant, getPersistedPowerPlant(returnedPowerPlant));
    }

    @Test
    @Transactional
    void createPowerPlantWithExistingId() throws Exception {
        // Create the PowerPlant with an existing ID
        powerPlant.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPowerPlantMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(powerPlant)))
            .andExpect(status().isBadRequest());

        // Validate the PowerPlant in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPowerPlants() throws Exception {
        // Initialize the database
        powerPlantRepository.saveAndFlush(powerPlant);

        // Get all the powerPlantList
        restPowerPlantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(powerPlant.getId().intValue())))
            .andExpect(jsonPath("$.[*].powerPlantName").value(hasItem(DEFAULT_POWER_PLANT_NAME)));
    }

    @Test
    @Transactional
    void getPowerPlant() throws Exception {
        // Initialize the database
        powerPlantRepository.saveAndFlush(powerPlant);

        // Get the powerPlant
        restPowerPlantMockMvc
            .perform(get(ENTITY_API_URL_ID, powerPlant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(powerPlant.getId().intValue()))
            .andExpect(jsonPath("$.powerPlantName").value(DEFAULT_POWER_PLANT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingPowerPlant() throws Exception {
        // Get the powerPlant
        restPowerPlantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPowerPlant() throws Exception {
        // Initialize the database
        powerPlantRepository.saveAndFlush(powerPlant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the powerPlant
        PowerPlant updatedPowerPlant = powerPlantRepository.findById(powerPlant.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPowerPlant are not directly saved in db
        em.detach(updatedPowerPlant);
        updatedPowerPlant.powerPlantName(UPDATED_POWER_PLANT_NAME);

        restPowerPlantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPowerPlant.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPowerPlant))
            )
            .andExpect(status().isOk());

        // Validate the PowerPlant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPowerPlantToMatchAllProperties(updatedPowerPlant);
    }

    @Test
    @Transactional
    void putNonExistingPowerPlant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        powerPlant.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPowerPlantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, powerPlant.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(powerPlant))
            )
            .andExpect(status().isBadRequest());

        // Validate the PowerPlant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPowerPlant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        powerPlant.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPowerPlantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(powerPlant))
            )
            .andExpect(status().isBadRequest());

        // Validate the PowerPlant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPowerPlant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        powerPlant.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPowerPlantMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(powerPlant)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PowerPlant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePowerPlantWithPatch() throws Exception {
        // Initialize the database
        powerPlantRepository.saveAndFlush(powerPlant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the powerPlant using partial update
        PowerPlant partialUpdatedPowerPlant = new PowerPlant();
        partialUpdatedPowerPlant.setId(powerPlant.getId());

        partialUpdatedPowerPlant.powerPlantName(UPDATED_POWER_PLANT_NAME);

        restPowerPlantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPowerPlant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPowerPlant))
            )
            .andExpect(status().isOk());

        // Validate the PowerPlant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPowerPlantUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPowerPlant, powerPlant),
            getPersistedPowerPlant(powerPlant)
        );
    }

    @Test
    @Transactional
    void fullUpdatePowerPlantWithPatch() throws Exception {
        // Initialize the database
        powerPlantRepository.saveAndFlush(powerPlant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the powerPlant using partial update
        PowerPlant partialUpdatedPowerPlant = new PowerPlant();
        partialUpdatedPowerPlant.setId(powerPlant.getId());

        partialUpdatedPowerPlant.powerPlantName(UPDATED_POWER_PLANT_NAME);

        restPowerPlantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPowerPlant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPowerPlant))
            )
            .andExpect(status().isOk());

        // Validate the PowerPlant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPowerPlantUpdatableFieldsEquals(partialUpdatedPowerPlant, getPersistedPowerPlant(partialUpdatedPowerPlant));
    }

    @Test
    @Transactional
    void patchNonExistingPowerPlant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        powerPlant.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPowerPlantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, powerPlant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(powerPlant))
            )
            .andExpect(status().isBadRequest());

        // Validate the PowerPlant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPowerPlant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        powerPlant.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPowerPlantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(powerPlant))
            )
            .andExpect(status().isBadRequest());

        // Validate the PowerPlant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPowerPlant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        powerPlant.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPowerPlantMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(powerPlant))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PowerPlant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePowerPlant() throws Exception {
        // Initialize the database
        powerPlantRepository.saveAndFlush(powerPlant);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the powerPlant
        restPowerPlantMockMvc
            .perform(delete(ENTITY_API_URL_ID, powerPlant.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return powerPlantRepository.count();
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

    protected PowerPlant getPersistedPowerPlant(PowerPlant powerPlant) {
        return powerPlantRepository.findById(powerPlant.getId()).orElseThrow();
    }

    protected void assertPersistedPowerPlantToMatchAllProperties(PowerPlant expectedPowerPlant) {
        assertPowerPlantAllPropertiesEquals(expectedPowerPlant, getPersistedPowerPlant(expectedPowerPlant));
    }

    protected void assertPersistedPowerPlantToMatchUpdatableProperties(PowerPlant expectedPowerPlant) {
        assertPowerPlantAllUpdatablePropertiesEquals(expectedPowerPlant, getPersistedPowerPlant(expectedPowerPlant));
    }
}
