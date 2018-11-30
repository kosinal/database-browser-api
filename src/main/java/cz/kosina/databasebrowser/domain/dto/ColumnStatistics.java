package cz.kosina.databasebrowser.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Statistics for given column
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnStatistics {
    /**
     * Name of the column
     */
    @NotNull
    private String name;
    /**
     * Minimal value converted to a string
     */
    @NotNull
    private String minValue;
    /**
     * Maximal value converted to a string
     */
    @NotNull
    private String maxValue;
    /**
     * Number of null values for given column
     */
    @NotNull
    private int nullValuesNumber;
}
