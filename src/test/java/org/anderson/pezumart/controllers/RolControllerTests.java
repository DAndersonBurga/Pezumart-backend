package org.anderson.pezumart.controllers;

import org.anderson.pezumart.controllers.api.RolController;
import org.anderson.pezumart.entity.Rol;
import org.anderson.pezumart.entity.enums.ERol;
import org.anderson.pezumart.repository.RolRepository;
import org.anderson.pezumart.service.UsuarioService;
import org.anderson.pezumart.utils.auth.JwtUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(controllers = RolController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class RolControllerTests {

    @MockBean
    private RolRepository rolRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private JwtUtils jwtUtils;

    @Test
    @DisplayName("Listar roles")
    void RolController_ListarRoles_ReturnListRol() throws Exception {
        Mockito.when(rolRepository.findAll())
                .thenReturn(List.of(new Rol(1L, ERol.USUARIO)));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/rol/listar"));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L));

    }
}
