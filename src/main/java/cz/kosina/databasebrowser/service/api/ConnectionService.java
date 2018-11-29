package cz.kosina.databasebrowser.service.api;

import cz.kosina.databasebrowser.domain.dto.ConnectionProperties;

import java.util.List;
import java.util.Optional;

/**
 * Service used for basic CRUD operation with {@link ConnectionProperties}
 */
public interface ConnectionService {

    /**
     * Create new instance of connection properties in the database. If the connection property with same name already
     * exists, return empty optional
     *
     * @param connectionProperties connection property to be created
     * @return optional with created property if it was created. Otherwise empty.
     */
    Optional<ConnectionProperties> create(ConnectionProperties connectionProperties);

    /**
     * Update existing instance of connection properties in the database. If the connection property with same name
     * doe not exists, return empty optional
     *
     * @param connectionProperties connection property to be updated
     * @return optional with updated properties, if update was performed. Otherwise empty.
     */
    Optional<ConnectionProperties> update(ConnectionProperties connectionProperties);

    /**
     * Delete connection property identified by name
     *
     * @param name name of the connection
     * @return true if connection property was removed. Otherwise false.
     */
    boolean delete(String name);

    /**
     * List all stored connection properties in the database
     *
     * @return all stored {@link ConnectionProperties}
     */
    List<ConnectionProperties> getAll();

    /**
     * Find connection property stored in the database and identified by name. If none property is found, than
     * return empty {@link Optional}
     *
     * @param name name of the connection property stored in DB
     * @return found connection property or empty, if no name exists in DB
     */
    Optional<ConnectionProperties> getById(String name);
}
