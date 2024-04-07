package org.anderson.pezumart.repository;

import org.anderson.pezumart.entity.Producto;
import org.anderson.pezumart.repository.projections.MiProductoView;
import org.anderson.pezumart.repository.projections.ProductoView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Page<ProductoView> findAllProjectedBy(Pageable pageable);
    Page<ProductoView> findAllByNombreContainingIgnoreCase(Pageable pageable, String nombre);
    Page<MiProductoView> findAllByUsuarioId(Pageable pageable, Long id);
    List<Producto> findAllByUsuarioId(Long id);
    List<ProductoView> findFirst8ByOrderByFechaCreacionDesc();
}
