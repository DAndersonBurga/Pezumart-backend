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
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ImagenProductoRepositoryTests {

    @Autowired
    private ImagenProductoRepository imagenProductoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    private ImagenProducto imagenProducto;

    private Producto producto;

    @BeforeEach
    void setUp() {
        Categoria categoria = Categoria.builder().nombre("Categoria de prueba").build();
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

        imagenProducto = ImagenProducto.builder()
                .nombreImagen("Imagen de prueba")
                .imagenUrl("https://www.imagen.com")
                .producto(productoRepository.findById(producto.getId()).get())
                .build();
    }

    @Test
    @DisplayName("Crear una imagen de un producto")
    void ImagenProductoRepository_CrearUnaImagenProducto_ReturnImagenProducto() {

        ImagenProducto imagenProductoCreada = imagenProductoRepository.save(imagenProducto);

        Assertions.assertThat(imagenProductoCreada).isNotNull();
        Assertions.assertThat(imagenProductoCreada.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Buscar una imagen de un producto por id")
    void ImagenProductoRepository_BuscarUnaImagenPorId_ReturnImagenProducto() {

        ImagenProducto imagenProductoCreada = imagenProductoRepository.save(imagenProducto);

        Optional<ImagenProducto> imagenProductoEncontrada = imagenProductoRepository.findById(imagenProductoCreada.getId());

        Assertions.assertThat(imagenProductoEncontrada).isNotEmpty();
        Assertions.assertThat(imagenProductoEncontrada.get().getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Listar todas las imagenes de un producto por idProducto")
    void ImagenProductoRepository_ListarImagenesPorProductoId_ReturnListImagenProducto() {
        ImagenProducto imagenProductoCreada = imagenProductoRepository.save(imagenProducto);
        List<ImagenProducto> imagenesProductos = imagenProductoRepository.findAllByProductoId(imagenProductoCreada.getId());

        Assertions.assertThat(imagenesProductos).isNotNull();
        Assertions.assertThat(imagenesProductos).hasSizeGreaterThan(0);
    }

    @Test
    @DisplayName("Actualizar una imagen de un producto")
    void ImagenProductoRepository_ActualizarImagenRepository_ReturnImagenProducto() {
        ImagenProducto imagenProductoCreada = imagenProductoRepository.save(imagenProducto);
        imagenProductoCreada.setNombreImagen("Imagen de prueba actualizada");
        imagenProductoCreada.setImagenUrl("https://www.imagen-actualizada.com");

        ImagenProducto imagenProductoActualizada = imagenProductoRepository.save(imagenProductoCreada);

        Assertions.assertThat(imagenProductoActualizada).isNotNull();
        Assertions.assertThat(imagenProductoActualizada.getId()).isEqualTo(imagenProductoCreada.getId());
        Assertions.assertThat(imagenProductoActualizada.getNombreImagen()).isNotBlank();
        Assertions.assertThat(imagenProductoActualizada.getImagenUrl()).isNotBlank();
    }

    @Test
    @DisplayName("Eliminar una imagen de un producto")
    void ImagenProductoRepository_EliminarImagenProducto_ReturnVoid() {
        ImagenProducto imagenProductoCreada = imagenProductoRepository.save(imagenProducto);
        imagenProductoRepository.delete(imagenProductoCreada);

        Optional<ImagenProducto> imagenProductoEncontrada = imagenProductoRepository.findById(imagenProductoCreada.getId());

        Assertions.assertThat(imagenProductoEncontrada).isEmpty();
    }

    @Test
    @DisplayName("Eliminar todas las imágenes de un producto")
    void ImagenProductoRepository_DeleteAllImagenProducto_ReturnVoid() {
        ImagenProducto imagenProductoCreada = imagenProductoRepository.save(imagenProducto);
        imagenProductoRepository.deleteAll(List.of(imagenProductoCreada));

        Optional<ImagenProducto> imagenProductoEncontrada = imagenProductoRepository.findById(imagenProductoCreada.getId());

        Assertions.assertThat(imagenProductoEncontrada).isEmpty();
    }
}
