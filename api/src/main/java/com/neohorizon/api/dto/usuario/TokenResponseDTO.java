package com.neohorizon.api.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseDTO {
    private String token;
    private String type = "Bearer";
    private long expiresIn; // seconds
}
