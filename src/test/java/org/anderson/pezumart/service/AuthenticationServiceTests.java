package org.anderson.pezumart.service;

import org.anderson.pezumart.controllers.request.LoginRequest;
import org.anderson.pezumart.controllers.response.LoginResponse;
import org.anderson.pezumart.entity.Rol;
import org.anderson.pezumart.entity.Usuario;
import org.anderson.pezumart.entity.enums.ERol;
import org.anderson.pezumart.service.auth.AuthenticationService;
import org.anderson.pezumart.utils.auth.JwtUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTests {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        Rol rol = Rol.builder()
                .id(1L)
                .rol(ERol.ADMINISTRADOR)
                .build();

        usuario = Usuario.builder()
                .nombreCompleto("Anderson")
                .rol(rol)
                .build();
    }

    @Test
    @DisplayName("Login de usuario")
    void AuthenticationService_Login_LoginResponse() {

        when(authenticationManager.authenticate(Mockito.any(Authentication.class)))
                .thenReturn(Mockito.mock(Authentication.class));

        when(usuarioService.buscarUsuarioPorCorreo(Mockito.anyString()))
                .thenReturn(usuario);

        when(jwtUtils.generarToken(Mockito.any(Usuario.class), Mockito.anyMap()))
                .thenReturn("jwt");

        LoginRequest loginRequest = new LoginRequest("correo@correo.com", "123456");
        LoginResponse loginResponse = authenticationService.login(loginRequest);


        Assertions.assertThat(loginResponse).isNotNull();
        Assertions.assertThat(loginResponse.getJwt()).isNotNull();
        Assertions.assertThat(loginResponse.getJwt()).isEqualTo("jwt");
    }
}
