package cz.kosina.databasebrowser.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

/**
 * Entity object used for storing data into database
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionPropertiesEntity {
    /**
     * Name of the connection
     */
    @Id
    @NotNull
    private String name;
    /**
     * JDBC connection url
     */
    @NotNull
    private String url;
    /**
     * Username for connection
     */
    @NotNull
    private String username;

    /**
     * Password for connection
     */
    private String password;

    /**
     * Version of the object. Used for optimistic locking
     */
    @Version
    private Long version;
}
