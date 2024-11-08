package br.com.sirio.dtos;

import lombok.Builder;

@Builder
public record RecoveryJwtTokenDto(
        String token
) {
}
