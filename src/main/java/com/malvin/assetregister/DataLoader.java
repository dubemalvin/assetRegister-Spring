package com.malvin.assetregister;

import com.malvin.assetregister.entity.User;
import com.malvin.assetregister.enums.Role;
import com.malvin.assetregister.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) {
        addSampleUsers();
    }

    private void addSampleUsers() {
        User admin = new User();
        admin.setFirstName("Test1");
        admin.setLastName("Test1");
        admin.setEmail("admin@email.com");
        admin.setPassword(passwordEncoder.encode("root"));
        admin.setRole(Role.valueOf(Role.ADMIN.name()));

        User user = new User();
        user.setFirstName("Test1");
        user.setLastName("Test1");
        user.setEmail("user@email.com");
        user.setPassword(passwordEncoder.encode("root"));
        user.setRole(Role.valueOf(Role.USER.name()));
        if(!userRepository.existsByEmail(user.getEmail())){
            userRepository.save(admin);
            userRepository.save(user);
        }

    }
}
