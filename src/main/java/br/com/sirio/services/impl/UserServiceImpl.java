	package br.com.sirio.services.impl;

    import br.com.sirio.dtos.UpdateUserDto;
    import br.com.sirio.models.StatusType;
    import br.com.sirio.models.User;
    import br.com.sirio.repositories.UserRepository;
    import br.com.sirio.services.UserService;
    import com.fasterxml.jackson.databind.DeserializationFeature;
    import com.fasterxml.jackson.databind.JsonMappingException;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;

    import java.time.Instant;
    import java.util.List;

    @Service
    @RequiredArgsConstructor
    public class UserServiceImpl implements UserService {
        private final UserRepository userRepository;

        @Override
        public List<User> getUsers() {
            return userRepository.findAll();
        }

        @Override
        public User getUser(String cpf) {
            return userRepository.findByCpf(cpf).orElseThrow(() -> new RuntimeException("Usuário não localizado."));
        }

        @Override
        public User removeUser(String cpf, Long userId) {
            User user = userRepository.findByCpf(cpf).orElseThrow(() -> new RuntimeException("Usuário não localizado."));
            user.setStatus(StatusType.REMOVED);
            user.setDeletedAt(Instant.now());
            user.setDeletedUser(userId);

            return userRepository.save(user);
        }

        @Override
        public User updateUser(String cpf, UpdateUserDto userDto, Long userId) throws JsonMappingException {
            User user = userRepository.findByCpf(cpf).orElseThrow(() -> new RuntimeException("Usuário não localizado."));
            user.setUpadatedAt(Instant.now());
            user.setUpadatedUser(userId);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.updateValue(user, userDto);

            return userRepository.save(user);
        }
    }
