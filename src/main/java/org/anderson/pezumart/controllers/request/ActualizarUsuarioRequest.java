package org.anderson.pezumart.controllers.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class ActualizarUsuarioRequest {
    @NotBlank
    @Length(max = 60)
    private String nombreCompleto;

    @NotBlank
    @Length(max = 15)
    private String telefono;

    @NotBlank
    @Length(max = 100)
    private String direccion;

    @Length(max = 40)
    private String coordenadas;
}
