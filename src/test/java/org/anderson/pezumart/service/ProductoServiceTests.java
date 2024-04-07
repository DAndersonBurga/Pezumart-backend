package org.anderson.pezumart.service;

import org.anderson.pezumart.controllers.request.ActualizarProductoRequest;
import org.anderson.pezumart.controllers.request.CrearProductoRequest;
import org.anderson.pezumart.controllers.response.ActualizarProductoResponse;
import org.anderson.pezumart.controllers.response.CrearProductoResponse;
import org.anderson.pezumart.controllers.response.ProductoEliminadoResponse;
import org.anderson.pezumart.controllers.response.ProductoResponse;
import org.anderson.pezumart.entity.*;
import org.anderson.pezumart.entity.enums.ERol;
import org.anderson.pezumart.repository.CategoriaRepository;
import org.anderson.pezumart.repository.ImagenProductoRepository;
import org.anderson.pezumart.repository.ProductoRepository;
import org.anderson.pezumart.repository.projections.MiProductoView;
import org.anderson.pezumart.repository.projections.ProductoView;
import org.anderson.pezumart.service.impl.ProductoServiceImpl;
import org.anderson.pezumart.utils.auth.UsuarioAutenticadoUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTests {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private ImagenProductoRepository imagenProductoRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Usuario usuario;
    private Categoria categoria;
    private ImagenProducto imagenProducto;
    private Producto producto;

    @BeforeEach
    void setUp() {

        categoria = Categoria.builder().id(1L)
                .nombre("Muebles")
                .build();

        usuario = Usuario.builder()
                .id(1L)
                .nombreCompleto("Anderson")
                .rol(new Rol(1L, ERol.ADMINISTRADOR))
                .correo("correo123@gmail.com")
                .password("123456")
                .nombreImagen("imagen.jpg")
                .telefono("123456")
                .direccion("Calle 123")
                .coordenadas("123,123")
                .imagenUrl("url").build();


        imagenProducto = ImagenProducto.builder()
                .id(1L)
                .nombreImagen("imagen.jpg")
                .imagenUrl("https://www.google.com")
                .build();

        producto = Producto.builder()
                .id(1L)
                .nombre("Mesa")
                .categoria(categoria)
                .usuario(usuario)
                .cantidadDisponible(10)
                .fechaCreacion(LocalDate.now())
                .precio(100.0)
                .descripcion("Mesa de madera")
                .disponible(true).build();
    }

    @Test
    @DisplayName("Obtener Producto por Id")
    void ProductoService_ObtenerProductoPorId_ReturnProductoResponse() {

            when(productoRepository.findById(Mockito.anyLong()))
                    .thenReturn(Optional.of(producto));

            when(imagenProductoRepository.findAllByProductoId(Mockito.anyLong()))
                    .thenReturn(List.of(imagenProducto));

            ProductoResponse productoResponse = productoService.obtenerProductoPorId(1L);

            verify(productoRepository).findById(Mockito.anyLong());
            verify(imagenProductoRepository).findAllByProductoId(Mockito.anyLong());
            Assertions.assertThat(productoResponse).isNotNull();
            Assertions.assertThat(productoResponse.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Crear Producto")
    void ProductoService_CrearProducto_ReturnCrearProductoResponse() {

        CrearProductoRequest crearProductoRequest = CrearProductoRequest.builder()
                .categoriaId(1L)
                .nombre("Mesa")
                .descripcion("Mesa de madera")
                .disponible(true)
                .precio(100.0)
                .cantidadDisponible(10)
                .build();

        try (MockedStatic<UsuarioAutenticadoUtils> usuarioAutenticadoUtils = Mockito.mockStatic(UsuarioAutenticadoUtils.class)) {
            Authentication authentication = new UsernamePasswordAuthenticationToken("correo123@gmail.com", "contraseña");
            SecurityContextHolder.getContext().setAuthentication(authentication);

            usuarioAutenticadoUtils.when(UsuarioAutenticadoUtils::obtenerCorreoDeUsuarioAutenticado)
                    .thenReturn("correo123@gmail.com");
        }

        when(usuarioService.buscarUsuarioPorCorreo(Mockito.anyString()))
                .thenReturn(usuario);

        when(categoriaRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(categoria));

        when(productoRepository.save(Mockito.any(Producto.class)))
                .thenReturn(producto);

        when(imagenProductoRepository.saveAll(Mockito.anyList()))
                .thenReturn(List.of(imagenProducto));

        when(cloudinaryService.uploadImage(Mockito.any(), Mockito.anyString()))
                .thenReturn(Map.of("publicId", "test", "url", "test.com"));

        MockMultipartFile mockMultipartFile = new MockMultipartFile("imagen", "imagen.jpg", "image/jpg", "imagen".getBytes());

        CrearProductoResponse crearProductoResponse = productoService.crearProducto(
                crearProductoRequest, List.of(mockMultipartFile, mockMultipartFile));

        verify(usuarioService).buscarUsuarioPorCorreo("correo123@gmail.com");
        verify(categoriaRepository).findById(Mockito.anyLong());
        verify(productoRepository).save(Mockito.any(Producto.class));
        verify(cloudinaryService, times(2)).uploadImage(Mockito.any(), Mockito.anyString());
        verify(imagenProductoRepository).saveAll(Mockito.anyList());

        Assertions.assertThat(crearProductoResponse).isNotNull();
        Assertions.assertThat(crearProductoResponse.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Actualizar Producto")
    void ProductoService_ActualizarProducto_ReturnActualizarProductoResponse() {

        ActualizarProductoRequest actualizarProductoRequest = new ActualizarProductoRequest();
            actualizarProductoRequest.setCategoriaId(1L);
            actualizarProductoRequest.setNombre("Mesa");
            actualizarProductoRequest.setDescripcion("Mesa de madera");
            actualizarProductoRequest.setDisponible(true);
            actualizarProductoRequest.setPrecio(100.0);

        try (MockedStatic<UsuarioAutenticadoUtils> usuarioAutenticadoUtils = Mockito.mockStatic(UsuarioAutenticadoUtils.class)) {
            Authentication authentication = new UsernamePasswordAuthenticationToken("correo123@gmail.com", "contraseña");
            SecurityContextHolder.getContext().setAuthentication(authentication);

            usuarioAutenticadoUtils.when(UsuarioAutenticadoUtils::obtenerCorreoDeUsuarioAutenticado)
                    .thenReturn("correo123@gmail.com");
        }

        when(productoRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(producto));

        when(categoriaRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(categoria));

        when(productoRepository.save(Mockito.any(Producto.class)))
                .thenReturn(producto);

        ActualizarProductoResponse actualizarProductoResponse = productoService.actualizarProducto(
                1L, actualizarProductoRequest);

        verify(productoRepository).findById(Mockito.anyLong());
        verify(categoriaRepository).findById(Mockito.any());
        verify(productoRepository).save(Mockito.any(Producto.class));

        Assertions.assertThat(actualizarProductoResponse).isNotNull();
        Assertions.assertThat(actualizarProductoResponse.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Eliminar producto")
    void ProductoService_EliminarProducto_ProductoEliminadoResponse() {
        when(productoRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(producto));

        try (MockedStatic<UsuarioAutenticadoUtils> usuarioAutenticadoUtils = Mockito.mockStatic(UsuarioAutenticadoUtils.class)) {
            Authentication authentication = new UsernamePasswordAuthenticationToken("correo123@gmail.com", "contraseña");
            SecurityContextHolder.getContext().setAuthentication(authentication);

            usuarioAutenticadoUtils.when(UsuarioAutenticadoUtils::obtenerCorreoDeUsuarioAutenticado)
                    .thenReturn("correo123@gmail.com");
        }

        when(imagenProductoRepository.findAllByProductoId(Mockito.anyLong()))
                .thenReturn(List.of(imagenProducto));

        doNothing().when(cloudinaryService).deleteImage(Mockito.anyString());
        doNothing().when(imagenProductoRepository).deleteAll(Mockito.anyList());
        doNothing().when(productoRepository).delete(Mockito.any(Producto.class));

        ProductoEliminadoResponse productoEliminadoResponse = productoService.eliminarProducto(1L);

        Assertions.assertThat(productoEliminadoResponse).isNotNull();
        Assertions.assertThat(productoEliminadoResponse.getMensaje()).isNotBlank();
    }

    @Test
    @DisplayName("Buscar Productos por nombre")
    void ProductoService_BuscarProductosPorNombre_ReturnPageProductoView() {
        Page<ProductoView> page = Mockito.mock(Page.class);

        when(productoRepository.findAllByNombreContainingIgnoreCase(Mockito.any(), Mockito.any()))
                .thenReturn(page);

        Page<ProductoView> productoViews = productoService.buscarProductoPorNombre(Pageable.ofSize(5), "Mesa");

        verify(productoRepository).findAllByNombreContainingIgnoreCase(Mockito.any(), Mockito.any());
        Assertions.assertThat(productoViews).isNotNull();
    }

    @Test
    @DisplayName("Buscar Productos del Usuario Autenticado")
    void ProductoService_BuscarProductosDelUsuarioAutenticado() {
        Page<MiProductoView> page = Mockito.mock(Page.class);

        try (MockedStatic<UsuarioAutenticadoUtils> usuarioAutenticadoUtils = Mockito.mockStatic(UsuarioAutenticadoUtils.class)) {
            Authentication authentication = new UsernamePasswordAuthenticationToken("correo123@gmail.com", "contraseña");
            SecurityContextHolder.getContext().setAuthentication(authentication);

            usuarioAutenticadoUtils.when(UsuarioAutenticadoUtils::obtenerCorreoDeUsuarioAutenticado)
                    .thenReturn("correo123@gmail.com");
        }

        when(usuarioService.buscarUsuarioPorCorreo(Mockito.anyString()))
                .thenReturn(usuario);

        when(productoRepository.findAllByUsuarioId(Mockito.any(), Mockito.any()))
                .thenReturn(page);

        Page<MiProductoView> miProductoViews = productoService.buscarProductosDelUsuarioAutenticado(Pageable.ofSize(5));

        verify(productoRepository).findAllByUsuarioId(Mockito.any(), Mockito.any());
        Assertions.assertThat(miProductoViews).isNotNull();
    }

    @Test
    @DisplayName("Obtener últimos 8 productos")
    void ProductoService_ObtenerUltimos8Productos() {
        List<ProductoView> productoViewsMock = Mockito.mock(List.class);

        when(productoRepository.findFirst8ByOrderByFechaCreacionDesc())
                .thenReturn(productoViewsMock);

        List<ProductoView> productoViews = productoService.obtenerUltimos8Productos();

        verify(productoRepository).findFirst8ByOrderByFechaCreacionDesc();
        Assertions.assertThat(productoViews).isNotNull();
    }
}
