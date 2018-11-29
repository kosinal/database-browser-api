package cz.kosina.databasebrowser.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Column in the table
 */
@Data
public class TableColumn {
    /**
     * Order number of the column in the table
     */
    @NotNull
    private final Integer orderNo;
    /**
     * Name of the column
     */
    @NotNull
    private final String name;
    /**
     * Type of the column
     */
    @NotNull
    private final String type;
    /**
     * Size of the column stored in DB
     */
    private final Integer size;
    /**
     * YES/NO is the column allows null inside
     */
    private final String nullable;
    /**
     * Flag if the column is primary key
     */
    @NotNull
    private final boolean isPrimaryKey;
    /**
     * Comment for the given column
     */
    private final String comment;
    /**
     * In case of number: Amount of fractional digits
     */
    private final Integer fractionalDigits;
}
