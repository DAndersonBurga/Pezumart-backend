package org.anderson.pezumart.controllers.response;

import lombok.*;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsuarioActualizadoResponse implements Serializable {
    private String nombreCompleto;
    private String mensaje;
    private String imagenUrl;
}
