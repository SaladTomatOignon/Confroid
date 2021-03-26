package fr.uge.confroid.web.service.confroidstorageservice.repositories;

import fr.uge.confroid.web.service.confroidstorageservice.models.Configuration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfigRepo extends CrudRepository<Configuration, Long> {

    Optional<Configuration> findByNameAndVersion(String name, int version);

    Optional<Configuration> findByNameAndTag(String name, String tag);

    List<Configuration> findByName(String name);

    void deleteByName(String name);
}
