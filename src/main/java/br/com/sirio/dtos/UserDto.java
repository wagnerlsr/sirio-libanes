package br.com.sirio.dtos;

import br.com.sirio.models.StatusType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserDto {
	@NotNull(message = "cpf.empty")
	@NotBlank(message = "cpf.blank")
	private String cpf;

	@NotNull(message = "password.empty")
	@NotBlank(message = "password.blank")
	private String password;

	@NotNull(message = "name.empty")
	@NotBlank(message = "name.blank")
	private String name;

	@NotNull(message = "status.empty")
	private StatusType status;
}
