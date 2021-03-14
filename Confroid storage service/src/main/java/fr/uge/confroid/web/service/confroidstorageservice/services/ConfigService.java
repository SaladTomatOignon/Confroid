package fr.uge.confroid.web.service.confroidstorageservice.services;

import fr.uge.confroid.web.service.confroidstorageservice.models.Configuration;
import fr.uge.confroid.web.service.confroidstorageservice.repositories.ConfigRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Configuration save(Configuration config) {
        return repository.save(config);
    }

    @Transactional
    public Optional<Configuration> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public List<Configuration> findByName(String name) {
        return repository.findByName(name);
    }

    @Transactional
    public Optional<Configuration> findByNameAndVersion(String name, int version) {
        return repository.findByNameAndVersion(name, version);
    }

    @Transactional
    public Optional<Configuration> findByNameAndTag(String name, String tag) {
        return repository.findByNameAndTag(name, tag);
    }

    @Transactional
    public void deleteByName(String name) {
        repository.deleteByName(name);
    }
}
