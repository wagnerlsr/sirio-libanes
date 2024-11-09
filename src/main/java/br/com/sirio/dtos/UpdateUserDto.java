package br.com.sirio.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateUserDto {
        @NotNull(message = "name.null")
        @NotBlank(message = "name.blank")
        @JsonProperty("name")
        public String name;

        @NotNull(message = "zip_code.null")
        @JsonProperty("zip_code")
        public Integer zipCode;

        @NotNull(message = "address.null")
        @NotBlank(message = "_address.blank")
        @JsonProperty("address")
        public String address;

        @NotNull(message = "number_address.null")
        @JsonProperty("number_address")
        public Integer numberAddress;

        @NotNull(message = "additional_address.null")
        @NotBlank(message = "additional_address.blank")
        @JsonProperty("additional_address")
        public String additionalAddress;

        @NotNull(message = "district.null")
        @NotBlank(message = "district.blank")
        @JsonProperty("district")
        public String district;

        @NotNull(message = "state.null")
        @NotBlank(message = "state.blank")
        @JsonProperty("state")
        public String state;
}
