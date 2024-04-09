package org.anderson.pezumart.service.impl;

import org.anderson.pezumart.controllers.request.ActualizarProductoRequest;
import org.anderson.pezumart.controllers.request.CrearProductoRequest;
import org.anderson.pezumart.controllers.response.ActualizarProductoResponse;
import org.anderson.pezumart.controllers.response.CrearProductoResponse;
import org.anderson.pezumart.controllers.response.ProductoEliminadoResponse;
import org.anderson.pezumart.controllers.response.ProductoResponse;
import org.anderson.pezumart.entity.*;
import org.anderson.pezumart.entity.enums.ERol;
import org.anderson.pezumart.exceptions.*;
import org.anderson.pezumart.repository.*;
import org.anderson.pezumart.repository.projections.MiProductoView;
import org.anderson.pezumart.repository.projections.ProductoView;
import org.anderson.pezumart.service.CloudinaryService;
import org.anderson.pezumart.service.ProductoService;
import org.anderson.pezumart.service.UsuarioService;
import org.anderson.pezumart.utils.auth.UsuarioAutenticadoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ImagenProductoRepository imagenProductoRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoDestacadoRepository productoDestacadoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public ProductoResponse obtenerProductoPorId(Long id) {

        if(id == null || id < 0) throw new ProductoNotFoundException("El id del producto no puede ser nulo");

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado"));

        List<ImagenProducto> imagenProductoList = imagenProductoRepository.findAllByProductoId(producto.getId());

        List<Map<String, String>> imagenesProducto = imagenProductoList.stream().map(imagenProducto -> Map.of(
                "id", imagenProducto.getId().toString(),
                "nombreImagen", imagenProducto.getNombreImagen(),
                "url", imagenProducto.getImagenUrl()
        )).toList();

        return ProductoResponse.builder()
                .id(producto.getId())
                .categoriaId(producto.getCategoria().getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .disponible(producto.isDisponible())
                .precio(producto.getPrecio())
                .cantidadDisponible(producto.getCantidadDisponible())
                .fechaCreacion(producto.getFechaCreacion().toString())
                .autor(Map.of(
                        "id", producto.getUsuario().getId().toString(),
                        "nombre", producto.getUsuario().getNombreCompleto(),
                        "imagen", producto.getUsuario().getImagenUrl(),
                        "telefono", producto.getUsuario().getTelefono()
                ))
                .imagenes(imagenesProducto)
                .build();
    }

    @Override
    public CrearProductoResponse crearProducto(CrearProductoRequest crearProductoRequest,
                                               List<MultipartFile> imagenes) {

        String correo = UsuarioAutenticadoUtils.obtenerCorreoDeUsuarioAutenticado();

        Usuario usuario = usuarioService.buscarUsuarioPorCorreo(correo);
        Categoria categoria = categoriaRepository.findById(crearProductoRequest.getCategoriaId())
                .orElseThrow(() -> new CategoriaNotFoundException("Categoria no encontrada"));

        Producto producto = Producto.builder()
                .categoria(categoria)
                .nombre(crearProductoRequest.getNombre())
                .descripcion(crearProductoRequest.getDescripcion())
                .disponible(crearProductoRequest.isDisponible())
                .precio(crearProductoRequest.getPrecio())
                .cantidadDisponible(crearProductoRequest.getCantidadDisponible())
                .usuario(usuario)
                .build();

        Producto productoGuardado = productoRepository.save(producto);

        List<ImagenProducto> imagenesProductoList = imagenes.stream().map(imagen -> {
            Map<String, String> imagenSubida = cloudinaryService.uploadImage(imagen, "productos");
            return ImagenProducto.builder()
                    .producto(producto)
                    .nombreImagen(imagenSubida.get("publicId"))
                    .imagenUrl(imagenSubida.get("url"))
                    .build();
        }).collect(Collectors.toList());

        imagenProductoRepository.saveAll(imagenesProductoList);

        return new CrearProductoResponse(productoGuardado.getId(), productoGuardado.getNombre(), "Producto creado exitosamente");
    }

    @Override
    public ActualizarProductoResponse actualizarProducto(Long id, ActualizarProductoRequest actualizarProductoRequest) {

        if(id == null || id < 0) throw new ProductoNotFoundException("El id del producto no puede ser nulo");

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado"));

        String usuarioAutenticadoCorreo = UsuarioAutenticadoUtils.obtenerCorreoDeUsuarioAutenticado();
        if(!usuarioAutenticadoCorreo.equals(producto.getUsuario().getCorreo()))  {
            throw new ProductoModifiedByAnotherUserException("No tienes permisos para modificar este producto");
        }

        Categoria categoria = categoriaRepository.findById(actualizarProductoRequest.getCategoriaId())
                .orElseThrow(() -> new CategoriaNotFoundException("Categoria no encontrada"));

        producto.setCategoria(categoria);
        producto.setNombre(actualizarProductoRequest.getNombre());
        producto.setDescripcion(actualizarProductoRequest.getDescripcion());
        producto.setDisponible(actualizarProductoRequest.isDisponible());
        producto.setPrecio(actualizarProductoRequest.getPrecio());
        producto.setCantidadDisponible(actualizarProductoRequest.getCantidadDisponible());

        productoRepository.save(producto);

        return new ActualizarProductoResponse(producto.getId(), producto.getNombre(), "Producto actualizado exitosamente");
    }

    @Override
    public ProductoEliminadoResponse eliminarProducto(Long id) {

        if(id == null || id < 0) throw new ProductoNotFoundException("El id del producto no puede ser nulo");

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado"));

        String usuarioAutenticadoCorreo = UsuarioAutenticadoUtils.obtenerCorreoDeUsuarioAutenticado();
        Usuario usuario = usuarioService.buscarUsuarioPorCorreo(usuarioAutenticadoCorreo);

        if(!(usuario.getRol().getRol().equals(ERol.ADMINISTRADOR))) {
            if(!usuarioAutenticadoCorreo.equals(producto.getUsuario().getCorreo())) {
                throw new UnauthorizedProductoDeletionException("No tienes permisos para eliminar este producto");
            }
        }

        List<ImagenProducto> imagenProductoList = imagenProductoRepository.findAllByProductoId(producto.getId());
        imagenProductoList.forEach(imagenProducto -> {
            cloudinaryService.deleteImage(imagenProducto.getNombreImagen());
        });

        Optional<ProductoDestacado> productoDestacado = productoDestacadoRepository.findById(producto.getId());

        productoDestacado.ifPresent(destacado -> productoDestacadoRepository.delete(destacado));

        imagenProductoRepository.deleteAll(imagenProductoList);
        productoRepository.delete(producto);

        return new ProductoEliminadoResponse( "Producto " + producto.getNombre() + " eliminado exitosamente");

    }

    @Override
    public Page<ProductoView> buscarProductoPorNombre(Pageable pageable, String nombre) {

        if (nombre == null || nombre.isBlank()) {
            throw new ProductoNotFoundException("El nombre del producto no puede ser nulo");
        }

        return productoRepository.findAllByNombreContainingIgnoreCase(pageable, nombre);
    }

    @Override
    public Page<MiProductoView> buscarProductosDelUsuarioAutenticado(Pageable pageable) {

        String correo = UsuarioAutenticadoUtils.obtenerCorreoDeUsuarioAutenticado();
        Usuario usuario = usuarioService.buscarUsuarioPorCorreo(correo);

        return productoRepository.findAllByUsuarioId(pageable, usuario.getId());
    }

    @Override
    public List<ProductoView> obtenerUltimos8Productos() {
        return productoRepository.findTop8ByOrderByFechaCreacionDesc();
    }

    @Override
    public String descatarProducto(Long id) {
        if(id == null) throw new ProductoNotFoundException("El id del producto no puede ser nulo");
        List<ProductoDestacado> productoDestacados = productoDestacadoRepository.findAll();

        if(productoDestacados.size() == 8) {
            throw new MaxFeaturedProductsReachedException("No puedes destacar más de 8 productos");
        }
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado"));

        boolean existeProductoDestacado = productoDestacados.stream().anyMatch(productoDestacado -> productoDestacado.getProducto().getId().equals(id));

        if(existeProductoDestacado) {
            throw new ProductoAlreadyFeaturedException("El producto ya está destacado");
        }

        ProductoDestacado productoDestacado = ProductoDestacado.builder().producto(producto).build();
        productoDestacadoRepository.save(productoDestacado);

        return "Producto destacado exitosamente";
    }

    @Override
    public String eliminarProductoDestacado(Long id) {
        ProductoDestacado productoDestacado = productoDestacadoRepository.findById(id)
                .orElseThrow(() -> new ProductoDestacadoNotFoundException("Producto destacado no encontrado"));

        productoDestacadoRepository.delete(productoDestacado);

        return "Producto destacado eliminado exitosamente";
    }
}
