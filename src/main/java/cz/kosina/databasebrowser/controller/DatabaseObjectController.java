package cz.kosina.databasebrowser.controller;

import cz.kosina.databasebrowser.domain.dto.ColumnStatistics;
import cz.kosina.databasebrowser.domain.dto.DatabaseObject;
import cz.kosina.databasebrowser.domain.dto.TableColumn;
import cz.kosina.databasebrowser.domain.dto.TableStatistics;
import cz.kosina.databasebrowser.service.api.DatabaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.Validate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller used for accesing information about database object defined by connection
 */
@SuppressWarnings("unused")
@RestController
@RequestMapping("databases")
@Api(value = "Databases", description = "Operations for getting information about objects stored in database")
public class DatabaseObjectController {
    private static final String[] STRING_DEF_MAP = new String[0];
    private static final DatabaseObject[] TABLE_DEF_MAP = new DatabaseObject[0];
    private static final TableColumn[] COLUMN_DEF_MAP = new TableColumn[0];

    private final DatabaseService databaseService;

    @Autowired
    public DatabaseObjectController(DatabaseService databaseService,
                                    ModelMapper modelMapper) {
        this.databaseService = Validate.notNull(databaseService);
    }

    @ApiOperation(value = "Find all catalogs stored in defined connection")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "If connection exists"),
            @ApiResponse(code = 404, message = "If there is none stored connection"),
    })
    @GetMapping(value = "/{id}/catalogs")
    public ResponseEntity<String[]> findCatalogs(@PathVariable String id) {
        return databaseService.findCatalogs(id)
                .map(i -> i.toArray(STRING_DEF_MAP))
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @ApiOperation(value = "Find all schemas stored in the defined catalog")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "If connection exists"),
            @ApiResponse(code = 404, message = "If there is none stored connection"),
    })
    @GetMapping(value = "/{id}/{catalog}/schemas")
    public ResponseEntity<String[]> findSchemas(@PathVariable String id, @PathVariable String catalog) {
        return databaseService.findSchemas(id, catalog)
                .map(i -> i.toArray(STRING_DEF_MAP))
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @ApiOperation(value = "Find all tables stored in the defined catalog and schema")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "If connection exists"),
            @ApiResponse(code = 404, message = "If there is none stored connection"),
    })
    @GetMapping(value = "/{id}/{catalog}/{schema}/tables")
    public ResponseEntity<DatabaseObject[]> findTables(@PathVariable String id, @PathVariable String catalog,
                                                       @PathVariable String schema) {
        return databaseService.findTables(id, catalog, schema)
                .map(i -> i.toArray(TABLE_DEF_MAP))
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @ApiOperation(value = "Find all columns stored in the table identified by catalog, schema and table name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "If connection exists"),
            @ApiResponse(code = 404, message = "If there is none stored connection"),
    })
    @GetMapping(value = "/{id}/{catalog}/{schema}/{tableName}/columns")
    public ResponseEntity<TableColumn[]> findColumns(@PathVariable String id,
                                                     @PathVariable String catalog,
                                                     @PathVariable String schema,
                                                     @PathVariable String tableName) {
        return databaseService.findColumns(id, catalog, schema, tableName)
                .map(i -> i.toArray(COLUMN_DEF_MAP))
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @ApiOperation(value = "Preview first 20 rows stored in the table identified by catalog, schema and table name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "If connection exists"),
            @ApiResponse(code = 404, message = "If there is none stored connection"),
    })
    @GetMapping(value = "/{id}/{catalog}/{schema}/{tableName}/data")
    public ResponseEntity<List<List<String>>> previewData(@PathVariable String id,
                                                          @PathVariable String catalog,
                                                          @PathVariable String schema,
                                                          @PathVariable String tableName) {
        return databaseService.listData(id, catalog, schema, tableName)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @ApiOperation(value = "Compute basic column statistics for the table identified by catalog, schema and table name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "If connection exists"),
            @ApiResponse(code = 404, message = "If there is none stored connection"),
    })
    @GetMapping(value = "/{id}/{catalog}/{schema}/{tableName}/columnStatistics")
    public ResponseEntity<List<ColumnStatistics>> computeColumnStatistics(@PathVariable String id,
                                                                          @PathVariable String catalog,
                                                                          @PathVariable String schema,
                                                                          @PathVariable String tableName) {
        return databaseService.getColumnsStatistics(id, catalog, schema, tableName)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @ApiOperation(value = "Compute table statistics for the table identified by catalog, schema and table name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "If connection exists"),
            @ApiResponse(code = 404, message = "If there is none stored connection"),
    })
    @GetMapping(value = "/{id}/{catalog}/{schema}/{tableName}/statistics")
    public ResponseEntity<TableStatistics> computeTableStatistics(@PathVariable String id,
                                                                  @PathVariable String catalog,
                                                                  @PathVariable String schema,
                                                                  @PathVariable String tableName) {
        return databaseService.getTableStatistics(id, catalog, schema, tableName)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
