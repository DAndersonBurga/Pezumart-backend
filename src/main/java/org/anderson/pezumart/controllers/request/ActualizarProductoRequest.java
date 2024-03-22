package org.anderson.pezumart.controllers.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarProductoRequest {
    private Long categoriaId;
    private String nombre;
    private String descripcion;
    private boolean disponible;
    private double precio;
    private int cantidadDisponible;
}
