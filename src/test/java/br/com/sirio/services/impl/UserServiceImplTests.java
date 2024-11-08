package br.com.sirio.services.impl;

import br.com.sirio.SirioApplication;
import br.com.sirio.dtos.UpdateUserDto;
import br.com.sirio.models.Role;
import br.com.sirio.models.StatusType;
import br.com.sirio.models.User;
import br.com.sirio.repositories.UserRepository;
import br.com.sirio.services.UserService;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SirioApplication.class)
class UserServiceImplTests {

    @MockBean(name="userRepository")
    UserRepository userRepository;
    UserService userService;
    Role roleAdmin;
    Role roleUser;

    List<User> users;
    List<UpdateUserDto> usersDto;

    @BeforeEach
    public void setUp() {
        roleAdmin = Role.builder().id(1L).name(Role.RoleName.ADMIN).build();
        roleUser = Role.builder().id(2L).name(Role.RoleName.USER).build();

        users = Arrays.asList(
                User.builder()
                        .id(1L)
                        .name("Admin")
                        .cpf("11111111111")
                        .password("11111")
                        .createdAt(Instant.now())
                        .status(StatusType.ACTIVE)
                        .roles(List.of(roleAdmin))
                        .build(),
                User.builder()
                        .id(2L)
                        .name("Usuario 2")
                        .cpf("22222222222")
                        .password("22222")
                        .zipCode(2)
                        .address("Rua 2")
                        .numberAddress(2)
                        .additionalAddress("Complemento 2")
                        .district("Cidade 2")
                        .state("SP")
                        .createdAt(Instant.now())
                        .status(StatusType.ACTIVE)
                        .roles(List.of(roleUser))
                        .build(),
                User.builder()
                        .id(1L)
                        .name("Usuario 3")
                        .cpf("33333333333")
                        .password("33333")
                        .createdAt(Instant.now())
                        .status(StatusType.ACTIVE)
                        .roles(List.of(roleUser))
                        .build()
        );

        usersDto = Arrays.asList(
                UpdateUserDto.builder()
                        .name("Usuario 1")
                        .zipCode(1)
                        .address("Rua 1")
                        .numberAddress(1)
                        .additionalAddress("Complemento 1")
                        .district("Cidade 1")
                        .state("SP")
                        .build(),
                UpdateUserDto.builder()
                        .name("Usuario 2")
                        .zipCode(2)
                        .address("Rua 2")
                        .numberAddress(2)
                        .additionalAddress("Complemento 2")
                        .district("Cidade 2")
                        .state("SP")
                        .build(),
                UpdateUserDto.builder()
                        .name("Usuario 3")
                        .zipCode(3)
                        .address("Rua 3")
                        .numberAddress(3)
                        .additionalAddress("Complemento 3")
                        .district("Cidade 3")
                        .state("SP")
                        .build()
        );

        userService = spy(new UserServiceImpl(userRepository));
    }

    @Test
    public void getUsers_Success() {
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getUsers();

        assertThat( result.size() ).isEqualTo(3);
        assertThat( result.containsAll(users) ).isTrue();
    }

    @Test
    public void removeUser_Success() {
        when(userRepository.findByCpf(eq(users.get(1).getCpf()))).thenReturn(Optional.of(users.get(1)));
        when(userRepository.save(any())).thenReturn( users.get(1) );

        User result = userService.removeUser(users.get(1).getCpf(), users.get(1).getId());

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(User.class);
        assertThat(result.getDeletedAt()).isNotNull();
    }

    @Test
    public void updateUser_Success() throws JsonMappingException {
        when(userRepository.findByCpf(eq(users.get(1).getCpf()))).thenReturn(Optional.of(users.get(1)));
        when(userRepository.save(any())).thenReturn( users.get(1) );

        User result = userService.updateUser(users.get(1).getCpf(), usersDto.get(1), 1L);

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(User.class);
        assertThat(result.getAdditionalAddress()).isEqualTo(users.get(1).getAdditionalAddress());
    }

    @Test
    public void updateUser_ExceptionError() throws JsonMappingException {
        when(userRepository.findByCpf(eq(users.get(1).getCpf()))).thenThrow(new RuntimeException("Usuário não localizado."));

        Exception exception = assertThrows(Exception.class, () -> {
            userService.updateUser(users.get(1).getCpf(), usersDto.get(1), 1L);
        });

        assertEquals("Usuário não localizado.", exception.getMessage());
    }
}
