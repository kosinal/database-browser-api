package cz.kosina.databasebrowser.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Statistics for given column
 */
@Data
public class TableStatistics {
    /**
     * Number of rows in the table
     */
    @NotNull
    private final Integer rows;
    /**
     * Number of columns in the table
     */
    private final Integer columns;
    /**
     * List of all columns in the table
     */
    private final List<TableColumn> columnStatisticsList;
}
