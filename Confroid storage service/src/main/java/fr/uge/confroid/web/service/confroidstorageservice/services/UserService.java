package fr.uge.confroid.web.service.confroidstorageservice.services;

import fr.uge.confroid.web.service.confroidstorageservice.models.User;
import fr.uge.confroid.web.service.confroidstorageservice.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo repository;

    public List<User> findAll() {
        List<User> users = (List<User>) repository.findAll();
        return users;
    }

    public void save(User user) {
        repository.save(user);
    }

    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public boolean authenticate(String username, String password) {
        return repository.findByUsernameAndPassword(username, password) != null;
    }

    public void delete(User user) {
        repository.delete(user);
    }
}
