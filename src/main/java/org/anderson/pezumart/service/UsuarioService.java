package org.anderson.pezumart.service;

import org.anderson.pezumart.controllers.request.UsuarioRequest;
import org.anderson.pezumart.controllers.response.UsuarioCreadoResponse;
import org.anderson.pezumart.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UsuarioService {
    UsuarioCreadoResponse registrarUsuario(UsuarioRequest usuarioRequest, MultipartFile file);
    Page<Usuario> listarUsuarios(Pageable pageable);
    Usuario buscarUsuarioPorCorreo(String correo);
}
