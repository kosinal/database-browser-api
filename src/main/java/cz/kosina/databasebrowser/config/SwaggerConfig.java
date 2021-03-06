package cz.kosina.databasebrowser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuration of Swagger endpoint documentation
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("cz.kosina.databasebrowser.controller"))
                .paths(PathSelectors.any())
                .build()
                .directModelSubstitute(ResponseEntity.class, String.class)
                .tags(
                        new Tag("Connections", "Operations for storing and retrieving database connections."),
                        new Tag("Databases", "Operations for getting information about objects stored in database")
                );
    }

}