package com.yuzhi.ainms.core.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ProvinceStisticsAsserts {

  /**
   * Asserts that the entity has all properties (fields/relationships) set.
   *
   * @param expected the expected entity
   * @param actual the actual entity
   */
  public static void assertProvinceStisticsAllPropertiesEquals(
    ProvinceStistics expected,
    ProvinceStistics actual
  ) {
    assertProvinceStisticsAutoGeneratedPropertiesEquals(expected, actual);
    assertProvinceStisticsAllUpdatablePropertiesEquals(expected, actual);
  }

  /**
   * Asserts that the entity has all updatable properties (fields/relationships) set.
   *
   * @param expected the expected entity
   * @param actual the actual entity
   */
  public static void assertProvinceStisticsAllUpdatablePropertiesEquals(
    ProvinceStistics expected,
    ProvinceStistics actual
  ) {
    assertProvinceStisticsUpdatableFieldsEquals(expected, actual);
    assertProvinceStisticsUpdatableRelationshipsEquals(expected, actual);
  }

  /**
   * Asserts that the entity has all the auto generated properties (fields/relationships) set.
   *
   * @param expected the expected entity
   * @param actual the actual entity
   */
  public static void assertProvinceStisticsAutoGeneratedPropertiesEquals(
    ProvinceStistics expected,
    ProvinceStistics actual
  ) {
    assertThat(expected)
      .as("Verify ProvinceStistics auto generated properties")
      .satisfies(
        e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId())
      );
  }

  /**
   * Asserts that the entity has all the updatable fields set.
   *
   * @param expected the expected entity
   * @param actual the actual entity
   */
  public static void assertProvinceStisticsUpdatableFieldsEquals(
    ProvinceStistics expected,
    ProvinceStistics actual
  ) {
    assertThat(expected)
      .as("Verify ProvinceStistics relevant properties")
      .satisfies(
        e ->
          assertThat(e.getName()).as("check name").isEqualTo(actual.getName())
      )
      .satisfies(
        e ->
          assertThat(e.getTotalCount())
            .as("check totalCount")
            .isEqualTo(actual.getTotalCount())
      )
      .satisfies(
        e ->
          assertThat(e.getOnlineCount())
            .as("check onlineCount")
            .isEqualTo(actual.getOnlineCount())
      )
      .satisfies(
        e ->
          assertThat(e.getOfflineCount())
            .as("check offlineCount")
            .isEqualTo(actual.getOfflineCount())
      )
      .satisfies(
        e ->
          assertThat(e.getOtherCount())
            .as("check otherCount")
            .isEqualTo(actual.getOtherCount())
      )
      .satisfies(
        e ->
          assertThat(e.getStatisticDate())
            .as("check statisticDate")
            .isEqualTo(actual.getStatisticDate())
      )
      .satisfies(
        e ->
          assertThat(e.getStatisticTime())
            .as("check statisticTime")
            .isEqualTo(actual.getStatisticTime())
      );
  }

  /**
   * Asserts that the entity has all the updatable relationships set.
   *
   * @param expected the expected entity
   * @param actual the actual entity
   */
  public static void assertProvinceStisticsUpdatableRelationshipsEquals(
    ProvinceStistics expected,
    ProvinceStistics actual
  ) {
    assertThat(expected)
      .as("Verify ProvinceStistics relationships")
      .satisfies(
        e ->
          assertThat(e.getCountry())
            .as("check country")
            .isEqualTo(actual.getCountry())
      );
  }
}
