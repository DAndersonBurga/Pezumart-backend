package org.anderson.pezumart.controllers.api;

import jakarta.validation.Valid;
import org.anderson.pezumart.controllers.request.ActualizarProductoRequest;
import org.anderson.pezumart.controllers.request.CrearProductoDestacadoRequest;
import org.anderson.pezumart.controllers.request.CrearProductoRequest;
import org.anderson.pezumart.controllers.response.ActualizarProductoResponse;
import org.anderson.pezumart.controllers.response.CrearProductoResponse;
import org.anderson.pezumart.controllers.response.ProductoEliminadoResponse;
import org.anderson.pezumart.controllers.response.ProductoResponse;
import org.anderson.pezumart.entity.ProductoDestacado;
import org.anderson.pezumart.repository.ProductoDestacadoRepository;
import org.anderson.pezumart.repository.ProductoRepository;
import org.anderson.pezumart.repository.projections.MiProductoView;
import org.anderson.pezumart.repository.projections.ProductoView;
import org.anderson.pezumart.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoDestacadoRepository productoDestacadoRepository;

    @GetMapping("/listar")
    public ResponseEntity<Page<ProductoView>> listarProductos(Pageable pageable) {
        Page<ProductoView> productos = productoRepository.findAllProjectedBy(pageable);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/listar/mis-productos")
    public ResponseEntity<Page<MiProductoView>> listarProductosDelUsuarioAutenticado(Pageable pageable) {
        Page<MiProductoView> productos = productoService.buscarProductosDelUsuarioAutenticado(pageable);

        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtenerProductoPorId(@PathVariable Long id) {
        ProductoResponse productoResponse = productoService.obtenerProductoPorId(id);
        return ResponseEntity.ok(productoResponse);
    }

    @GetMapping("/ultimos")
    public ResponseEntity<List<ProductoView>> listarUltimosProductos() {
        List<ProductoView> productos = productoService.obtenerUltimos8Productos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/destacados")
    public ResponseEntity<List<ProductoDestacado>> listarProductosDestacados() {
        List<ProductoDestacado> productos = productoDestacadoRepository.findAll();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/categoria/{id}")
    public ResponseEntity<Page<ProductoView>> listarProductosPorCategoria(Pageable pageable, @PathVariable Long id) {
        Page<ProductoView> productos = productoService.obtenerProductosPorCategoria(pageable, id);

        return ResponseEntity.ok(productos);
    }

    @PostMapping("/destacar")
    public ResponseEntity<Map<String, String>> destacarProducto(@Valid @RequestBody CrearProductoDestacadoRequest crearProductoDestacadoRequest) {
        String mensaje = productoService.descatarProducto(crearProductoDestacadoRequest.getProductoId());

        return ResponseEntity.ok(Map.of("mensaje", mensaje));
    }

    @DeleteMapping("/destacado/eliminar/{id}")
    public ResponseEntity<Map<String, String>> eliminarProductoDestacado(@PathVariable Long id) {
       String mensaje = productoService.eliminarProductoDestacado(id);

        return ResponseEntity.ok(Map.of("mensaje", mensaje));
    }

    @PostMapping("/crear")
    public ResponseEntity<CrearProductoResponse> crearProducto(@RequestPart("producto") @Valid CrearProductoRequest crearProductoRequest,
                                           @RequestPart("files") List<MultipartFile> files) {

        CrearProductoResponse productoResponse = productoService.crearProducto(crearProductoRequest, files);


        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productoResponse.getId())
                .toUri();

        return ResponseEntity.created(location).body(productoResponse);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ActualizarProductoResponse> actualizarProducto(@RequestBody @Valid ActualizarProductoRequest actualizarProductoRequest,
                                                @PathVariable Long id) {
        ActualizarProductoResponse actualizarProductoResponse = productoService.actualizarProducto(id, actualizarProductoRequest);
        return ResponseEntity.ok(actualizarProductoResponse);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<ProductoEliminadoResponse> eliminarProducto(@PathVariable Long id) {
        ProductoEliminadoResponse productoEliminadoResponse = productoService.eliminarProducto(id);

        return ResponseEntity.ok(productoEliminadoResponse);
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<ProductoView>> buscarProductoPorNombre(Pageable pageable, @RequestParam String nombre) {
        Page<ProductoView> productoViews = productoService.buscarProductoPorNombre(pageable, nombre);

        return ResponseEntity.ok(productoViews);
    }
}