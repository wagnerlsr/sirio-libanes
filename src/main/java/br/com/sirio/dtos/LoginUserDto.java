package br.com.sirio.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record LoginUserDto(
        @NotNull(message = "cpf.empty")
        @NotBlank(message = "cpf.blank")
        String cpf,

        @NotNull(message = "password.empty")
        @NotBlank(message = "password.blank")
        String password
){
}
