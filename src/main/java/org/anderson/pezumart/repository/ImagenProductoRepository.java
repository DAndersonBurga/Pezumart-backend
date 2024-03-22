package org.anderson.pezumart.repository;

import org.anderson.pezumart.entity.ImagenProducto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImagenProductoRepository extends JpaRepository<ImagenProducto, Long> {
    List<ImagenProducto> findAllByProductoId(Long id);
}
