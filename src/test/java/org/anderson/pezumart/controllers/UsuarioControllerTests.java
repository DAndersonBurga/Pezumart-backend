package org.anderson.pezumart.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Part;
import org.anderson.pezumart.controllers.api.UsuarioController;
import org.anderson.pezumart.controllers.request.ActualizarUsuarioRequest;
import org.anderson.pezumart.controllers.request.RegistrarUsuarioRequest;
import org.anderson.pezumart.controllers.response.UsuarioActualizadoResponse;
import org.anderson.pezumart.controllers.response.UsuarioCreadoResponse;
import org.anderson.pezumart.controllers.response.UsuarioEliminadoResponse;
import org.anderson.pezumart.entity.Usuario;
import org.anderson.pezumart.repository.UsuarioRepository;
import org.anderson.pezumart.service.UsuarioService;
import org.anderson.pezumart.utils.auth.JwtUtils;
import org.apache.commons.codec.CharEncoding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = UsuarioController.class)
@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private JwtUtils jwtUtils;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder().id(1L).nombreImagen("Anderson").build();
    }

    @Test
    @DisplayName("Listar Usuarios")
    void UsuarioController_ListarUsuarios_ReturnPageUsuario() throws Exception {

        Page<Usuario> usuarios = new PageImpl<>(List.of(usuario, usuario));

        when(usuarioService.listarUsuarios(Mockito.any()))
                .thenReturn(usuarios);

        ResultActions response =  mockMvc.perform(get("/usuario/listar"));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Buscar usuario por coincidencia de nombre")
    void UsuarioController_BuscarUsuarioPorCoincidenciaDeNombre_ReturnUsuario() throws Exception {

        when(usuarioService.buscarUsuarioPorNombre(Mockito.anyString()))
                .thenReturn(usuario);

        ResultActions response = mockMvc.perform(get("/usuario/buscar?nombre=Anderson"));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("Obtener usuario por id")
    void UsuarioController_ObtenerUsuarioPorId() throws Exception {
        when(usuarioRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(usuario));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/usuario/1"));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Crear usuario")
    void UsuarioController_CrearUsuario_ReturnUsuarioCreadoResponse() throws Exception {
        RegistrarUsuarioRequest registrarUsuarioRequest = new RegistrarUsuarioRequest();
        registrarUsuarioRequest.setNombreCompleto("Anderson");
        registrarUsuarioRequest.setCorreo("correo123@gmail.com");
        registrarUsuarioRequest.setPassword("123456");
        registrarUsuarioRequest.setTelefono("51987654321");
        registrarUsuarioRequest.setRol(1);
        registrarUsuarioRequest.setCoordenadas("");
        registrarUsuarioRequest.setDireccion("Calle 123");

        String usuarioJson = objectMapper.writeValueAsString(registrarUsuarioRequest);

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.txt", MediaType.MULTIPART_FORM_DATA_VALUE, "test data".getBytes());

        UsuarioCreadoResponse usuarioCreadoResponse = new UsuarioCreadoResponse(
                1L, "Anderson", "Usuario Creado"
        );

        Part part = new MockPart("usuario", null, usuarioJson.getBytes(), MediaType.APPLICATION_JSON);

        when(usuarioService.registrarUsuario(Mockito.any(RegistrarUsuarioRequest.class), Mockito.any(MultipartFile.class)))
                .thenReturn(usuarioCreadoResponse);


        ResultActions response = mockMvc.perform(multipart("/usuario/crear")
                .file("file", mockMultipartFile.getBytes())
                .part(part));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("Anderson"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensaje").value("Usuario Creado"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Actualizar usuario")
    void UsuarioController_ActualizarUsuario_ReturnUsuarioActualizadoResponse() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.txt", MediaType.MULTIPART_FORM_DATA_VALUE, "test data".getBytes());
        ActualizarUsuarioRequest actualizarUsuarioRequest = new ActualizarUsuarioRequest();
        actualizarUsuarioRequest.setNombreCompleto("Anderson");
        actualizarUsuarioRequest.setDireccion("Calle 123");
        actualizarUsuarioRequest.setTelefono("51987654321");
        actualizarUsuarioRequest.setCoordenadas("");

        String usuarioJson = objectMapper.writeValueAsString(actualizarUsuarioRequest);
        Part part = new MockPart("usuario", null, usuarioJson.getBytes(), MediaType.APPLICATION_JSON);

        UsuarioActualizadoResponse usuarioActualizadoResponse = new UsuarioActualizadoResponse(
                "Anderson", "Usuario Actualizado", "imagen.jpg"
        );

        when(usuarioService.actualizarUsuario(Mockito.any(ActualizarUsuarioRequest.class), Mockito.any(MultipartFile.class), Mockito.anyLong()))
                .thenReturn(usuarioActualizadoResponse);

        ResultActions response = mockMvc.perform(multipart(HttpMethod.PUT, "/usuario/actualizar/1")
                .file("imagen", mockMultipartFile.getBytes())
                .part(part));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombreCompleto").value("Anderson"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensaje").value("Usuario Actualizado"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.imagenUrl").value("imagen.jpg"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Eliminar usuario")
    void UsuarioController_EliminarUsuario_ReturnUsuarioEliminadoResponse() throws Exception {
        UsuarioEliminadoResponse usuarioEliminadoResponse = new UsuarioEliminadoResponse(
                "Anderson", 1L, "Usuario Eliminado", LocalDateTime.now()
        );

        when(usuarioService.eliminarUsuario(Mockito.anyLong()))
                .thenReturn(usuarioEliminadoResponse);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/usuario/eliminar/1"));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombreCompleto").value("Anderson"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensaje").value("Usuario Eliminado"))
                .andDo(MockMvcResultHandlers.print());
    }
}
