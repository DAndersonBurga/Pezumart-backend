package org.anderson.pezumart.repository;

import org.anderson.pezumart.entity.Rol;
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
public class RolRepositoryTests {
    
    @Autowired
    private RolRepository rolRepository;
    
    private Rol rol;

    @BeforeEach
    void setUp() {
        rol = Rol.builder()
                .rol(ERol.ADMINISTRADOR)
                .build();
    }


    @Test
    @DisplayName("Crear Rol")
    void RolRepository_CrearRol_ReturnRolCreado() {
        Rol rolCreado = rolRepository.save(rol);

        Assertions.assertThat(rolCreado).isNotNull();
        Assertions.assertThat(rolCreado.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Listar Roles")
    void RolRepository_ListarRoles_ReturnRoles() {
        rolRepository.save(rol);

        List<Rol> roles = rolRepository.findAll();

        Assertions.assertThat(rolRepository.findAll()).isNotNull();
        Assertions.assertThat(roles).hasSizeGreaterThan(0);
    }

    @Test
    @DisplayName("Buscar Rol por Id")
    void RolRepository_BuscarRolPorId_ReturnRol() {
        Rol rolCreado = rolRepository.save(rol);

        Optional<Rol> rolEncontrado = rolRepository.findById(rolCreado.getId());

        Assertions.assertThat(rolEncontrado).isNotEmpty();
        Assertions.assertThat(rolEncontrado.get().getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Actualizar Rol")
    void RolRepository_ActualizarRol_ReturnRolActualizado() {
        Rol rolCreado = rolRepository.save(rol);

        rolCreado.setRol(ERol.USUARIO);

        Rol rolActualizado = rolRepository.save(rolCreado);

        Assertions.assertThat(rolActualizado).isNotNull();
        Assertions.assertThat(rolActualizado.getId()).isEqualTo(rolCreado.getId());
        Assertions.assertThat(rolActualizado.getRol()).isEqualTo(ERol.USUARIO);
    }

    @Test
    @DisplayName("Eliminar Rol")
    void RolRepository_EliminarRol_ReturnVoid() {
        Rol rolCreado = rolRepository.save(rol);

        rolRepository.delete(rolCreado);

        Optional<Rol> rolEncontrado = rolRepository.findById(rolCreado.getId());

        Assertions.assertThat(rolEncontrado).isEmpty();
    }
}
