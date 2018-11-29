package cz.kosina.databasebrowser.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for mapper from Entity object into DTO
 */
@Configuration
public class MappingConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
