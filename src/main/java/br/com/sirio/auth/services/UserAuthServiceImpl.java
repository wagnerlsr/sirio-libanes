package br.com.sirio.auth.services;

import br.com.sirio.auth.JwtTokenService;
import br.com.sirio.auth.UserDetailsImpl;
import br.com.sirio.config.SecurityConfiguration;
import br.com.sirio.dtos.CreateUserDto;
import br.com.sirio.dtos.LoginUserDto;
import br.com.sirio.dtos.RecoveryJwtTokenDto;
import br.com.sirio.models.Role;
import br.com.sirio.models.StatusType;
import br.com.sirio.models.User;
import br.com.sirio.repositories.RoleRepository;
import br.com.sirio.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import static br.com.sirio.models.Role.RoleName.USER;

@Service
@RequiredArgsConstructor
public class UserAuthServiceImpl {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final SecurityConfiguration securityConfiguration;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public RecoveryJwtTokenDto authenticateUser(LoginUserDto loginUserDto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDto.cpf(), loginUserDto.password());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new RecoveryJwtTokenDto(jwtTokenService.generateToken(userDetails));
    }

    public User createUser(CreateUserDto createUserDto, Authentication authentication) {
        Role roleUser = roleRepository.findByName(USER).orElseThrow(() -> new RuntimeException("Role USER não encontrada."));

        User newUser = User.builder()
                .cpf(createUserDto.cpf())
                .name(createUserDto.name())
                .address(createUserDto.address())
                .numberAddress(createUserDto.numberAddress())
                .additionalAddress(createUserDto.additionalAddress())
                .district(createUserDto.district())
                .state(createUserDto.state())
                .zipCode(createUserDto.zipCode())
                .status(StatusType.ACTIVE)
                .createdAt(Instant.now())
                .createdUser(getCurrentUserId(authentication))
                .password(securityConfiguration.passwordEncoder().encode(createUserDto.password()))
                .roles(List.of(roleUser))
                .build();

        return userRepository.save(newUser);
    }

    public Long getCurrentUserId(Authentication authentication) {
        User user = userRepository.findByCpf(authentication.getPrincipal().toString()).orElseThrow(() -> new RuntimeException("Usuário não logado."));
        return user.getId();
    }

    public boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(ga -> ga.getAuthority().equals("ADMIN"));
    }

    public boolean hasAccess(Authentication authentication, String cpf) {
        var isAdmin = isAdmin(authentication);
        var isUser = cpf.equals(authentication.getPrincipal());

        return isAdmin || isUser;
    }
}
