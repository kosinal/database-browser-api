package cz.kosina.databasebrowser.controller;

import com.google.common.collect.Iterables;
import cz.kosina.databasebrowser.domain.dto.ConnectionProperties;
import cz.kosina.databasebrowser.service.api.ConnectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller used for CRUD operations with {@link ConnectionProperties}
 */
@SuppressWarnings("unused")
@RestController
@RequestMapping("connections")
@Api(value = "Connections", description = "Operations for storing and retrieving database connections.")
public class ConnectionController {

    /**
     * Service used for CRUD operations
     */
    private final ConnectionService connectionService;

    @Autowired
    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = Validate.notNull(connectionService);
    }

    @ApiOperation(value = "Create new instance of connection property")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created"),
            @ApiResponse(code = 409, message = "If there is already connection with the same name"),
    })
    @PostMapping
    public ResponseEntity<ConnectionProperties> create(@RequestBody @Valid ConnectionProperties connectionProperties) {
        return connectionService.create(connectionProperties)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.CONFLICT));
    }

    @ApiOperation(value = "Update existing connection properties")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated"),
            @ApiResponse(code = 404, message = "If the connection property is not found in the database"),
            @ApiResponse(code = 500, message = "Usually if version is outdated"),
    })
    @PutMapping
    public ResponseEntity<ConnectionProperties> update(@RequestBody @Valid ConnectionProperties connectionProperties) {
        return connectionService.update(connectionProperties)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ApiOperation(value = "Delete connection property identified by name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted"),
            @ApiResponse(code = 404, message = "If the connection is not found"),
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        final boolean deleted = connectionService.delete(id);
        final HttpStatus status = deleted ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity(status);
    }

    @ApiOperation(value = "List all connections stored in database")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "All listed connections"),
    })
    @GetMapping
    public ResponseEntity<ConnectionProperties[]> findAll() {
        return ResponseEntity.ok(
                Iterables.toArray(
                        connectionService.getAll(),
                        ConnectionProperties.class
                )
        );
    }

    @ApiOperation(value = "Get the connection identified by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Selected connection"),
            @ApiResponse(code = 404, message = "If none connection is found")
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<ConnectionProperties> findById(@PathVariable String id) {
        return connectionService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
