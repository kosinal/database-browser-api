package cz.kosina.databasebrowser.service.impl;

import cz.kosina.databasebrowser.domain.dto.*;
import cz.kosina.databasebrowser.service.api.ConnectionService;
import cz.kosina.databasebrowser.service.api.DatabaseService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Implementation of {@link DatabaseService} for getting information from database about database catalogs, schemas and
 * objects.
 */
@Service
public class DatabaseServiceImpl implements DatabaseService {

    /**
     * Service used for getting the information about connections
     */
    private final ConnectionService connectionService;
    /**
     * Convert map between numerical SQL Types and real names
     */
    private final Map<Integer, String> typeNames;

    @Autowired
    public DatabaseServiceImpl(ConnectionService connectionService) {
        this.connectionService = Validate.notNull(connectionService);
        typeNames = Stream.of(Types.class.getFields()).collect(
                Collectors.toMap(
                        this::getTypeInt,
                        Field::getName
                )
        );
    }

    /**
     * Get the integer representation of the field
     *
     * @param i field to be evaluated
     * @return numerical representation
     */
    @SneakyThrows
    private Integer getTypeInt(Field i) {
        return i.getInt(null);
    }

    /**
     * Get properties stored in database
     *
     * @param connectionName identification of {@link ConnectionProperties}
     * @return properties stored in databse
     */
    private Optional<ConnectionProperties> getConnProperties(String connectionName) {
        return connectionService.getById(connectionName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<String>> findSchemas(String connectionName, String catalogName) {
        return getConnProperties(connectionName)
                .map(i -> listSchemas(i, catalogName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<String>> findCatalogs(String connectionName) {
        return getConnProperties(connectionName)
                .map(this::listCatalogs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<DatabaseObject>> findTables(String connectionName, String catalogName, String schemaName) {
        return getConnProperties(connectionName)
                .map(i -> listTables(i, catalogName, schemaName));
    }

    /**
     * List all database objects (table, view ...) in the connection defined by the connection property and stored in the catalog and the schema.
     *
     * @param connectionProperties properties stored in database
     * @param catalogName          name of the catalog
     * @param schemaName           name of the schema
     * @return list of all database objects
     */
    @SneakyThrows
    private List<DatabaseObject> listTables(ConnectionProperties connectionProperties,
                                            String catalogName,
                                            String schemaName) {
        try (Connection con = establishConnection(connectionProperties)) {
            final List<DatabaseObject> dbObjectsList = new ArrayList<>();
            final ResultSet tables = con.getMetaData().getTables(catalogName, schemaName, null, null);
            while (tables.next()) {
                dbObjectsList.add(
                        new DatabaseObject(
                                tables.getString("TABLE_NAME"),
                                tables.getString("TABLE_TYPE"),
                                tables.getString("REMARKS")
                        )
                );
            }
            return dbObjectsList;
        }
    }

    /**
     * List all schemas in the connection defined by the connection property and stored in the catalog.
     *
     * @param connectionProperties properties stored in database
     * @param catalogName          name of the catalog
     * @return list of names of all schemas
     */
    @SneakyThrows
    private List<String> listSchemas(ConnectionProperties connectionProperties, String catalogName) {
        try (Connection con = establishConnection(connectionProperties)) {
            final List<String> schemasList = new ArrayList<>();
            final ResultSet schemas = con.getMetaData().getSchemas(catalogName, null);
            while (schemas.next()) {
                schemasList.add(schemas.getString("TABLE_SCHEM"));
            }
            return schemasList;
        }
    }

    /**
     * List all catalogs in the connection defined by the connection property.
     *
     * @param connectionProperties properties stored in database
     * @return list of names of all catalogs
     */
    @SneakyThrows
    private List<String> listCatalogs(ConnectionProperties connectionProperties) {
        try (Connection con = establishConnection(connectionProperties)) {
            final List<String> catalogList = new ArrayList<>();
            final ResultSet catalogs = con.getMetaData().getCatalogs();
            while (catalogs.next()) {
                catalogList.add(catalogs.getString("TABLE_CAT"));
            }
            return catalogList;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<TableColumn>> findColumns(String connectionName, String catalogName, String schemaName, String tableName) {
        return getConnProperties(connectionName)
                .map(i -> listColumns(i, catalogName, schemaName, tableName));
    }

    /**
     * Get all primary keys for the given table
     *
     * @param con         connection used for communication with database
     * @param catalogName name of the catalog
     * @param schemaName  name of the schema
     * @param tableName   name of te table
     * @return set of all columns which are primary key in the table
     * @throws SQLException if getting information from DB does not work
     */
    private Set<String> listPrimaryKeys(Connection con,
                                        String catalogName,
                                        String schemaName,
                                        String tableName) throws SQLException {
        final Set<String> columns = new HashSet<>();
        final ResultSet primaryKeys = con.getMetaData().getPrimaryKeys(
                catalogName,
                schemaName,
                tableName
        );
        while (primaryKeys.next()) {
            columns.add(primaryKeys.getString("COLUMN_NAME"));
        }
        return columns;
    }

    /**
     * Find all columns in to table. If the connection does not exists, than the result is empty optional.
     *
     * @param connectionProperties properties read from database
     * @param catalogName          name of the catalog
     * @param schemaName           name of the schema
     * @param tableName            name of the table
     * @return list of all columns in the table if the connection exists. Otherwise empty
     */
    @SneakyThrows
    private List<TableColumn> listColumns(ConnectionProperties connectionProperties,
                                          String catalogName,
                                          String schemaName,
                                          String tableName) {
        try (Connection con = establishConnection(connectionProperties)) {
            return listColumns(con, catalogName, schemaName, tableName);
        }
    }

    /**
     * Find all columns in to table. If the connection does not exists, than the result is empty optional.
     *
     * @param con         open database connection
     * @param catalogName name of the catalog
     * @param schemaName  name of the schema
     * @param tableName   name of the table
     * @return list of all columns in the table if the connection exists. Otherwise empty
     */
    private List<TableColumn> listColumns(Connection con, String catalogName, String schemaName, String tableName) throws SQLException {
        final Set<String> primaryKeys = listPrimaryKeys(con, catalogName, schemaName, tableName);
        final List<TableColumn> columnList = new ArrayList<>();
        final ResultSet columns = con.getMetaData().getColumns(
                catalogName,
                schemaName,
                tableName,
                null
        );
        while (columns.next()) {
            final String columnName = columns.getString("COLUMN_NAME");
            columnList.add(
                    new TableColumn(
                            columns.getInt("ORDINAL_POSITION"),
                            columnName,
                            typeNames.get(columns.getInt("DATA_TYPE")),
                            columns.getInt("COLUMN_SIZE"),
                            columns.getString("IS_NULLABLE"),
                            primaryKeys.contains(columnName),
                            columns.getString("REMARKS"),
                            columns.getInt("DECIMAL_DIGITS")
                    )
            );
        }
        return columnList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<List<String>>> listData(String connectionName,
                                                 String catalogName,
                                                 String schemaName,
                                                 String tableName) {
        return getConnProperties(connectionName)
                .map(i -> previewData(i, catalogName, schemaName, tableName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<ColumnStatistics>> getColumnsStatistics(String connectionName, String catalogName, String schemaName, String tableName) {
        return getConnProperties(connectionName)
                .map(i -> previewColumnStatistics(i, catalogName, schemaName, tableName));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<TableStatistics> getTableStatistics(String connectionName, String catalogName, String schemaName, String tableName) {
        return getConnProperties(connectionName)
                .map(i -> previewTableStatistics(i, catalogName, schemaName, tableName));

    }

    /**
     * List sample of data stored in the table and convert them into string to be readable in the HTTP response. If the
     * connection is not stored, than the result is empty.
     *
     * @param connectionProperties connection stored in db
     * @param catalogName          name of the catalog
     * @param schemaName           name of the schema
     * @param tableName            name of the table
     * @return sample data converted into string if the connection exists. Otherwise empty
     */
    @SneakyThrows
    private List<List<String>> previewData(ConnectionProperties connectionProperties,
                                           String catalogName,
                                           String schemaName,
                                           String tableName) {
        try (Connection con = establishConnection(connectionProperties)) {
            Statement statement = con.createStatement();
            final int fetchSize = 20;
            statement.setFetchSize(fetchSize);
            statement.setMaxRows(fetchSize);
            List<List<String>> retResults = new ArrayList<>();
            final ResultSet rs = statement.executeQuery(
                    String.format("select * from %s", createFullTableName(catalogName, schemaName, tableName))
            );
            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                List<String> resLine = IntStream.rangeClosed(1, columnCount)
                        .mapToObj(i -> extractFromResultSet(rs, i))
                        .collect(Collectors.toList());
                retResults.add(resLine);
            }
            return retResults;
        }
    }

    /**
     * Create full table name with basic SQL injection protection
     *
     * @param catalogName name of the catalog
     * @param schemaName  name of the schema
     * @param tableName   name of the table
     * @return full table name used for querying
     */
    private String createFullTableName(String catalogName,
                                       String schemaName,
                                       String tableName) {
        return String.format("%s.%s.%s", escapeSQL(catalogName), escapeSQL(schemaName), escapeSQL(tableName));
    }

    /**
     * Get the column statistics for the table in the catalog and schema. If the
     * connection is not stored, than the result is empty.
     *
     * @param connectionProperties connection stored in db
     * @param catalogName          name of the catalog
     * @param schemaName           name of the schema
     * @param tableName            name of the table
     * @return column statistics if the connection exists. Otherwise empty
     */
    @SneakyThrows
    private List<ColumnStatistics> previewColumnStatistics(ConnectionProperties connectionProperties,
                                                           String catalogName,
                                                           String schemaName,
                                                           String tableName) {
        try (Connection con = establishConnection(connectionProperties)) {
            final String fullTableName = createFullTableName(catalogName, schemaName, tableName);
            return listColumns(con, catalogName, schemaName, tableName).stream()
                    .map(i -> computeStatistics(fullTableName, con, i))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Get the table statistics for the table in the catalog and schema. If the
     * connection is not stored, than the result is empty.
     *
     * @param connectionProperties connection stored in db
     * @param catalogName          name of the catalog
     * @param schemaName           name of the schema
     * @param tableName            name of the table
     * @return table statistics if the connection exists. Otherwise empty
     */
    @SneakyThrows
    private TableStatistics previewTableStatistics(ConnectionProperties connectionProperties,
                                                   String catalogName,
                                                   String schemaName,
                                                   String tableName) {
        try (Connection con = establishConnection(connectionProperties)) {
            final String fullTableName = createFullTableName(catalogName, schemaName, tableName);
            ResultSet resultSet = con.createStatement().executeQuery(
                    String.format("select count(*) from %s", fullTableName)
            );
            if (resultSet.next()) {
                final Integer rowCount = resultSet.getInt(1);
                List<TableColumn> columns = listColumns(con, catalogName, schemaName, tableName);
                return new TableStatistics(
                        rowCount,
                        columns.size(),
                        columns
                );
            } else {
                throw new IllegalStateException("Count does not have any results");
            }
        }
    }

    /**
     * Compute column statistics for given table column. Supported statistics are: min, max and number of null entries
     *
     * @param fullTableName full table name
     * @param con           connection used for database communication
     * @param tableColumn   column to be evaluated
     * @return statistics for given column
     */
    @SneakyThrows
    private ColumnStatistics computeStatistics(String fullTableName, Connection con, TableColumn tableColumn) {
        final String columnName = tableColumn.getName();
        final ResultSet rs = con.createStatement().executeQuery(
                String.format(
                        "select min(%1$s), max(%1$s), " +
                                "sum(case when %1$s is null then 1 else 0 end) from %2$s",
                        columnName,
                        fullTableName)
        );
        if (rs.next()) {
            return new ColumnStatistics(
                    columnName,
                    rs.getString(1),
                    rs.getString(2),
                    rs.getInt(3)
            );
        } else {
            throw new IllegalStateException("Count does not have any results");
        }
    }

    /**
     * Extract result in string form in {@link ResultSet} and covert the throw exception
     *
     * @param rs result set to be evaluated
     * @param i  number of index of column
     * @return string representation of value
     */
    @SneakyThrows
    private String extractFromResultSet(ResultSet rs, int i) {
        return rs.getString(i);
    }

    /**
     * Do simple SQL injection protection
     *
     * @param sql sql query
     * @return query with basic protection
     */
    private String escapeSQL(String sql) {
        return sql.replace("'", "''");
    }

    /**
     * Create new connection into database defined by properties
     *
     * @param properties properties for database connection
     * @return new database connection
     * @throws SQLException if there is some problem with connection initialization
     */
    private Connection establishConnection(ConnectionProperties properties) throws SQLException {
        return DriverManager.getConnection(
                properties.getUrl(),
                properties.getUsername(),
                properties.getPassword()
        );
    }
}
