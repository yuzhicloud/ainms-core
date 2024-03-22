package com.yuzhi.ainms.core.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class PowerPlantAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPowerPlantAllPropertiesEquals(PowerPlant expected, PowerPlant actual) {
        assertPowerPlantAutoGeneratedPropertiesEquals(expected, actual);
        assertPowerPlantAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPowerPlantAllUpdatablePropertiesEquals(PowerPlant expected, PowerPlant actual) {
        assertPowerPlantUpdatableFieldsEquals(expected, actual);
        assertPowerPlantUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPowerPlantAutoGeneratedPropertiesEquals(PowerPlant expected, PowerPlant actual) {
        assertThat(expected)
            .as("Verify PowerPlant auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPowerPlantUpdatableFieldsEquals(PowerPlant expected, PowerPlant actual) {
        assertThat(expected)
            .as("Verify PowerPlant relevant properties")
            .satisfies(e -> assertThat(e.getPowerPlantName()).as("check powerPlantName").isEqualTo(actual.getPowerPlantName()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPowerPlantUpdatableRelationshipsEquals(PowerPlant expected, PowerPlant actual) {
        assertThat(expected)
            .as("Verify PowerPlant relationships")
            .satisfies(e -> assertThat(e.getPowerPlant()).as("check powerPlant").isEqualTo(actual.getPowerPlant()));
    }
}
