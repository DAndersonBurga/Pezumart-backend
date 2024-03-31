package org.anderson.pezumart.repository;

import org.anderson.pezumart.entity.Rol;
import org.anderson.pezumart.entity.Usuario;
import org.anderson.pezumart.entity.enums.ERol;
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

import java.util.Optional;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UsuarioRepositoryTests {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        Rol rol = Rol.builder().rol(ERol.ADMINISTRADOR).build();

        rolRepository.save(rol);

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
    }

    @Test
    @DisplayName("Listar usuarios con Pageable")
    void UsuarioRepository_FindAll_ReturnUsuarios() {
        usuarioRepository.save(usuario);

        Pageable pageable = Pageable.ofSize(10);
        Page<Usuario> usuarios = usuarioRepository.findAll(pageable);

        Assertions.assertThat(usuarios).isNotNull();
        Assertions.assertThat(usuarios.toList()).isNotEmpty();
    }

    @Test
    @DisplayName("Guardar Usuario")
    void UsuarioRepository_GuardarUsuario_ReturnUsuarioGuardado() {
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        Assertions.assertThat(usuarioGuardado).isNotNull();
        Assertions.assertThat(usuarioGuardado.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Buscar Usuario por Id")
    void UsuarioRepository_BuscarPorId_ReturnUsuarioEncontrado() {

       usuarioRepository.save(usuario);

        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(usuario.getId());

        Assertions.assertThat(usuarioEncontrado).isNotEmpty();
        Assertions.assertThat(usuarioEncontrado.get().getId()).isGreaterThanOrEqualTo(2L);
    }

    @Test
    @DisplayName("Buscar Usuario por Correo")


    void UsuarioRepository_BuscarPorCorreo_ReturnUsuarioEncontrado() {
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByCorreo("correo@correo.com");

        Assertions.assertThat(usuarioEncontrado).isPresent();
        Assertions.assertThat(usuarioEncontrado.get().getId()).isGreaterThan(0L);
        Assertions.assertThat(usuarioEncontrado.get().getCorreo()).isEqualTo(usuarioGuardado.getCorreo());
    }

    @Test
    @DisplayName("Buscar Usuario por Nombre Completo Containing Ignore Case")
    void UsuarioRepository_BuscarPorNombreCompletoContainingIgnoreCase_ReturnUsuarioEncontrado() {
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByNombreCompletoContainingIgnoreCase("anderson");

        Assertions.assertThat(usuarioEncontrado).isPresent();
        Assertions.assertThat(usuarioEncontrado.get().getId()).isGreaterThan(0L);
        Assertions.assertThat(usuarioEncontrado.get().getNombreCompleto()).isEqualTo(usuarioGuardado.getNombreCompleto());
    }

    @Test
    @DisplayName("Actualizar Usuario")
    void UsuarioRepository_ActualizarUsuario_ReturnUsuarioActualizado() {
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        usuarioGuardado.setNombreCompleto("Anderson Pezuña");
        usuarioGuardado.setCorreo("correo2@correo.com");

        Usuario usuarioActualizado = usuarioRepository.save(usuarioGuardado);

        Assertions.assertThat(usuarioActualizado).isNotNull();
        Assertions.assertThat(usuarioActualizado.getId()).isEqualTo(usuarioGuardado.getId());
        Assertions.assertThat(usuarioActualizado.getCorreo()).isEqualTo("correo2@correo.com");
    }

    @Test
    @DisplayName("Eliminar Usuario")
    void UsuarioRepository_EliminarUsuario_ReturnVoid() {
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        usuarioRepository.delete(usuarioGuardado);

        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(usuarioGuardado.getId());

        Assertions.assertThat(usuarioEncontrado).isEmpty();
    }
}
