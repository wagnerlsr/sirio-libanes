package br.com.sirio;

import br.com.sirio.config.SecurityConfiguration;
import br.com.sirio.models.Role;
import br.com.sirio.models.StatusType;
import br.com.sirio.models.User;
import br.com.sirio.repositories.RoleRepository;
import br.com.sirio.repositories.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Instant;
import java.util.List;

import static br.com.sirio.models.Role.RoleName.ADMIN;
import static br.com.sirio.models.Role.RoleName.USER;

@SpringBootApplication
public class SirioApplication implements ApplicationRunner {
    private final SecurityConfiguration securityConfiguration;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public SirioApplication(SecurityConfiguration securityConfiguration, RoleRepository roleRepository, UserRepository userRepository) {
        this.securityConfiguration = securityConfiguration;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(SirioApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Role roleAdmin = roleRepository.findByName(ADMIN).orElse(null);
        if (roleAdmin == null) {
            roleAdmin = Role.builder().name(ADMIN).build();
            roleRepository.save(roleAdmin);
        }

        Role roleUser = roleRepository.findByName(USER).orElse(null);
        if (roleUser == null) {
            roleUser = Role.builder().name(USER).build();
            roleRepository.save(roleUser);
        }

        User user = userRepository.findByCpf("99999999999").orElse(null);
        if (user == null) {
            user = User.builder()
                    .cpf("99999999999")
                    .name("Admin")
                    .status(StatusType.ACTIVE)
                    .createdAt(Instant.now())
                    .password(securityConfiguration.passwordEncoder().encode("admin"))
                    .roles(List.of(roleAdmin))
                    .build();
            userRepository.save(user);
        }
    }
}
