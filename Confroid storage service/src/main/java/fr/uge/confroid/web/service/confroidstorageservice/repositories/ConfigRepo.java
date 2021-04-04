package fr.uge.confroid.web.service.confroidstorageservice.repositories;

import fr.uge.confroid.web.service.confroidstorageservice.models.Configuration;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConfigRepo extends CrudRepository<Configuration, Long> {

    @Query("SELECT DISTINCT name FROM Configurations")
    List<String> getAllNames();

    Optional<Configuration> findByNameAndVersion(String name, int version);

    Optional<Configuration> findByNameAndTag(String name, String tag);

    List<Configuration> findByNameOrderByVersionAsc(String name);

    void deleteByName(String name);

    void deleteByNameAndVersion(String name, int version);

    boolean existsByNameAndVersion(String name, int version);

    boolean existsByNameAndTag(String name, String tag);

    @Modifying
    @Query("UPDATE " +
                "Configurations c " +
            "SET " +
                "c.config = :configuration, " +
                "c.creationDate = :creationDate, " +
                "c.tag = :tag " +
            "WHERE " +
                "c.name = :name AND " +
                "c.version = :version")
    void updateByVersion(@Param("name") String name,
                         @Param("configuration") String configuration,
                         @Param("creationDate") LocalDateTime creationDate,
                         @Param("version") int version,
                         @Param("tag") String tag);

    @Modifying
    @Query("UPDATE " +
                "Configurations c " +
            "SET " +
                "c.config = :configuration, " +
                "c.creationDate = :creationDate, " +
                "c.version = :version " +
            "WHERE " +
                "c.name = :name AND " +
                "c.tag = :tag")
    void updateByTag(@Param("name") String name,
                     @Param("configuration") String configuration,
                     @Param("creationDate") LocalDateTime creationDate,
                     @Param("version") int version,
                     @Param("tag") String tag);
}
