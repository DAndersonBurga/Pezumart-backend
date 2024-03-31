package org.anderson.pezumart.repository;

import org.anderson.pezumart.entity.Categoria;
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
public class CategoriaRepositoryTests {

    @Autowired
    private CategoriaRepository categoriaRepository;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = Categoria.builder()
                .nombre("Golosinas")
                .build();
    }

    @Test
    @DisplayName("Crear Categoría")
    void CategoriaRepository_CrearCategoria_ReturnCategoriaCreada() {
        Categoria categoriaCreada = categoriaRepository.save(categoria);

        // Assert
        Assertions.assertThat(categoriaCreada).isNotNull();
        Assertions.assertThat(categoriaCreada.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Listar Categorías")
    void CategoriaRepository_ListarCategorias_ReturnCategorias() {
        categoriaRepository.save(categoria);

        List<Categoria> categorias = categoriaRepository.findAll();
        
        Assertions.assertThat(categorias).isNotNull();
        Assertions.assertThat(categorias).hasSizeGreaterThan(0);
    }

    @Test
    @DisplayName("Buscar Categoría por ID")
    void CategoriaRepository_BuscarCategoriaPorId_ReturnCategoriaEncontrada() {
        Categoria categoriaCreada = categoriaRepository.save(categoria);

        Optional<Categoria> categoriaEncontrada = categoriaRepository.findById(categoriaCreada.getId());

        Assertions.assertThat(categoriaEncontrada).isNotEmpty();
        Assertions.assertThat(categoriaEncontrada.get().getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Actualizar Categoría")
    void CategoriaRepository_ActualizarCategoria_ReturnCategoriaActualizada() {
        Categoria categoriaCreada = categoriaRepository.save(categoria);

        categoriaCreada.setNombre("Frutasss");

        Categoria categoriaActualizada = categoriaRepository.save(categoriaCreada);

        Assertions.assertThat(categoriaActualizada).isNotNull();
        Assertions.assertThat(categoriaActualizada.getNombre()).isEqualTo("Frutasss");
    }

    @Test
    @DisplayName("Eliminar Categoría")
    void CategoriaRepository_EliminarCategoria_ReturnVoid() {
        Categoria categoriaCreada = categoriaRepository.save(categoria);

        categoriaRepository.delete(categoriaCreada);
        
        Optional<Categoria> categoriaEncontrada = categoriaRepository.findById(categoriaCreada.getId());

        Assertions.assertThat(categoriaEncontrada).isEmpty();
    }
}
