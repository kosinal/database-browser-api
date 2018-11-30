package cz.kosina.databasebrowser.controller;

import cz.kosina.databasebrowser.RestTestStarter;
import cz.kosina.databasebrowser.domain.dto.ConnectionProperties;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ConnectionControllerTest extends RestTestStarter {


    private static final String PATH = "/connections";
    private static final String NAMED_PATH = String.format("%s/{name}", PATH);
    private static final Map<String, String> URL_VARIABLES = Collections.singletonMap("name", "TestH2");

    @SuppressWarnings("unchecked")
    @Test
    public void testListObjects() {
        ResponseEntity<ConnectionProperties[]> connections = template.getForEntity(
                createUrl(PATH),
                ConnectionProperties[].class
        );
        assertThat(
                connections,
                allOf(
                        hasProperty("statusCode", equalTo(HttpStatus.OK)),
                        hasProperty("body",
                                arrayContaining(
                                        allOf(
                                                hasProperty("name", equalTo("TestH2")),
                                                hasProperty("url", equalTo("jdbc:h2:file:./src/test/resources/test")),
                                                hasProperty("username", equalTo("sa")),
                                                hasProperty("password", nullValue()),
                                                hasProperty("version", equalTo(0L))
                                        )
                                )
                        )
                )
        );
    }

    @Test
    public void createExistingWillFail() {
        ResponseEntity<ConnectionProperties> resp = template.postForEntity(
                createUrl(PATH),
                new ConnectionProperties(
                        "TestH2",
                        "a",
                        "a",
                        null,
                        null
                ),
                ConnectionProperties.class
        );
        assertThat(
                resp,
                hasProperty("statusCode", equalTo(HttpStatus.CONFLICT))
        );
    }

    @SuppressWarnings("unchecked")
    @Test
    public void createEntity() {
        ResponseEntity<ConnectionProperties> resp = template.postForEntity(
                createUrl(PATH),
                new ConnectionProperties(
                        "NewConnection",
                        "test url",
                        "test user",
                        "test password",
                        null
                ),
                ConnectionProperties.class
        );
        assertThat(
                resp,
                allOf(
                        hasProperty("statusCode", equalTo(HttpStatus.OK)),
                        hasProperty("body",
                                allOf(
                                        hasProperty("name", equalTo("NewConnection")),
                                        hasProperty("url", equalTo("test url")),
                                        hasProperty("username", equalTo("test user")),
                                        hasProperty("password", equalTo("test password")),
                                        hasProperty("version", equalTo(0L))
                                )
                        )
                )
        );
    }

    @Test
    public void updateEntity() {
        template.put(
                createUrl(PATH),
                new ConnectionProperties(
                        "TestH2",
                        "a",
                        "a",
                        null,
                        0L
                )
        );
        ResponseEntity<ConnectionProperties> resp = template.getForEntity(
                createUrl("/connections/TestH2"),
                ConnectionProperties.class
        );

        assertThat(
                resp,
                allOf(
                        hasProperty("statusCode", equalTo(HttpStatus.OK)),
                        hasProperty("body",
                                allOf(
                                        hasProperty("name", equalTo("TestH2")),
                                        hasProperty("url", equalTo("a")),
                                        hasProperty("username", equalTo("a")),
                                        hasProperty("password", nullValue()),
                                        hasProperty("version", equalTo(1L))
                                )
                        )
                )
        );
    }

    @Test
    public void updateNonExistEntityWillFail() {
        template.put(
                createUrl(PATH),
                new ConnectionProperties(
                        "Fake",
                        "a",
                        "a",
                        null,
                        0L
                )
        );
        ResponseEntity<ConnectionProperties> resp = template.getForEntity(
                createUrl("/connections/Fake"),
                ConnectionProperties.class
        );

        assertThat(
                resp,
                hasProperty("statusCode", equalTo(HttpStatus.NOT_FOUND))
        );
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findById() {
        ResponseEntity<ConnectionProperties> connections = template.getForEntity(
                createUrl(NAMED_PATH),
                ConnectionProperties.class,
                URL_VARIABLES
        );
        assertThat(
                connections,
                allOf(
                        hasProperty("statusCode", equalTo(HttpStatus.OK)),
                        hasProperty("body",
                                allOf(
                                        hasProperty("name", equalTo("TestH2")),
                                        hasProperty("url", equalTo("jdbc:h2:file:./src/test/resources/test")),
                                        hasProperty("username", equalTo("sa")),
                                        hasProperty("password", nullValue()),
                                        hasProperty("version", equalTo(0L))
                                )
                        )
                )
        );
    }

    @Test
    public void deleteIsOk() {
        template.delete(
                createUrl(createUrl(NAMED_PATH)),
                URL_VARIABLES
        );
        assertThat(
                template.getForEntity(
                        createUrl(createUrl(NAMED_PATH)),
                        ConnectionProperties.class,
                        URL_VARIABLES
                ),
                hasProperty("statusCode", equalTo(HttpStatus.NOT_FOUND))
        );
    }
}