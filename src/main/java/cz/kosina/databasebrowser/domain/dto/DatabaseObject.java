package cz.kosina.databasebrowser.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Object stored in database (table, view ...)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseObject {
    /**
     * Name of the object stored in database
     */
    @NotNull
    private String name;
    /**
     * Type of the object (table, view ...)
     */
    @NotNull
    private String type;
    /**
     * Comment of the object
     */
    @NotNull
    private String comment;
}
