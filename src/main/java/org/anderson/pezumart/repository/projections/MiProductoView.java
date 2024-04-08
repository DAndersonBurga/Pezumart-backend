package org.anderson.pezumart.repository.projections;

import java.time.LocalDateTime;
import java.util.List;

public interface MiProductoView  {
    Long getId();
    String getNombre();
    String getDescripcion();
    ProductoView.Categoria getCategoria();
    boolean getDisponible();
    double getPrecio();
    int getCantidadDisponible();
    LocalDateTime getFechaCreacion();
    List<ImagenProducto> getImagenes();


    interface Categoria {
        Long getId();
        String getNombre();
    }

    interface ImagenProducto {
        Long getId();
        String getNombreImagen();
        String getImagenUrl();
    }
}
