package fr.uge.confroid.web.service.confroidstorageservice.services;

import fr.uge.confroid.web.service.confroidstorageservice.models.Configuration;
import fr.uge.confroid.web.service.confroidstorageservice.repositories.ConfigRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConfigService {

    @Autowired
    private ConfigRepo repository;

    public List<Configuration> findAll() {
        List<Configuration> configs = (List<Configuration>) repository.findAll();
        return configs;
    }

    public void save(Configuration config) {
        repository.save(config);
    }

    public Optional<Configuration> findById(Long id) {
        return repository.findById(id);
    }

    public Configuration findByNameAndVersion(String name, String version) {
        return repository.findByNameAndVersion(name, version);
    }

    public void delete(Configuration config) {
        repository.delete(config);
    }
}
