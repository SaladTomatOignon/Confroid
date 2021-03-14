package fr.uge.confroid.web.service.confroidstorageservice.repositories;

import fr.uge.confroid.web.service.confroidstorageservice.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
