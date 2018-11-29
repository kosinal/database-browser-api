package cz.kosina.databasebrowser.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Object stored in database (table, view ...)
 */
@Data
public class DatabaseObject {
    /**
     * Name of the object stored in database
     */
    @NotNull
    private final String name;
    /**
     * Type of the object (table, view ...)
     */
    @NotNull
    private final String type;
    /**
     * Comment of the object
     */
    @NotNull
    private final String comment;
}
