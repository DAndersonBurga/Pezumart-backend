package org.anderson.pezumart.controllers.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class ActualizarProductoRequest {

    @NotBlank
    @Min(1)
    private Long categoriaId;

    @NotBlank
    @Length(min = 3, max = 30)
    private String nombre;

    @NotBlank
    @Length(max = 300)
    private String descripcion;

    @NotBlank
    private boolean disponible;

    @NotBlank
    @Min(0)
    private double precio;

    @NotBlank
    @Min(0)
    private int cantidadDisponible;
}
