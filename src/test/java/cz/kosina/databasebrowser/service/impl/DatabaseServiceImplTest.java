package cz.kosina.databasebrowser.service.impl;

import cz.kosina.databasebrowser.DatabaseTestStarter;
import cz.kosina.databasebrowser.domain.dto.ColumnStatistics;
import cz.kosina.databasebrowser.domain.dto.DatabaseObject;
import cz.kosina.databasebrowser.domain.dto.TableColumn;
import cz.kosina.databasebrowser.domain.dto.TableStatistics;
import cz.kosina.databasebrowser.service.api.DatabaseService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class DatabaseServiceImplTest extends DatabaseTestStarter {

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private DatabaseService databaseService;

    @Test
    public void findCatalogs() {
        Optional<List<String>> catalogs = databaseService.findCatalogs(CONNECTION_NAME);

        assertTrue(catalogs.isPresent());

        assertThat(
                catalogs.get(),
                contains(TEST_CATALOG)
        );
    }

    @Test
    public void findCatalogsFake() {
        Optional<List<String>> catalogs = databaseService.findCatalogs(FAKE_CONNECTION_NAME);
        assertFalse(catalogs.isPresent());
    }

    @Test
    public void findSchemas() {
        Optional<List<String>> schema = databaseService.findSchemas(
                CONNECTION_NAME,
                TEST_CATALOG
        );

        assertTrue(schema.isPresent());
        assertThat(
                schema.get(),
                containsInAnyOrder(TEST_SCHEME, "INFORMATION_SCHEMA", "PUBLIC", "SECOND_SCHEMA")
        );
    }

    @Test
    public void findTables() {
        Optional<List<DatabaseObject>> tables = databaseService.findTables(
                CONNECTION_NAME,
                TEST_CATALOG,
                TEST_SCHEME
        );
        assertTrue(tables.isPresent());
        assertThat(
                tables.get(),
                contains(
                        allOf(
                                hasProperty("name", equalTo(TEST_TABLE)),
                                hasProperty("type", equalTo("TABLE")),
                                hasProperty("comment", equalTo(""))
                        )
                )
        );
    }

    @SuppressWarnings("unchecked")
    @Test
    public void previewData() {
        Optional<List<List<String>>> data = databaseService.listData(
                CONNECTION_NAME,
                TEST_CATALOG,
                TEST_SCHEME,
                TEST_TABLE
        );
        assertTrue(data.isPresent());
        assertThat(
                data.get(),
                containsInAnyOrder(
                        contains("a", "1"),
                        contains("b", "2"),
                        contains("c", "3"),
                        contains("d", "4"),
                        contains("e", "5")
                )
        );
    }

    @SuppressWarnings("unchecked")
    @Test
    public void previewColumnStatistics() {
        Optional<List<ColumnStatistics>> columnsStatistics = databaseService.getColumnsStatistics(
                CONNECTION_NAME,
                TEST_CATALOG,
                TEST_SCHEME,
                TEST_TABLE
        );
        assertTrue(columnsStatistics.isPresent());
        assertThat(
                columnsStatistics.get(),
                containsInAnyOrder(
                        allOf(
                                hasProperty("name", equalTo("NAME")),
                                hasProperty("minValue", equalTo("a")),
                                hasProperty("maxValue", equalTo("e")),
                                hasProperty("nullValuesNumber", equalTo(0))
                        ),
                        allOf(
                                hasProperty("name", equalTo("ID")),
                                hasProperty("minValue", equalTo("1")),
                                hasProperty("maxValue", equalTo("5")),
                                hasProperty("nullValuesNumber", equalTo(0))
                        )
                )
        );
    }

    @SuppressWarnings("unchecked")
    @Test
    public void tableStatistics() {
        Optional<TableStatistics> tableStatistics = databaseService.getTableStatistics(
                CONNECTION_NAME,
                TEST_CATALOG,
                TEST_SCHEME,
                TEST_TABLE
        );
        assertTrue(tableStatistics.isPresent());
        assertThat(
                tableStatistics.get(),
                allOf(
                        hasProperty("rows", equalTo(5)),
                        hasProperty("columns", equalTo(2)),
                        hasProperty("columnStatisticsList",
                                containsInAnyOrder(
                                        allOf(
                                                hasProperty("orderNo", equalTo(1)),
                                                hasProperty("name", equalTo("NAME")),
                                                hasProperty("type", equalTo("VARCHAR")),
                                                hasProperty("size", equalTo(255)),
                                                hasProperty("nullable", equalTo("YES")),
                                                hasProperty("comment", equalTo("")),
                                                hasProperty("fractionalDigits", equalTo(0)),
                                                hasProperty("primaryKey", equalTo(false))
                                        ),
                                        allOf(
                                                hasProperty("orderNo", equalTo(2)),
                                                hasProperty("name", equalTo("ID")),
                                                hasProperty("type", equalTo("INTEGER")),
                                                hasProperty("size", equalTo(10)),
                                                hasProperty("nullable", equalTo("NO")),
                                                hasProperty("comment", equalTo("")),
                                                hasProperty("fractionalDigits", equalTo(0)),
                                                hasProperty("primaryKey", equalTo(true))
                                        )
                                )
                        )
                )
        );
    }

    @SuppressWarnings("unchecked")
    @Test
    public void getColumnList() {
        Optional<List<TableColumn>> columns = databaseService.findColumns(
                CONNECTION_NAME,
                TEST_CATALOG,
                TEST_SCHEME,
                TEST_TABLE
        );
        assertTrue(columns.isPresent());
        assertThat(
                columns.get(),
                containsInAnyOrder(
                        allOf(
                                hasProperty("orderNo", equalTo(1)),
                                hasProperty("name", equalTo("NAME")),
                                hasProperty("type", equalTo("VARCHAR")),
                                hasProperty("size", equalTo(255)),
                                hasProperty("nullable", equalTo("YES")),
                                hasProperty("comment", equalTo("")),
                                hasProperty("fractionalDigits", equalTo(0)),
                                hasProperty("primaryKey", equalTo(false))
                        ),
                        allOf(
                                hasProperty("orderNo", equalTo(2)),
                                hasProperty("name", equalTo("ID")),
                                hasProperty("type", equalTo("INTEGER")),
                                hasProperty("size", equalTo(10)),
                                hasProperty("nullable", equalTo("NO")),
                                hasProperty("comment", equalTo("")),
                                hasProperty("fractionalDigits", equalTo(0)),
                                hasProperty("primaryKey", equalTo(true))
                        )
                )
        );
    }
}