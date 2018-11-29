package cz.kosina.databasebrowser.service.impl;

import cz.kosina.databasebrowser.domain.dto.ConnectionProperties;
import cz.kosina.databasebrowser.domain.entity.ConnectionPropertiesEntity;
import cz.kosina.databasebrowser.repository.ConnectionRepository;
import cz.kosina.databasebrowser.service.api.ConnectionService;
import org.apache.commons.lang3.Validate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Implementation of {@link ConnectionService} used for stored {@link ConnectionProperties} into Database
 */
@Service
public class ConnectionServiceImpl implements ConnectionService {

    /**
     * Repository used for database manipulation
     */
    private final ConnectionRepository connectionRepository;
    /**
     * Mapper for Entity/DTO
     */
    private final ModelMapper modelMapper;

    @Autowired
    public ConnectionServiceImpl(ConnectionRepository connectionRepository, ModelMapper modelMapper) {
        this.connectionRepository = Validate.notNull(connectionRepository);
        this.modelMapper = Validate.notNull(modelMapper);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Optional<ConnectionProperties> create(ConnectionProperties connectionProperties) {
        if (!getById(connectionProperties.getName()).isPresent()) {
            ConnectionPropertiesEntity created = connectionRepository.save(
                    mapToEntity(connectionProperties)
            );
            return Optional.of(
                    mapToDTO(created)
            );
        } else {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Optional<ConnectionProperties> update(ConnectionProperties connectionProperties) {
        if (getById(connectionProperties.getName()).isPresent()) {
            ConnectionPropertiesEntity updated = connectionRepository.save(
                    mapToEntity(connectionProperties)
            );
            return Optional.of(
                    mapToDTO(updated)
            );
        } else {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public boolean delete(String name) {
        if (getById(name).isPresent()) {
            connectionRepository.deleteById(name);
            return true;
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ConnectionProperties> getAll() {
        return StreamSupport.stream(
                connectionRepository.findAll().spliterator(),
                false
        ).map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ConnectionProperties> getById(String name) {
        return connectionRepository.findById(name).map(this::mapToDTO);
    }

    /**
     * Map entity object into DTO object
     *
     * @param entity entity object to by mapped
     * @return new instance of DTO mapped from entity object
     */
    private ConnectionProperties mapToDTO(ConnectionPropertiesEntity entity) {
        return modelMapper.map(entity, ConnectionProperties.class);
    }

    /**
     * Map DTO object into Entity object
     *
     * @param dto original DTO object to be mapped
     * @return new instance of entity object
     */
    private ConnectionPropertiesEntity mapToEntity(ConnectionProperties dto) {
        return modelMapper.map(dto, ConnectionPropertiesEntity.class);
    }
}
