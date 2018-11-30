package cz.kosina.databasebrowser;

import cz.kosina.databasebrowser.domain.dto.ConnectionProperties;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class RestTestStarter implements StarterOptions {

    protected TestRestTemplate template = new TestRestTemplate();
    @LocalServerPort
    private int port;

    @Before
    public void init() {
        template.delete(createUrlInternal("/connections/TestH2"));
        template.postForEntity(
                createUrlInternal("/connections"),
                new ConnectionProperties(
                        "TestH2",
                        "jdbc:h2:file:./src/test/resources/test",
                        "sa",
                        null,
                        0L
                ),
                ConnectionProperties.class
        );
    }

    private String createUrlInternal(String path) {
        return String.format("http://localhost:%d/%s", port, path);
    }

    protected String createUrl(String path) {
        return createUrlInternal(path);
    }

}
