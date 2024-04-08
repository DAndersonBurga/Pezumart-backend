package org.anderson.pezumart.repository;

import org.anderson.pezumart.entity.*;
import org.anderson.pezumart.entity.enums.ERol;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductoDestacadoRepositoryTests {

    @Autowired
    private ProductoDestacadoRepository productoDestacadoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    private Producto producto;

    @BeforeEach
    void setUp() {
        Categoria categoria = Categoria.builder().nombre("Categoria de prueba").imagen("http://imagen.com").build();
        Rol rol = Rol.builder().rol(ERol.ADMINISTRADOR).build();

        rolRepository.save(rol);
        categoriaRepository.save(categoria);

        Usuario usuario = Usuario.builder()
                .nombreCompleto("Anderson")
                .correo("correo@correo.com")
                .rol(rol)
                .password("123456")
                .nombreImagen("imagen.jpg")
                .imagenUrl("https://www.imagen.com")
                .direccion("Calle 123")
                .telefono("51987654321")
                .build();

        usuarioRepository.save(usuario);


        producto = Producto.builder()
                .nombre("Producto de prueba")
                .descripcion("Descripción de prueba")
                .precio(100.0)
                .cantidadDisponible(10)
                .usuario(usuario)
                .categoria(categoria)
                .disponible(true)
                .precio(100.0)
                .build();

        productoRepository.save(producto);
    }

    @Test
    @DisplayName("Test Guardar Producto Destacado")
    void ProductoDestacadoRepository_GuardarProductoDestacado_ReturnProductoDestacado() {
        ProductoDestacado productoDestacado = ProductoDestacado.builder()
                .producto(producto)
                .build();

        ProductoDestacado productoDestacadoGuardado = productoDestacadoRepository.save(productoDestacado);

        assertThat(productoDestacadoGuardado).isNotNull();
        assertThat(productoDestacadoGuardado.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Listar Productos Destacados")
    void ProductoDestacadoRepository_ListarProductosDestacados_ReturnListProductoDestacado() {

        ProductoDestacado productoDestacado = ProductoDestacado.builder()
                .producto(producto)
                .build();

        productoDestacadoRepository.save(productoDestacado);
        List<ProductoDestacado> productoDestacados = productoDestacadoRepository.findAll();

        assertThat(productoDestacados).isNotEmpty();
        assertThat(productoDestacados).hasSizeGreaterThan(0);
    }

    @Test
    @DisplayName("Buscar Producto Destacado por Id")
    void ProductoDestacadoRepository_BuscarProductoDestacadoPorId_ReturnProductoDestacado() {

        ProductoDestacado productoDestacado = ProductoDestacado.builder()
                .producto(producto)
                .build();

        ProductoDestacado productoDestacadoGuardado = productoDestacadoRepository.save(productoDestacado);

        ProductoDestacado productoDestacadoEncontrado = productoDestacadoRepository.findById(productoDestacadoGuardado.getId()).orElse(null);


        assertThat(productoDestacadoEncontrado).isNotNull();
        assertThat(productoDestacadoEncontrado.getId()).isEqualTo(productoDestacadoGuardado.getId());
    }

    @Test
    @DisplayName("Eliminar Producto Destacado")
    void ProductoDestacadoRepository_EliminarProductoDestacado_ReturnVoid() {
        ProductoDestacado productoDestacado = ProductoDestacado.builder()
                .producto(producto)
                .build();

        ProductoDestacado productoDestacadoGuardado = productoDestacadoRepository.save(productoDestacado);

        productoDestacadoRepository.delete(productoDestacadoGuardado);

        ProductoDestacado productoDestacadoBorrado = productoDestacadoRepository.findById(productoDestacadoGuardado.getId()).orElse(null);

        assertThat(productoDestacadoBorrado).isNull();
    }
}
