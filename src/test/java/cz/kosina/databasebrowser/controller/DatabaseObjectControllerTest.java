package cz.kosina.databasebrowser.controller;

import cz.kosina.databasebrowser.RestTestStarter;
import cz.kosina.databasebrowser.domain.dto.ColumnStatistics;
import cz.kosina.databasebrowser.domain.dto.DatabaseObject;
import cz.kosina.databasebrowser.domain.dto.TableColumn;
import cz.kosina.databasebrowser.domain.dto.TableStatistics;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class DatabaseObjectControllerTest extends RestTestStarter {

    private static final Map<String, String> URL_VARIABLES = new HashMap<>();

    @BeforeClass
    public static void initClass() {
        URL_VARIABLES.put("id", CONNECTION_NAME);
        URL_VARIABLES.put("catalog", TEST_CATALOG);
        URL_VARIABLES.put("schema", TEST_SCHEME);
        URL_VARIABLES.put("tableName", TEST_TABLE);
    }

    @Override
    protected String createUrl(String name) {
        return super.createUrl(String.format("databases/%s", name));
    }

    @Test
    public void findCatalogs() {
        ResponseEntity<String[]> resp = template.getForEntity(
                createUrl("{id}/catalogs"),
                String[].class,
                URL_VARIABLES
        );

        assertThat(
                resp,
                allOf(
                        hasProperty("statusCode", equalTo(HttpStatus.OK)),
                        hasProperty("body",
                                arrayContaining(TEST_CATALOG)
                        )
                )
        );
    }

    @Test
    public void findSchemas() {
        ResponseEntity<String[]> resp = template.getForEntity(
                createUrl("{id}/{catalog}/schemas"),
                String[].class,
                URL_VARIABLES
        );

        assertThat(
                resp,
                allOf(
                        hasProperty("statusCode", equalTo(HttpStatus.OK)),
                        hasProperty("body",
                                arrayContainingInAnyOrder(TEST_SCHEME, "INFORMATION_SCHEMA", "PUBLIC", "SECOND_SCHEMA")
                        )
                )
        );
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findTables() {
        ResponseEntity<DatabaseObject[]> resp = template.getForEntity(
                createUrl("{id}/{catalog}/{schema}/tables"),
                DatabaseObject[].class,
                URL_VARIABLES
        );

        assertThat(
                resp,
                allOf(
                        hasProperty("statusCode", equalTo(HttpStatus.OK)),
                        hasProperty("body",
                                arrayContaining(
                                        allOf(
                                                hasProperty("name", equalTo(TEST_TABLE)),
                                                hasProperty("type", equalTo("TABLE")),
                                                hasProperty("comment", equalTo(""))
                                        )
                                )
                        )
                )
        );
    }

    @SuppressWarnings("unchecked")
    @Test
    public void previewData() {
        ResponseEntity<String[][]> resp = template.getForEntity(
                createUrl("{id}/{catalog}/{schema}/{tableName}/data"),
                String[][].class,
                URL_VARIABLES
        );
        assertThat(
                resp,
                allOf(
                        hasProperty("statusCode", equalTo(HttpStatus.OK)),
                        hasProperty("body",
                                arrayContainingInAnyOrder(
                                        arrayContaining("a", "1"),
                                        arrayContaining("b", "2"),
                                        arrayContaining("c", "3"),
                                        arrayContaining("d", "4"),
                                        arrayContaining("e", "5")
                                )
                        )
                )
        );
    }


    @SuppressWarnings("unchecked")
    @Test
    public void findColumns() {
        ResponseEntity<TableColumn[]> resp = template.getForEntity(
                createUrl("{id}/{catalog}/{schema}/{tableName}/columns"),
                TableColumn[].class,
                URL_VARIABLES
        );

        assertThat(
                resp,
                allOf(
                        hasProperty("statusCode", equalTo(HttpStatus.OK)),
                        hasProperty("body",
                                arrayContainingInAnyOrder(
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
                                ))
                )
        );
    }

    @SuppressWarnings("unchecked")
    @Test
    public void columnStatistics() {
        ResponseEntity<ColumnStatistics[]> resp = template.getForEntity(
                createUrl("{id}/{catalog}/{schema}/{tableName}/columnStatistics"),
                ColumnStatistics[].class,
                URL_VARIABLES
        );

        assertThat(
                resp,
                allOf(
                        hasProperty("statusCode", equalTo(HttpStatus.OK)),
                        hasProperty("body",
                                arrayContainingInAnyOrder(
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
                        )
                )
        );
    }

    @SuppressWarnings("unchecked")
    @Test
    public void computeTableStatistics() {
        ResponseEntity<TableStatistics> resp = template.getForEntity(
                createUrl("{id}/{catalog}/{schema}/{tableName}/statistics"),
                TableStatistics.class,
                URL_VARIABLES
        );

        assertThat(
                resp,
                allOf(
                        hasProperty("statusCode", equalTo(HttpStatus.OK)),
                        hasProperty("body",
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
                        )
                )
        );
    }

}