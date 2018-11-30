package cz.kosina.databasebrowser;

import cz.kosina.databasebrowser.domain.dto.ConnectionProperties;
import cz.kosina.databasebrowser.service.api.ConnectionService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class DatabaseTestStarter implements StarterOptions {


    @Autowired
    private ConnectionService connectionService;

    @Before
    public void init() {
        connectionService.create(
                new ConnectionProperties(
                        "TestH2",
                        "jdbc:h2:file:./src/test/resources/test",
                        "sa",
                        null,
                        0L
                )
        );
    }

}
