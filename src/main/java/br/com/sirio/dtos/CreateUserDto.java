package br.com.sirio.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateUserDto(
        @Size(max = 11)
        @NotBlank(message = "CPF não pode ser nulo ou estar em branco.")
        @JsonProperty("cpf")
        String cpf,

        @Size(min = 4, max = 20, message = "Senha de ter entre 4 a 20 caracteres.")
        @NotBlank(message = "Senha não pode ser nula ou estar em branco.")
        @JsonProperty("password")
        String password,

        @Size(max = 50, min = 2, message = "Nome de ter entre 2 a 50 caracteres.")
        @NotBlank(message = "Nome não pode ser nulo ou estar em branco.")
        @JsonProperty("name")
        String name,

        @NotNull(message = "CEP não pode ser nulo ou estar em branco.")
        @Max(value = 99999999, message = "CEP não pode ter mais do que 8 caracteres.")
        @JsonProperty("zip_code")
        Integer zipCode,

        @Size(max = 50)
        @NotBlank(message = "Endereço não pode ser nulo ou estar em branco.")
        @JsonProperty("address")
        String address,

        @NotNull(message = "Numero não pode ser nulo ou estar em branco.")
        @Max(value = 999999999, message = "Numero não pode ter mais do que 9 caracteres.")
        @JsonProperty("number_address")
        Integer numberAddress,

        @JsonProperty("additional_address")
        String additionalAddress,

        @Size(max = 30)
        @NotBlank(message = "Bairro não pode ser nulo ou estar em branco.")
        @JsonProperty("district")
        String district,

        @Size(max = 2, min = 2, message = "Estado tem que ter 2 caracteres.")
        @NotBlank(message = "Estado não pode ser nulo ou estar em branco.")
        @JsonProperty("state")
        String state
){
}
