package fr.uge.confroid.web.service.confroidstorageservice.repositories;

import fr.uge.confroid.web.service.confroidstorageservice.models.Configuration;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepo extends CrudRepository<Configuration, Long> {

    @Query("select config from Configurations where name=:configName and version=:configVersion")
    Configuration findByNameAndVersion(@Param("configName") String configName, @Param("configVersion") String configVersion);
}
