package org.anderson.pezumart.controllers.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CrearProductoRequest {

    @Min(1)
    @NotNull
    private Long categoriaId;

    @NotBlank
    @Length(min = 3, max = 30)
    private String nombre;

    @Length(min = 3, max = 300)
    private String descripcion;

    @NotNull
    private boolean disponible;

    @Min(1)
    @NotNull
    private double precio;

    @Min(0)
    @NotNull
    private int cantidadDisponible;
}
