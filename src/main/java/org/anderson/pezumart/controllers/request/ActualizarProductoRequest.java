package org.anderson.pezumart.controllers.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class ActualizarProductoRequest {

    @NotNull
    @Min(1)
    private Long categoriaId;

    @NotBlank
    @Length(min = 3, max = 30)
    private String nombre;

    @NotBlank
    @Length(max = 300)
    private String descripcion;

    @NotNull
    private boolean disponible;

    @NotNull
    @Min(0)
    private double precio;

    @NotNull
    @Min(0)
    private int cantidadDisponible;
}
