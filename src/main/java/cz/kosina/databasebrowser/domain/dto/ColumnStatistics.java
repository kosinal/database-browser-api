package cz.kosina.databasebrowser.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Statistics for given column
 */
@Data
public class ColumnStatistics {
    /**
     * Name of the column
     */
    @NotNull
    private final String name;
    /**
     * Minimal value converted to a string
     */
    @NotNull
    private final String minValue;
    /**
     * Maximal value converted to a string
     */
    @NotNull
    private final String maxValue;
    /**
     * Number of null values for given column
     */
    @NotNull
    private final int nullValuesNumber;
}
