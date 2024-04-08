package org.anderson.pezumart.repository;

import org.anderson.pezumart.entity.Categoria;
import org.anderson.pezumart.entity.Producto;
import org.anderson.pezumart.entity.Rol;
import org.anderson.pezumart.entity.Usuario;
import org.anderson.pezumart.entity.enums.ERol;
import org.anderson.pezumart.repository.projections.MiProductoView;
import org.anderson.pezumart.repository.projections.ProductoView;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductoRepositoryTests {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    private Usuario usuario;
    private Producto producto;

    @BeforeEach
    void setUp() {
        Categoria categoria = Categoria.builder().nombre("Categoria de prueba").imagen("http://imagen.com").build();
        Rol rol = Rol.builder().rol(ERol.ADMINISTRADOR).build();

        rolRepository.save(rol);
        categoriaRepository.save(categoria);

        usuario = Usuario.builder()
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
    }

    @Test
    @DisplayName("Crear un Producto")
    void ProductoRepository_CrearProducto_ReturnProducto() {
        Producto productoCreado = productoRepository.save(producto);

        Assertions.assertThat(productoCreado).isNotNull();
        Assertions.assertThat(productoCreado.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Guardar varios productos")
    void ProductoRepository_SaveAll_ReturnsProductos() {

        List<Producto> productos = productoRepository.saveAll(List.of(producto));

        Assertions.assertThat(productos).isNotNull();
        Assertions.assertThat(productos).hasSizeGreaterThan(0);
    }

    @Test
    @DisplayName("Obtener producto por Id")
    void ProductoRepository_FindById_ReturnProducto() {
        Producto productoCreado = productoRepository.save(producto);

        Optional<Producto> productoEncontrado = productoRepository.findById(productoCreado.getId());

        Assertions.assertThat(productoEncontrado).isNotEmpty();
        Assertions.assertThat(productoEncontrado.get().getId()).isEqualTo(productoCreado.getId());
    }

    // FindAllProjectedBy

    @Test
    @DisplayName("Obtener todos los productos con proyección ProductoView")
    void ProductoRepository_FindAllProjectedBy_ReturnPageProductoView() {
        productoRepository.save(producto);

        Page<ProductoView> productoViews = productoRepository.findAllProjectedBy(Pageable.ofSize(5));

        Assertions.assertThat(productoViews).isNotNull();
        Assertions.assertThat(productoViews).hasSizeGreaterThan(0);
    }

    @Test
    @DisplayName("Obtener todos los productos por nombre con proyección ProductoView")
    void ProductoRepository_FindAllByNombreContainingIgnoreCase_ReturnPageProductoView() {
        productoRepository.save(producto);

        Page<ProductoView> productoViews = productoRepository.findAllByNombreContainingIgnoreCase(Pageable.ofSize(5), "Producto");

        Assertions.assertThat(productoViews).isNotNull();
        Assertions.assertThat(productoViews).hasSizeGreaterThan(0);
    }

    @Test
    @DisplayName("Obtener todos los productos por Id de usuario con proyección MiProductoView")
    void ProductoRepository_FindAllByUsuarioId_ReturnPageMiProductoView() {
        productoRepository.save(producto);

        Page<MiProductoView> productoViews = productoRepository.findAllByUsuarioId(
                Pageable.ofSize(5), usuario.getId());

        Assertions.assertThat(productoViews).isNotNull();
        Assertions.assertThat(productoViews).hasSizeGreaterThan(0);
    }

    @Test
    @DisplayName("Obtener todos los productos por Id de usuario")
    void ProductoRepository_FindAllByUsuarioId_ReturnListProducto() {
        productoRepository.save(producto);

        List<Producto> productos = productoRepository.findAllByUsuarioId(usuario.getId());

        Assertions.assertThat(productos).isNotNull();
        Assertions.assertThat(productos).hasSizeGreaterThan(0);
    }

    @Test
    @DisplayName("Actualizar un Producto")
    void ProductoRepository_ActualizarProducto_ReturnProducto() {
        Producto productoCreado = productoRepository.save(producto);

        productoCreado.setNombre("Producto actualizado");
        productoCreado.setDescripcion("Descripción actualizada");

        Producto productoActualizado = productoRepository.save(productoCreado);

        Assertions.assertThat(productoActualizado).isNotNull();
        Assertions.assertThat(productoActualizado.getId()).isEqualTo(productoCreado.getId());
        Assertions.assertThat(productoActualizado.getNombre()).isEqualTo(productoCreado.getNombre());
        Assertions.assertThat(productoActualizado.getDescripcion()).isEqualTo(productoCreado.getDescripcion());
    }

    @Test
    @DisplayName("Eliminar un Producto")
    void ProductoRepository_EliminarProducto_ReturnVoid() {
        Producto productoCreado = productoRepository.save(producto);

        productoRepository.delete(productoCreado);

        Optional<Producto> productoEncontrado = productoRepository.findById(productoCreado.getId());

        Assertions.assertThat(productoEncontrado).isEmpty();
    }

    @Test
    @DisplayName("Eliminar varios productos")
    void ProductoRepository_ELiminarProductos_ReturnVoid() {
        productoRepository.save(producto);

        productoRepository.deleteAll(List.of(producto));

        Optional<Producto> productoEncontrado = productoRepository.findById(producto.getId());

        Assertions.assertThat(productoEncontrado).isEmpty();
    }

    @Test
    @DisplayName("Obtener los últimos 8 productos")
    void ProductoRepository_ObtenerLosUltimos8Productos_ReturnListProductoView() {
        productoRepository.save(producto);

        List<ProductoView> productoViews = productoRepository.findTop8ByOrderByFechaCreacionDesc();

        Assertions.assertThat(productoViews).isNotNull();
        Assertions.assertThat(productoViews).hasSizeGreaterThan(0);
    }
}
