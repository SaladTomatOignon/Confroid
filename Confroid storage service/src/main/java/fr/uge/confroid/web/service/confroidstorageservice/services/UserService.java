package fr.uge.confroid.web.service.confroidstorageservice.services;

import fr.uge.confroid.web.service.confroidstorageservice.models.User;
import fr.uge.confroid.web.service.confroidstorageservice.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo repository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return (List<User>) repository.findAll();
    }

    public void save(User user) {
        repository.save(new User(user.getUsername(), passwordEncoder.encode(user.getPassword())));
    }

    public Optional<User> getByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repository.findByUsername(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User '" + username + "' not found");
        }

        return user.get();
    }

    public void login(String username, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
    }
}
