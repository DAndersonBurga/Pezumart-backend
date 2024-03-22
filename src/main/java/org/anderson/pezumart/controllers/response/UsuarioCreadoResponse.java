package org.anderson.pezumart.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UsuarioCreadoResponse {
    private Long id;
    private String username;
    private String mensaje;
}
