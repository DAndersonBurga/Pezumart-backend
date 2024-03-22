package org.anderson.pezumart.controllers.response;

import lombok.*;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductoResponse {
    private Long id;
    private Long categoriaId;
    private String nombre;
    private String descripcion;
    private boolean disponible;
    private double precio;
    private int cantidadDisponible;
    private String fechaCreacion;
    private Map<String, String> autor;
    private List<Map<String, String>> imagenes;
}
