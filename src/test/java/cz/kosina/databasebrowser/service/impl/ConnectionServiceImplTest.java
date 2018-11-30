package cz.kosina.databasebrowser.service.impl;

import cz.kosina.databasebrowser.DatabaseTestStarter;
import cz.kosina.databasebrowser.domain.dto.ConnectionProperties;
import cz.kosina.databasebrowser.service.api.ConnectionService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class ConnectionServiceImplTest extends DatabaseTestStarter {

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private ConnectionService connectionService;

    @Test
    public void checkThatReadIsOk() {
        assertThat(
                connectionService.getAll(),
                contains(
                        allOf(
                                hasProperty("name", equalTo("TestH2")),
                                hasProperty("url", equalTo("jdbc:h2:file:./src/test/resources/test")),
                                hasProperty("username", equalTo("sa")),
                                hasProperty("password", nullValue()),
                                hasProperty("version", equalTo(0L)
                                )
                        )
                )
        );
    }

    @Test
    public void updateIsOk() {
        Optional<ConnectionProperties> update = updateToNewTestValue();

        assertTrue(update.isPresent());
        assertThat(
                update.get(),
                allOf(
                        hasProperty("name", equalTo("TestH2")),
                        hasProperty("url", equalTo("jdbc:h2:file:./src/test/resources/test")),
                        hasProperty("username", equalTo("sa")),
                        hasProperty("password", equalTo("newVal")),
                        hasProperty("version", equalTo(0L)
                        )
                )
        );
    }

    @Test
    public void readAfterUpdateIsOk() {
        updateToNewTestValue();
        assertThat(
                connectionService.getAll(),
                contains(
                        allOf(
                                hasProperty("name", equalTo("TestH2")),
                                hasProperty("url", equalTo("jdbc:h2:file:./src/test/resources/test")),
                                hasProperty("username", equalTo("sa")),
                                hasProperty("password", equalTo("newVal")),
                                hasProperty("version", equalTo(1L)
                                )
                        )
                )
        );
    }

    @Test
    public void createAlreadyExistingWillReturnEmpty() {
        Optional<ConnectionProperties> testH2 = connectionService.create(
                new ConnectionProperties(
                        "TestH2", null, null, null, null
                )
        );

        assertFalse(testH2.isPresent());
    }

    @Test
    public void updateNotExistingWillReturnEmpty() {
        Optional<ConnectionProperties> fake = connectionService.update(
                new ConnectionProperties(
                        "fake", null, null, null, null
                )
        );

        assertFalse(fake.isPresent());
    }

    @Test
    public void readFakeIdWillReturnEmpty() {
        Optional<ConnectionProperties> fake = connectionService.getById("fake");
        assertFalse(fake.isPresent());
    }

    @Test
    public void readOkWillReturnDetail() {
        Optional<ConnectionProperties> entity = connectionService.getById("TestH2");

        assertTrue(entity.isPresent());
        assertThat(
                entity.get(),
                allOf(
                        hasProperty("name", equalTo("TestH2")),
                        hasProperty("url", equalTo("jdbc:h2:file:./src/test/resources/test")),
                        hasProperty("username", equalTo("sa")),
                        hasProperty("password", nullValue()),
                        hasProperty("version", equalTo(0L)
                        )
                )
        );
    }

    @Test
    public void deleteFakeReturnFalse() {
        assertFalse(connectionService.delete("fake"));
    }

    @Test
    public void deleteOkReturnTrue() {
        assertTrue(connectionService.delete("TestH2"));
    }

    private Optional<ConnectionProperties> updateToNewTestValue() {
        return connectionService.update(
                new ConnectionProperties(
                        "TestH2",
                        "jdbc:h2:file:./src/test/resources/test",
                        "sa",
                        "newVal",
                        0L
                )
        );
    }
}