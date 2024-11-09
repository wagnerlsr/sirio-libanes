package br.com.sirio.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record LoginUserDto(
        @NotBlank(message = "CPF não pode ser nulo ou estar em branco")
        @Size(max = 11)
        String cpf,

        @Size(min = 4, max = 20, message = "Senha de ter entre 4 a 20 caracteres.")
        String password
){
}
