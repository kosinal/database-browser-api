package cz.kosina.databasebrowser.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Properties for connection definition
 */
@Data
public class ConnectionProperties {
    /**
     * Name of the connection
     */
    @NotNull
    private String name;
    /**
     * Url used for connection
     */
    @NotNull
    private String url;
    /**
     * Username for accessing database
     */
    @NotNull
    private String username;

    /**
     * Password for accessing database
     */
    private String password;

    /**
     * Version of the object stored in database
     */
    private Long version;
}
