package br.com.sirio.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateUserDto {
        @NotNull(message = "name.null")
        @NotBlank(message = "name.blank")
        @JsonProperty("name")
        String name;

        @NotNull(message = "zip_code.null")
        @JsonProperty("zip_code")
        Integer zipCode;

        @NotNull(message = "address.null")
        @NotBlank(message = "_address.blank")
        @JsonProperty("address")
        String address;

        @NotNull(message = "number_address.null")
        @JsonProperty("number_address")
        Integer numberAddress;

        @NotNull(message = "additional_address.null")
        @NotBlank(message = "additional_address.blank")
        @JsonProperty("additional_address")
        String additionalAddress;

        @NotNull(message = "district.null")
        @NotBlank(message = "district.blank")
        @JsonProperty("district")
        String district;

        @NotNull(message = "state.null")
        @NotBlank(message = "state.blank")
        @JsonProperty("state")
        String state;
}
