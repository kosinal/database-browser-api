package cz.kosina.databasebrowser.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Statistics for given column
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableStatistics {
    /**
     * Number of rows in the table
     */
    @NotNull
    private Integer rows;
    /**
     * Number of columns in the table
     */
    private Integer columns;
    /**
     * List of all columns in the table
     */
    private List<TableColumn> columnStatisticsList;
}
