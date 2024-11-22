package com.ideas2it.sample.helper;

import com.ideas2it.sample.model.Role;
import com.ideas2it.sample.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoleInitializer {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void initRoles() {
        createRoleIfNotExists("ADMIN");
        createRoleIfNotExists("USER");
    }

    private void createRoleIfNotExists(String roleName) {
        Optional<Role> role = roleRepository.findByName(roleName);
        if (role.isEmpty()) {
            Role newRole = new Role();
            newRole.setName(roleName);
            roleRepository.save(newRole);
        }
    }
}
