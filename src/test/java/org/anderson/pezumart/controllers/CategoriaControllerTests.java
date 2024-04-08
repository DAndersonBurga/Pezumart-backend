package org.anderson.pezumart.controllers;

import org.anderson.pezumart.controllers.api.CategoriaController;
import org.anderson.pezumart.entity.Categoria;
import org.anderson.pezumart.repository.CategoriaRepository;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = CategoriaController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CategoriaControllerTests {

    @MockBean
    private CategoriaRepository categoriaRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private JwtUtils jwtUtils;


    @Test
    @DisplayName("Listar Categorias")
    void CategoriaController_ListarCategorias_ReturnListCategoria() throws Exception {
        when(categoriaRepository.findAll())
                .thenReturn(List.of(new Categoria(1L, "Comida", "")));

        ResultActions response = mockMvc.perform(get("/categoria/listar"));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nombre").value("Comida"))
                .andDo(MockMvcResultHandlers.print());

    }
}
