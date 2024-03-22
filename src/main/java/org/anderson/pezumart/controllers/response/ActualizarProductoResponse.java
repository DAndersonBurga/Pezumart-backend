package org.anderson.pezumart.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ActualizarProductoResponse {
    private Long id;
    private String nombre;
    private String mensaje;
}
