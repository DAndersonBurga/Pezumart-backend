package org.anderson.pezumart.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Part;
import org.anderson.pezumart.controllers.api.ProductoController;
import org.anderson.pezumart.controllers.request.ActualizarProductoRequest;
import org.anderson.pezumart.controllers.request.CrearProductoRequest;
import org.anderson.pezumart.controllers.response.ActualizarProductoResponse;
import org.anderson.pezumart.controllers.response.CrearProductoResponse;
import org.anderson.pezumart.controllers.response.ProductoEliminadoResponse;
import org.anderson.pezumart.controllers.response.ProductoResponse;
import org.anderson.pezumart.entity.ProductoDestacado;
import org.anderson.pezumart.repository.ProductoDestacadoRepository;
import org.anderson.pezumart.repository.ProductoRepository;
import org.anderson.pezumart.repository.projections.MiProductoView;
import org.anderson.pezumart.repository.projections.ProductoView;
import org.anderson.pezumart.service.ProductoService;
import org.anderson.pezumart.service.UsuarioService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = ProductoController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ProductoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductoRepository productoRepository;

    @MockBean
    private ProductoDestacadoRepository productoDestacadoRepository;

    @MockBean
    private ProductoService productoService;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private JwtUtils jwtUtils;

    @Test
    @DisplayName("Listar productos")
    void ProductoController_ListarProductos_Return() throws Exception {

        Page<ProductoView> productoViews = new PageImpl<>(List.of());

        when(productoRepository.findAllProjectedBy(Mockito.any()))
                .thenReturn(productoViews);

        ResultActions response = mockMvc.perform(get("/producto/listar"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Listar ultimos 8 productos")
    void ProductoController_ListarUltimos8Productos_ReturnListProductoView() throws Exception {
        List<ProductoView> productoViews = List.of();

        when(productoService.obtenerUltimos8Productos())
                .thenReturn(productoViews);

        ResultActions response = mockMvc.perform(get("/producto/ultimos"));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Listar productos del usuario autenticado")
    void ProductoController_ListarProductosDelUsuarioAutenticado_ReturnPageMiProductoView() throws Exception {
        Page<MiProductoView> productoViews = new PageImpl<>(List.of());

        when(productoService.buscarProductosDelUsuarioAutenticado(Mockito.any()))
                .thenReturn(productoViews);

        ResultActions response = mockMvc.perform(get("/producto/listar/mis-productos"));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Obtener producto por id")
    void ProductoController_ObtenerProductoPorId_ReturnProductoResponse() throws Exception {
        ProductoResponse productoResponse = ProductoResponse.builder()
                .id(1L)
                .nombre("Producto 1")
                .precio(100.0)
                .build();

        when(productoService.obtenerProductoPorId(1L))
                .thenReturn(productoResponse);

        ResultActions response = mockMvc.perform(get("/producto/1"));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre", CoreMatchers.is("Producto 1")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Crear producto")
    void ProductoController_CrearProducto_ReturnCrearProductoResponse() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("files", "test.txt", MediaType.MULTIPART_FORM_DATA_VALUE, "test data".getBytes());

        CrearProductoRequest crearProductoRequest = new CrearProductoRequest();
        crearProductoRequest.setNombre("Producto 1");
        crearProductoRequest.setCategoriaId(1L);
        crearProductoRequest.setDescripcion("Descripción del producto 1");
        crearProductoRequest.setDisponible(true);
        crearProductoRequest.setPrecio(100.0);
        crearProductoRequest.setCantidadDisponible(10);

        String crearProductoRequestJson = objectMapper.writeValueAsString(crearProductoRequest);

        CrearProductoResponse crearProductoResponse = new CrearProductoResponse(
                1L, "Producto 1", "Producto Creado"
        );
        Part part = new MockPart("producto", null, crearProductoRequestJson.getBytes(), MediaType.APPLICATION_JSON);

        when(productoService.crearProducto(Mockito.any(CrearProductoRequest.class), Mockito.any()))
                .thenReturn(crearProductoResponse);


        ResultActions response = mockMvc.perform(multipart(HttpMethod.POST,"/producto/crear")
                .file("files", mockMultipartFile.getBytes())
                .file("files", mockMultipartFile.getBytes())
                .part(part));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombreProducto", CoreMatchers.is("Producto 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensaje", CoreMatchers.is("Producto Creado")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Actualizar Producto")
    void ProductoController_ActualizarProducto_ReturnActualizarProductoResponse() throws Exception {
        ActualizarProductoRequest actualizarProductoRequest = new ActualizarProductoRequest();
        actualizarProductoRequest.setNombre("Producto 1");
        actualizarProductoRequest.setCategoriaId(1L);
        actualizarProductoRequest.setDescripcion("Descripción del producto 1");
        actualizarProductoRequest.setDisponible(true);
        actualizarProductoRequest.setPrecio(100);
        actualizarProductoRequest.setCantidadDisponible(10);

        ActualizarProductoResponse actualizarProductoResponse = new ActualizarProductoResponse(
                1L, "Producto 1", "Producto Actualizado"
        );

        when(productoService.actualizarProducto(Mockito.anyLong(), Mockito.any(ActualizarProductoRequest.class)))
                .thenReturn(actualizarProductoResponse);


        ResultActions response = mockMvc.perform(put("/producto/actualizar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizarProductoRequest)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre", CoreMatchers.is("Producto 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensaje", CoreMatchers.is("Producto Actualizado")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Eliminar Producto")
    void ProductoController_EliminarProducto_ReturnProductoEliminadoResponse() throws Exception {
        ProductoEliminadoResponse productoEliminadoResponse = new ProductoEliminadoResponse(
                "Producto Eliminado"
        );

        when(productoService.eliminarProducto(Mockito.anyLong()))
                .thenReturn(productoEliminadoResponse);

        ResultActions response = mockMvc.perform(delete("/producto/eliminar/1"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensaje", CoreMatchers.is("Producto Eliminado")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Buscar producto por nombre")
    void ProductoController_BuscarProductoPorNombre() throws Exception {
        Page<ProductoView> productoViews = new PageImpl<>(List.of());

        when(productoService.buscarProductoPorNombre(Mockito.any(), Mockito.anyString()))
                .thenReturn(productoViews);

        ResultActions response = mockMvc.perform(get("/producto/buscar")
                .param("nombre", "Producto 1"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Listar productos destacados")
    void ProductoController_ListarProductosDestacados_ReturnListProductoDestacado() throws Exception {
        ProductoDestacado productoDestacado = ProductoDestacado.builder()
                .id(1L)
                .build();
        List<ProductoDestacado> productoDestacados = List.of(productoDestacado);

       when(productoDestacadoRepository.findAll()).thenReturn(productoDestacados);

        ResultActions response = mockMvc.perform(get("/producto/destacados"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", CoreMatchers.is(1)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Destacar producto")
    void ProductoController_CrearProductoDestacado_ReturnMap() throws Exception {
        Map<String, Long> productoDestacado = Map.of("productoId", 1L);

        when(productoService.descatarProducto(Mockito.anyLong()))
                .thenReturn("Producto Destacado");

        ResultActions response = mockMvc.perform(post("/producto/destacar")
                .content(objectMapper.writeValueAsString(productoDestacado))
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensaje", CoreMatchers.is("Producto Destacado")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Eliminar producto destacado")
    void ProductoController_EliminarProductoDestacado_ReturnMap() throws Exception {

        when(productoService.eliminarProductoDestacado(Mockito.anyLong()))
                .thenReturn("Producto Destacado Eliminado");

        ResultActions response = mockMvc.perform(delete("/producto/destacado/eliminar/1"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensaje", CoreMatchers.is("Producto Destacado Eliminado")))
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("Obtener productos por categoria id")
    @Test
    void ProductoController_ObtenerProductosPorCategoriaId_ReturnPageProductoView() throws Exception {
        Page<ProductoView> productoViews = new PageImpl<>(List.of());

        when(productoService.obtenerProductosPorCategoria(Mockito.any(), Mockito.anyLong()))
                .thenReturn(productoViews);

        ResultActions response = mockMvc.perform(get("/producto/categoria/1"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
