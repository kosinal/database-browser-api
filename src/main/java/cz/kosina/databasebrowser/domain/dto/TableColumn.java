package cz.kosina.databasebrowser.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Column in the table
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableColumn {
    /**
     * Order number of the column in the table
     */
    @NotNull
    private Integer orderNo;
    /**
     * Name of the column
     */
    @NotNull
    private String name;
    /**
     * Type of the column
     */
    @NotNull
    private String type;
    /**
     * Size of the column stored in DB
     */
    private Integer size;
    /**
     * YES/NO is the column allows null inside
     */
    private String nullable;
    /**
     * Flag if the column is primary key
     */
    @NotNull
    private boolean primaryKey;
    /**
     * Comment for the given column
     */
    private String comment;
    /**
     * In case of number: Amount of fractional digits
     */
    private Integer fractionalDigits;
}
