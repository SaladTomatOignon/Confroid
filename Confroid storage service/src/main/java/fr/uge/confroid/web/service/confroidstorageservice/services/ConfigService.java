package fr.uge.confroid.web.service.confroidstorageservice.services;

import fr.uge.confroid.web.service.confroidstorageservice.models.Configuration;
import fr.uge.confroid.web.service.confroidstorageservice.repositories.ConfigRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Transactional
@Service
public class ConfigService {

    @Autowired
    private ConfigRepo repository;

    public List<Configuration> findAll() {
        return (List<Configuration>) repository.findAll();
    }

    public Configuration saveOrUpdate(Configuration config) {
        if (repository.existsByNameAndVersion(config.getName(), config.getVersion())) {
            repository.updateByVersion(config.getName(), config.getConfig(), config.getCreationDate(), config.getVersion(), config.getTag());
        } else if (!Objects.isNull(config.getTag()) && repository.existsByNameAndTag(config.getName(), config.getTag())) {
            repository.updateByTag(config.getName(), config.getConfig(), config.getCreationDate(), config.getVersion(), config.getTag());
        } else {
            return repository.save(config);
        }

        return null;
    }

    public Optional<Configuration> findById(Long id) {
        return repository.findById(id);
    }

    public List<Configuration> findByName(String name) {
        return repository.findByName(name);
    }

    public Optional<Configuration> findByNameAndVersion(String name, int version) {
        return repository.findByNameAndVersion(name, version);
    }

    public Optional<Configuration> findByNameAndTag(String name, String tag) {
        return repository.findByNameAndTag(name, tag);
    }

    public void deleteByName(String name) {
        repository.deleteByName(name);
    }

    public void deleteByNameAndVersion(String name, int version) {
        repository.deleteByNameAndVersion(name, version);
    }
}
