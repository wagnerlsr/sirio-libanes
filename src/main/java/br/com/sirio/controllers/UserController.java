package br.com.sirio.controllers;

import br.com.sirio.auth.services.UserAuthServiceImpl;
import br.com.sirio.dtos.CreateUserDto;
import br.com.sirio.dtos.LoginUserDto;
import br.com.sirio.dtos.RecoveryJwtTokenDto;
import br.com.sirio.dtos.UpdateUserDto;
import br.com.sirio.models.User;
import br.com.sirio.services.UserService;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RestControllerAdvice
@RequiredArgsConstructor
public class UserController {
	private final UserAuthServiceImpl userAuthService;
	private final UserService userService;

	@Operation(summary = "Login", description = "Recuperação do token de acesso")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuário localizado", content = { @Content(mediaType = "application/json",
					schema = @Schema(implementation = RecoveryJwtTokenDto.class)) }),
			@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
	})
	@PostMapping("/login")
	public ResponseEntity<RecoveryJwtTokenDto> authenticateUser(@Valid @RequestBody LoginUserDto loginUserDto) {
		RecoveryJwtTokenDto token = userAuthService.authenticateUser(loginUserDto);
		return new ResponseEntity<>(token, HttpStatus.OK);
	}

	@Operation(summary = "Criação de usuário", description = "Acesso apenas para administradores")
	@SecurityRequirement(name = "Bearer Authentication")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Usuário criado", content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = User.class)) }),
			@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
			@ApiResponse(responseCode = "401", description = "Usuário não autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
	})
	@PostMapping("/signup")
	public ResponseEntity<User> createUser(@Valid @RequestBody CreateUserDto createUserDto, Authentication authentication) {
		if (userAuthService.isAdmin(authentication)) {
			return ResponseEntity.status(HttpStatus.CREATED).body(userAuthService.createUser(createUserDto, authentication));
		}

		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	@Operation(summary = "Listar todos usuários", description = "Acesso apenas para administradores")
	@SecurityRequirement(name = "Bearer Authentication")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuários recuperados", content = { @Content(mediaType = "application/json",
					schema = @Schema(implementation = User.class)) }),
			@ApiResponse(responseCode = "401", description = "Usuário não autorizado", content = @Content),
	})
	@GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<User>> getUsers(Authentication authentication) {
		if (userAuthService.isAdmin(authentication)) {
			return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers());
		}

		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	@Operation(summary = "Obter um usuário", description = "Acessar dados do usuário")
	@SecurityRequirement(name = "Bearer Authentication")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuário recuperado", content = { @Content(mediaType = "application/json",
					schema = @Schema(implementation = User.class)) }),
			@ApiResponse(responseCode = "401", description = "Usuário não autorizado", content = @Content),
	})
	@GetMapping(value = "/users/{cpf}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> getUser(@PathVariable String cpf, Authentication authentication) {
		if (userAuthService.hasAccess(authentication, cpf)) {
			return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(cpf));
		}

		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	@Operation(summary = "Atualizar usuário", description = "Atualizar os dados do usário")
	@SecurityRequirement(name = "Bearer Authentication")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuário atualizado", content = { @Content(mediaType = "application/json",
					schema = @Schema(implementation = User.class)) }),
			@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
			@ApiResponse(responseCode = "401", description = "Usuário não autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
	})
	@PatchMapping(value = "/users/{cpf}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> updateUser(@PathVariable String cpf, @Valid @RequestBody UpdateUserDto updateUserDto, Authentication authentication) throws JsonMappingException {
		if (userAuthService.hasAccess(authentication, cpf)) {
			return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(cpf, updateUserDto, userAuthService.getCurrentUserId(authentication)));
		}

		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	@Operation(summary = "Remover usuário", description = "Marcar usário como removido")
	@SecurityRequirement(name = "Bearer Authentication")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Usuário marcado como removido", content = { @Content(mediaType = "application/json",
					schema = @Schema(implementation = User.class)) }),
			@ApiResponse(responseCode = "401", description = "Usuário não autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
	})
	@DeleteMapping(value = "/users/{cpf}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> removeUser(@PathVariable String cpf, Authentication authentication) {
		if (userAuthService.isAdmin(authentication)) {
			userService.removeUser(cpf, userAuthService.getCurrentUserId(authentication));
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}
}
