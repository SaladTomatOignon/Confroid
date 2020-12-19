package fr.uge.confroid.web.service.confroidstorageservice.repositories;

import fr.uge.confroid.web.service.confroidstorageservice.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {

    @Query("select user from Users where username=:name")
    User findByUsername(@Param("name") String username);

    @Query("select user from Users where username=:name and password=:pass")
    User findByUsernameAndPassword(@Param("name") String username, @Param("pass") String password);
}
