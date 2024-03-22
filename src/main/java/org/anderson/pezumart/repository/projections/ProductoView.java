package org.anderson.pezumart.repository.projections;


import java.time.LocalDate;
import java.util.List;

public interface ProductoView {
    Long getId();
    String getNombre();
    String getDescripcion();
    Categoria getCategoria();
    boolean getDisponible();
    double getPrecio();
    int getCantidadDisponible();
    LocalDate getFechaCreacion();
    Usuario getUsuario();
    List<ImagenProducto> getImagenes();


    interface Usuario {
        String getImagenUrl();
        String getNombreCompleto();
    }

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
