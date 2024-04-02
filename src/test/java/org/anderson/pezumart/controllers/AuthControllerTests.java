package org.anderson.pezumart.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.anderson.pezumart.controllers.auth.AuthController;
import org.anderson.pezumart.controllers.request.LoginRequest;
import org.anderson.pezumart.controllers.response.LoginResponse;
import org.anderson.pezumart.service.AuthenticationService;
import org.anderson.pezumart.service.UsuarioService;
import org.anderson.pezumart.service.auth.AuthenticationServiceImpl;
import org.anderson.pezumart.utils.auth.JwtUtils;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // Disables any security filters
@ExtendWith(MockitoExtension.class)
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private JwtUtils jwtUtils;

    @Test
    @DisplayName("Login de usuarios")
    void AuthController_Login_ReturnLoginResponse() throws Exception {
        LoginResponse loginResponse = new LoginResponse("token");
        LoginRequest loginRequest = new LoginRequest("correo123@gmail.com", "123456");

        when(authenticationService.login(Mockito.any()))
                .thenReturn(loginResponse);

        ResultActions response = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.jwt", CoreMatchers.is(loginResponse.getJwt())))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("Validar token JWT")
    void AuthController_ValidarTokenJWT_ReturnBoolean() throws Exception {
        when(authenticationService.validateToken(Mockito.anyString()))
                .thenReturn(true);

        ResultActions response = mockMvc.perform(get("/auth/validate?token=token"));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(true))
                .andDo(MockMvcResultHandlers.print());
    }

}