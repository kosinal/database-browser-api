package cz.kosina.databasebrowser.repository;

import cz.kosina.databasebrowser.domain.entity.ConnectionPropertiesEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository used for CRUD operations with {@link ConnectionPropertiesEntity}
 */
public interface ConnectionRepository extends CrudRepository<ConnectionPropertiesEntity, String> {
}
