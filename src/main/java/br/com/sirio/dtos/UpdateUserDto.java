package br.com.sirio.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateUserDto {
        @Size(max = 50, min = 2, message = "Nome de ter entre 2 a 50 caracteres.")
        @JsonProperty("name")
        public String name;

        @Max(value = 99999999, message = "CEP não pode ter mais do que 8 caracteres.")
        @JsonProperty("zip_code")
        public Integer zipCode;

        @Size(max = 50)
        @JsonProperty("address")
        public String address;

        @Max(value = 999999999, message = "Numero não pode ter mais do que 9 caracteres.")
        @JsonProperty("number_address")
        public Integer numberAddress;

        @JsonProperty("additional_address")
        public String additionalAddress;

        @Size(max = 30)
        @JsonProperty("district")
        public String district;

        @Size(max = 2, min = 2, message = "Estado tem que ter 2 caracteres.")
        @JsonProperty("state")
        public String state;
}
