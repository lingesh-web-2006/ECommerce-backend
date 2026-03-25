package com.ecommerce.config;
import com.ecommerce.entity.User;
import com.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
public class DataInitializer {
@Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
   @Bean
     public CommandLineRunner initDatabase() {
        return args -> {
            User existingUser = userRepository.findByUsername("lingesh");
            if (existingUser == null) {
                User user = new User();
                user.setUsername("lingesh");
                user.setPassword(passwordEncoder.encode("123"));
                user.setRole("USER");
                userRepository.save(user);
                System.out.println("✓ Test user 'lingesh' created successfully with password '123'");
            } else {
                System.out.println("✓ Test user 'lingesh' already exists");
            }
        };
    }
}
