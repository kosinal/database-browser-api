package cz.kosina.databasebrowser.service.api;

import cz.kosina.databasebrowser.domain.dto.ColumnStatistics;
import cz.kosina.databasebrowser.domain.dto.DatabaseObject;
import cz.kosina.databasebrowser.domain.dto.TableColumn;
import cz.kosina.databasebrowser.domain.dto.TableStatistics;

import java.util.List;
import java.util.Optional;

/**
 * Service used for listing information from datables
 */
public interface DatabaseService {

    /**
     * Find all columns in to table. If the connection does not exists, than the result is empty optional.
     *
     * @param connectionName name of the connection stored in db
     * @param catalogName    name of the catalog
     * @param schemaName     name of the schema
     * @param tableName      name of the table
     * @return list of all columns in the table if the connection exists. Otherwise empty
     */
    Optional<List<TableColumn>> findColumns(String connectionName, String catalogName, String schemaName, String tableName);

    /**
     * Find all table stored in catalog and schema. If the connection does not exists, than the result is empty optional.
     *
     * @param connectionName name of the connection stored in db
     * @param catalogName    name of the catalog
     * @param schemaName     name of the schema
     * @return list of all database object stored in schema if the connection exists. Otherwise empty
     */
    Optional<List<DatabaseObject>> findTables(String connectionName, String catalogName, String schemaName);

    /**
     * Find all schemas in the catalog. If the connection does not exists, than the result is empty optional.
     *
     * @param connectionName name of the connection stored in db
     * @param catalogName    name of the catalog
     * @return list of all name of schema stored in catalog if the connection exists. Otherwise empty
     */
    Optional<List<String>> findSchemas(String connectionName, String catalogName);

    /**
     * Find all catalogs in the connection. If the connection does not exists, than the result is empty optional.
     *
     * @param connectionName name of the connection stored in db
     * @return list of all name of catalogs stored in connection if the connection exists. Otherwise empty
     */
    Optional<List<String>> findCatalogs(String connectionName);

    /**
     * List sample of data stored in the table and convert them into string to be readable in the HTTP response. If the
     * connection is not stored, than the result is empty.
     *
     * @param connectionName name of the connection stored in db
     * @param catalogName    name of the catalog
     * @param schemaName     name of the schema
     * @param tableName      name of the table
     * @return sample data converted into string if the connection exists. Otherwise empty
     */
    Optional<List<List<String>>> listData(String connectionName, String catalogName,
                                          String schemaName, String tableName);

    /**
     * Get the column statistics for the table in the catalog and schema. If the
     * connection is not stored, than the result is empty.
     *
     * @param connectionName name of the connection stored in db
     * @param catalogName    name of the catalog
     * @param schemaName     name of the schema
     * @param tableName      name of the table
     * @return column statistics if the connection exists. Otherwise empty
     */
    Optional<List<ColumnStatistics>> getColumnsStatistics(String connectionName, String catalogName,
                                                          String schemaName, String tableName);

    /**
     * Get the table statistics for the table in the catalog and schema. If the
     * connection is not stored, than the result is empty.
     *
     * @param connectionName name of the connection stored in db
     * @param catalogName    name of the catalog
     * @param schemaName     name of the schema
     * @param tableName      name of the table
     * @return table statistics if the connection exists. Otherwise empty
     */
    Optional<TableStatistics> getTableStatistics(String connectionName, String catalogName,
                                                 String schemaName, String tableName);
}
