package org.anderson.pezumart.service;

import org.anderson.pezumart.controllers.request.ActualizarUsuarioRequest;
import org.anderson.pezumart.controllers.request.RegistrarUsuarioRequest;
import org.anderson.pezumart.controllers.response.UsuarioActualizadoResponse;
import org.anderson.pezumart.controllers.response.UsuarioCreadoResponse;
import org.anderson.pezumart.controllers.response.UsuarioEliminadoResponse;
import org.anderson.pezumart.entity.ImagenProducto;
import org.anderson.pezumart.entity.Producto;
import org.anderson.pezumart.entity.Rol;
import org.anderson.pezumart.entity.Usuario;
import org.anderson.pezumart.entity.enums.ERol;
import org.anderson.pezumart.repository.ImagenProductoRepository;
import org.anderson.pezumart.repository.ProductoRepository;
import org.anderson.pezumart.repository.RolRepository;
import org.anderson.pezumart.repository.UsuarioRepository;
import org.anderson.pezumart.service.impl.UsuarioServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTests {

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private ImagenProductoRepository imagenProductoRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    RegistrarUsuarioRequest registrarUsuarioRequest;

    @BeforeEach
    void setUp() {
        registrarUsuarioRequest = new RegistrarUsuarioRequest();
        registrarUsuarioRequest.setNombreCompleto("Anderson Pezua");
        registrarUsuarioRequest.setCoordenadas("0.0,0.0");
        registrarUsuarioRequest.setCorreo("anderson123@correo.com");
        registrarUsuarioRequest.setPassword("123456");
        registrarUsuarioRequest.setRol(1);
        registrarUsuarioRequest.setDireccion("Calle 123");
        registrarUsuarioRequest.setTelefono("51987654321");
    }

    @Test
    @DisplayName("Registrar Usuario")
    void UsuarioService_RegistrarUsuario_ReturnUsuarioCreadoResponse() {

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test".getBytes());

        when(usuarioRepository.findByCorreo(Mockito.anyString()))
                .thenReturn(Optional.empty());

        when(rolRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new Rol(1L, ERol.USUARIO)));

        when(cloudinaryService.uploadImage(Mockito.any(), Mockito.anyString()))
                .thenReturn(Map.of("publicId", "test", "url", "test.com"));

        when(passwordEncoder.encode(Mockito.anyString()))
                .thenReturn("123456");

        when(usuarioRepository.save(Mockito.any())).thenReturn(
                Usuario.builder().id(1L).correo("correo123@correo.com").build()
        );

        UsuarioCreadoResponse usuarioCreadoResponse = usuarioService.registrarUsuario(registrarUsuarioRequest, mockMultipartFile);

        Assertions.assertThat(usuarioCreadoResponse).isNotNull();
        Assertions.assertThat(usuarioCreadoResponse.getUsername()).isEqualTo("anderson123@correo.com");
    }

    @Test
    @DisplayName("Listar Usuarios")
    void UsuarioService_ListarUsuarios_ReturnPageUsuario() {
        Usuario usuario = Usuario.builder().nombreCompleto("Anderson").build();

        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(usuario);
        usuarios.add(usuario);
        usuarios.add(usuario);

        Page<Usuario> usuarioPage = new PageImpl<>(usuarios);

        when(usuarioRepository.findAll(Mockito.any(Pageable.class))).thenReturn(usuarioPage);

        Page<Usuario> usuariosPage = usuarioService.listarUsuarios(Pageable.ofSize(5));

        verify(usuarioRepository).findAll(Mockito.any(Pageable.class));
        Assertions.assertThat(usuariosPage).isNotNull();
        Assertions.assertThat(usuariosPage).hasSizeGreaterThan(2);
    }

    @Test
    @DisplayName("Buscar Usuario por Correo")
    void UsuarioService_BuscarUsuarioPorCorreo_ReturnUsuario() {

        when(usuarioRepository.findByCorreo(Mockito.anyString())).thenReturn(
                Optional.of(Usuario.builder().correo("correo123@correo.com").build()));

        Usuario usuario = usuarioService.buscarUsuarioPorCorreo("correo123@correo.com");

        verify(usuarioRepository).findByCorreo(Mockito.anyString());
        Assertions.assertThat(usuario).isNotNull();
        Assertions.assertThat(usuario.getCorreo()).isEqualTo("correo123@correo.com");
    }

    @Test
    @DisplayName("Buscar Usuario por Nombre")
    void UsuarioService_BuscarUsuarioPorNombre_ReturnUsuario() {
        when(usuarioRepository.findByNombreCompletoContainingIgnoreCase(Mockito.anyString())).thenReturn(
                Optional.of(Usuario.builder().nombreCompleto("Anderson").build()));

        Usuario usuario = usuarioService.buscarUsuarioPorNombre("Anderson");

        verify(usuarioRepository).findByNombreCompletoContainingIgnoreCase(Mockito.anyString());
        Assertions.assertThat(usuario).isNotNull();
        Assertions.assertThat(usuario.getNombreCompleto()).isEqualTo("Anderson");
    }

    @Test
    @DisplayName("Actualizar Usuario")
    void UsuarioService_ActualizarUsuario_ReturnUsuarioActualizadoResponse() {

        ActualizarUsuarioRequest usuarioActualizadoRequest = new ActualizarUsuarioRequest();
        usuarioActualizadoRequest.setNombreCompleto("Anderson Pezua");
        usuarioActualizadoRequest.setCoordenadas("0.0,0.0");
        usuarioActualizadoRequest.setDireccion("Calle 123");
        usuarioActualizadoRequest.setTelefono("51987654321");

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test".getBytes());

        when(usuarioRepository.findById(Mockito.anyLong())).thenReturn(
                Optional.of(
                        Usuario.builder()
                                .id(1L)
                                .nombreCompleto("Anderson")
                                .nombreImagen("pepito").build()
                )
        );

        when(cloudinaryService.uploadImage(Mockito.any(), Mockito.anyString()))
                .thenReturn(
                        Map.of(
                        "publicId", "test",
                        "url", "test.com"
                        )
                );

        doNothing().when(cloudinaryService).deleteImage(Mockito.anyString());

        when(usuarioRepository.save(Mockito.any())).thenReturn(
                Usuario.builder()
                        .id(1L)
                        .nombreCompleto("Anderson").build()
        );

        UsuarioActualizadoResponse usuarioActualizadoResponse = usuarioService.actualizarUsuario(
                usuarioActualizadoRequest, mockMultipartFile, 1L);

        verify(usuarioRepository).findById(Mockito.anyLong());
        Assertions.assertThat(usuarioActualizadoResponse).isNotNull();
        Assertions.assertThat(usuarioActualizadoResponse.getNombreCompleto())
                .isEqualTo("Anderson Pezua");
    }

    @Test
    @DisplayName("Eliminar Usuario")
    void UsuarioService_EliminarUsuario_ReturnUsuarioEliminadoResponse() {
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nombreImagen("imagen123")
                .nombreCompleto("Anderson").build();

        Producto producto = Producto.builder().id(1L).build();

        ImagenProducto imagenProducto = ImagenProducto.builder().nombreImagen("imagen123").build();

        List<Producto> productos = List.of(producto, producto);
        List<ImagenProducto> imagenProductos = List.of(imagenProducto, imagenProducto);

        when(usuarioRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(usuario));

        when(productoRepository.findAllByUsuarioId(Mockito.anyLong()))
                .thenReturn(productos);

        when(imagenProductoRepository.findAllByProductoId(Mockito.anyLong()))
                .thenReturn(imagenProductos);

        doNothing().when(cloudinaryService).deleteImage(Mockito.anyString());
        doNothing().when(imagenProductoRepository).deleteAll(imagenProductos);
        doNothing().when(productoRepository).deleteAll(productos);
        doNothing().when(usuarioRepository).delete(usuario);

        UsuarioEliminadoResponse usuarioEliminadoResponse = usuarioService.eliminarUsuario(1L);

        verify(usuarioRepository).findById(Mockito.anyLong());
        verify(productoRepository).findAllByUsuarioId(Mockito.anyLong());
        verify(imagenProductoRepository, times(2)).findAllByProductoId(Mockito.anyLong());
        verify(cloudinaryService, times(5)).deleteImage(Mockito.anyString());
        verify(imagenProductoRepository, times(2)).deleteAll(imagenProductos);
        verify(productoRepository).deleteAll(productos);
        verify(usuarioRepository).delete(usuario);

        Assertions.assertThat(usuarioEliminadoResponse).isNotNull();
        Assertions.assertThat(usuarioEliminadoResponse.getId()).isGreaterThan(0);
        Assertions.assertThat(usuarioEliminadoResponse.getNombreCompleto()).isNotBlank();

    }
}
