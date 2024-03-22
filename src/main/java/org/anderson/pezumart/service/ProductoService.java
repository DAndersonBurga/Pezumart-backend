package org.anderson.pezumart.service;

import org.anderson.pezumart.controllers.request.ActualizarProductoRequest;
import org.anderson.pezumart.controllers.request.CrearProductoRequest;
import org.anderson.pezumart.controllers.response.ActualizarProductoResponse;
import org.anderson.pezumart.controllers.response.CrearProductoResponse;
import org.anderson.pezumart.controllers.response.ProductoEliminadoResponse;
import org.anderson.pezumart.controllers.response.ProductoResponse;
import org.anderson.pezumart.repository.projections.MiProductoView;
import org.anderson.pezumart.repository.projections.ProductoView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductoService {
    ProductoResponse obtenerProductoPorId(Long id);
    CrearProductoResponse crearProducto(CrearProductoRequest crearProductoRequest, List<MultipartFile> imagenes);
    // Actualizar producto
    ActualizarProductoResponse actualizarProducto(Long id, ActualizarProductoRequest actualizarProductoRequest);
    // Eliminar producto
    ProductoEliminadoResponse eliminarProducto(Long id);
    // Buscar producto por nombre
    Page<ProductoView> buscarProductoPorNombre(Pageable pageable, String nombre);
    Page<MiProductoView> buscarProductosDelUsuarioAutenticado(Pageable pageable);
}
