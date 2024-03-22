package org.anderson.pezumart.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class CrearProductoResponse implements Serializable {
    private Long id;
    private String nombreProducto;
    private String mensaje;
}
