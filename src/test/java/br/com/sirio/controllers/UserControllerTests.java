package br.com.sirio.controllers;

import br.com.sirio.dtos.CreateUserDto;
import br.com.sirio.dtos.RecoveryJwtTokenDto;
import br.com.sirio.dtos.UpdateUserDto;
import br.com.sirio.models.StatusType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserControllerTests {
    MockMvc mvc;

    ObjectMapper objectMapper;
    String userRequest;
    String adminRequest;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

        adminRequest = """
                {
                    "cpf": "99999999999",
                    "password": "admin"
                }
                """;

        userRequest = """
                {
                    "cpf": "11111111111",
                    "password": "user"
                }
                """;

        objectMapper = new ObjectMapper();
    }

    @Test
    void login_success() throws Exception {
        mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(adminRequest))
                .andExpect(status().isOk());
    }

    @Test
    void signup_success() throws Exception {
        CreateUserDto userDto = CreateUserDto.builder()
                .cpf("2222")
                .password("user")
                .name("User 1")
                .address("Rua 1")
                .numberAddress(1)
                .additionalAddress("aa")
                .district("aa")
                .state("aa")
                .zipCode(1)
                .build();

        MvcResult result = mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(adminRequest))
                .andExpect(status().isOk()).andReturn();

        String json = result.getResponse().getContentAsString();
        RecoveryJwtTokenDto jwtToken = objectMapper.readValue(json, RecoveryJwtTokenDto.class);

        mvc.perform(post("/signup")
                        .header(HttpHeaders.AUTHORIZATION, jwtToken.token())
                        .content(asJsonString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value(userDto.cpf()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(userDto.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(StatusType.ACTIVE.name()));
    }

    @Test
    void signup_userDuplicate() throws Exception {
        CreateUserDto userDto = CreateUserDto.builder()
                .cpf("3333")
                .password("user")
                .name("User 1")
                .address("Rua 1")
                .numberAddress(1)
                .additionalAddress("aa")
                .district("aa")
                .state("aa")
                .zipCode(1)
                .build();

        MvcResult result = mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(adminRequest))
                .andExpect(status().isOk()).andReturn();

        String json = result.getResponse().getContentAsString();
        RecoveryJwtTokenDto jwtToken = objectMapper.readValue(json, RecoveryJwtTokenDto.class);

        mvc.perform(post("/signup")
                        .header(HttpHeaders.AUTHORIZATION, jwtToken.token())
                        .content(asJsonString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value(userDto.cpf()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(userDto.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(StatusType.ACTIVE.name()));

        mvc.perform(post("/signup")
                        .header(HttpHeaders.AUTHORIZATION, jwtToken.token())
                        .content(asJsonString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUsers_success() throws Exception {
        MvcResult result = mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(adminRequest))
                .andExpect(status().isOk()).andReturn();

        String json = result.getResponse().getContentAsString();
        RecoveryJwtTokenDto jwtToken = objectMapper.readValue(json, RecoveryJwtTokenDto.class);

        mvc.perform(get("/users")
                        .header(HttpHeaders.AUTHORIZATION, jwtToken.token())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect( jsonPath("$").isArray());
    }

    @Test
    void getUser_success() throws Exception {
        CreateUserDto userDto = CreateUserDto.builder()
                .cpf("11111111111")
                .password("user")
                .name("User 4")
                .address("Rua 4")
                .numberAddress(1)
                .additionalAddress("aa")
                .district("aa")
                .state("aa")
                .zipCode(1)
                .build();

        MvcResult result = mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(adminRequest))
                .andExpect(status().isOk()).andReturn();

        String json = result.getResponse().getContentAsString();
        RecoveryJwtTokenDto jwtToken = objectMapper.readValue(json, RecoveryJwtTokenDto.class);

        mvc.perform(post("/signup")
                        .header(HttpHeaders.AUTHORIZATION, jwtToken.token())
                        .content(asJsonString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value(userDto.cpf()));

        result = mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(userRequest))
                .andExpect(status().isOk()).andReturn();

        json = result.getResponse().getContentAsString();
        jwtToken = objectMapper.readValue(json, RecoveryJwtTokenDto.class);

        mvc.perform(get("/users/" + userDto.cpf())
                        .header(HttpHeaders.AUTHORIZATION, jwtToken.token())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect( jsonPath("$.cpf", equalTo(userDto.cpf())))
                .andExpect( jsonPath("$.name", equalTo(userDto.name())));

        mvc.perform(get("/users/99999999999")
                        .header(HttpHeaders.AUTHORIZATION, jwtToken.token())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void removeUser_success() throws Exception {
        CreateUserDto userDto = CreateUserDto.builder()
                .cpf("22222222222")
                .password("user")
                .name("User 2")
                .address("Rua 2")
                .numberAddress(1)
                .additionalAddress("aa")
                .district("aa")
                .state("aa")
                .zipCode(1)
                .build();

        MvcResult result = mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(adminRequest))
                .andExpect(status().isOk()).andReturn();

        String json = result.getResponse().getContentAsString();
        RecoveryJwtTokenDto jwtToken = objectMapper.readValue(json, RecoveryJwtTokenDto.class);

        mvc.perform(post("/signup")
                        .header(HttpHeaders.AUTHORIZATION, jwtToken.token())
                        .content(asJsonString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value(userDto.cpf()));

        mvc.perform(delete("/users/" + userDto.cpf())
                        .header(HttpHeaders.AUTHORIZATION, jwtToken.token())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mvc.perform(get("/users/" + userDto.cpf())
                        .header(HttpHeaders.AUTHORIZATION, jwtToken.token())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect( jsonPath("$.cpf", equalTo(userDto.cpf())))
                .andExpect( jsonPath("$.status", equalTo(StatusType.REMOVED.name())));
    }

    @Test
    void updateUser_success() throws Exception {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .cpf("33333333333")
                .password("user")
                .name("User 3")
                .address("Rua 3")
                .numberAddress(1)
                .additionalAddress("aa")
                .district("aa")
                .state("aa")
                .zipCode(1)
                .build();

        UpdateUserDto updateUserDto = UpdateUserDto.builder()
                .name("User 333")
                .address("Rua 333")
                .numberAddress(10)
                .additionalAddress("aa")
                .district("aa")
                .state("aa")
                .zipCode(1)
                .build();

        MvcResult result = mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(adminRequest))
                .andExpect(status().isOk()).andReturn();

        String json = result.getResponse().getContentAsString();
        RecoveryJwtTokenDto jwtToken = objectMapper.readValue(json, RecoveryJwtTokenDto.class);

        mvc.perform(post("/signup")
                        .header(HttpHeaders.AUTHORIZATION, jwtToken.token())
                        .content(asJsonString(createUserDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value(createUserDto.cpf()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(createUserDto.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value(createUserDto.address()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.upadatedAt").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.upadatedUser").isEmpty());

        mvc.perform(patch("/users/" + createUserDto.cpf())
                        .header(HttpHeaders.AUTHORIZATION, jwtToken.token())
                        .content(asJsonString(updateUserDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect( jsonPath("$.cpf", equalTo(createUserDto.cpf())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(updateUserDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value(updateUserDto.getAddress()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.upadatedAt").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.upadatedUser").isNotEmpty());
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
