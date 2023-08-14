package com.example.demo.configurations;

import com.example.demo.models.entities.RoleEntity;
import com.example.demo.models.entities.UserEntity;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class StartupConfiguration implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        var userExists = userRepository.findById(BigInteger.ONE).isPresent();

        if (!userExists) {
            var roles = new RoleEntity();
            roles.setName("ADMIN");
            roleRepository.save(roles);

            var roleSet = Set.of(roles);

            var user = new UserEntity();
            user.setDeleted(false);
            user.setUsername("test");
            user.setPassword(passwordEncoder.encode("mayfay_2018@M2003"));
            user.setFirstName("peter");
            user.setLastName("diei");
            user.setEmail("peter@cellulant.io");
            user.setPhone("23480540611");
            user.setRoles(roleSet);
            userRepository.save(user);
        }
    }
}
