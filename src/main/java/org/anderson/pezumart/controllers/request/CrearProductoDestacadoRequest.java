package org.anderson.pezumart.controllers.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearProductoDestacadoRequest {

    @NotNull
    @Min(1)
    private Long productoId;
}
