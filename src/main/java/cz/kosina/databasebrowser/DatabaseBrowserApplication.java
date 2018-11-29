package cz.kosina.databasebrowser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class DatabaseBrowserApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatabaseBrowserApplication.class, args);
    }
}
