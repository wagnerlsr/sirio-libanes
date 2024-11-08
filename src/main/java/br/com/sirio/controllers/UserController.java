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
@RequiredArgsConstructor
public class UserController {
	private final UserAuthServiceImpl userAuthService;
	private final UserService userService;

	@PostMapping("/login")
	public ResponseEntity<RecoveryJwtTokenDto> authenticateUser(@RequestBody LoginUserDto loginUserDto) {
		RecoveryJwtTokenDto token = userAuthService.authenticateUser(loginUserDto);
		return new ResponseEntity<>(token, HttpStatus.OK);
	}

	@Operation(summary = "Create user", description = "Create user")
	@SecurityRequirement(name = "Bearer Authentication")
	@PostMapping("/signup")
	public ResponseEntity<User> createUser(@Valid @RequestBody CreateUserDto createUserDto, Authentication authentication) {
		if (userAuthService.isAdmin(authentication)) {
			return ResponseEntity.status(HttpStatus.OK).body(userAuthService.createUser(createUserDto, authentication));
		}

		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	@Operation(summary = "List users", description = "List users")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<User>> getUsers(Authentication authentication) {
		if (userAuthService.isAdmin(authentication)) {
			return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers());
		}

		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	@Operation(summary = "Get user", description = "Get user by cpf")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping(value = "/users/{cpf}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> getUser(@PathVariable String cpf, Authentication authentication) {
		if (userAuthService.hasAccess(authentication, cpf)) {
			return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(cpf));
		}

		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	@Operation(summary = "Update user", description = "Update user")
	@SecurityRequirement(name = "Bearer Authentication")
	@PatchMapping(value = "/users/{cpf}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> updateUser(@PathVariable String cpf, @Valid @RequestBody UpdateUserDto updateUserDto, Authentication authentication) throws JsonMappingException {
		if (userAuthService.hasAccess(authentication, cpf)) {
			return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(cpf, updateUserDto, userAuthService.getCurrentUserId(authentication)));
		}

		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	@Operation(summary = "Remove user", description = "Remove user")
	@SecurityRequirement(name = "Bearer Authentication")
	@DeleteMapping(value = "/users/{cpf}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> removeUser(@PathVariable String cpf, Authentication authentication) {
		if (userAuthService.isAdmin(authentication)) {
			userService.removeUser(cpf, userAuthService.getCurrentUserId(authentication));
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}
}
