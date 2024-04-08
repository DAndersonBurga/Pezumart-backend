package org.anderson.pezumart.service;

import org.anderson.pezumart.controllers.response.UsuarioEliminadoResponse;
import org.anderson.pezumart.controllers.request.ActualizarUsuarioRequest;
import org.anderson.pezumart.controllers.request.RegistrarUsuarioRequest;
import org.anderson.pezumart.controllers.response.UsuarioActualizadoResponse;
import org.anderson.pezumart.controllers.response.UsuarioCreadoResponse;
import org.anderson.pezumart.entity.ProductoDestacado;
import org.anderson.pezumart.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UsuarioService {
    UsuarioCreadoResponse registrarUsuario(RegistrarUsuarioRequest registrarUsuarioRequest, MultipartFile file);
    Page<Usuario> listarUsuarios(Pageable pageable);
    Usuario buscarUsuarioPorCorreo(String correo);
    Usuario buscarUsuarioPorNombre(String nombre);
    UsuarioActualizadoResponse actualizarUsuario(ActualizarUsuarioRequest actualizarUsuarioRequest, MultipartFile file, Long id);
    UsuarioEliminadoResponse eliminarUsuario(Long id);
}
