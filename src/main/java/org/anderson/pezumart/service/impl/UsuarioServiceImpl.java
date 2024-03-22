package org.anderson.pezumart.service.impl;

import org.anderson.pezumart.controllers.request.UsuarioRequest;
import org.anderson.pezumart.controllers.response.UsuarioCreadoResponse;
import org.anderson.pezumart.entity.Rol;
import org.anderson.pezumart.entity.Usuario;
import org.anderson.pezumart.exceptions.*;
import org.anderson.pezumart.repository.RolRepository;
import org.anderson.pezumart.repository.UsuarioRepository;
import org.anderson.pezumart.service.CloudinaryService;
import org.anderson.pezumart.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UsuarioCreadoResponse registrarUsuario(UsuarioRequest usuarioRequest, MultipartFile file) {

         Optional<Usuario> usuarioOptional = usuarioRepository.findByCorreo(usuarioRequest.getCorreo());
         if(usuarioOptional.isPresent()) {
             throw new UsuarioExistsException("El usuario ya existe.");
         }

        Long idRol = (long) usuarioRequest.getRol();
        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() -> new RolNotFoundException("El rol no fue encontrado."));

        if(file == null || file.isEmpty()) {
            throw new EmptyImageException("La imagen no puede estar vacía.");
        }

        String fileName = file.getOriginalFilename();
        List<String> extencionesPermitidas = List.of("jpg", "jpeg", "png");
        String extencionFile = fileName.substring(fileName.lastIndexOf(".") + 1);


        if(!extencionesPermitidas.contains(extencionFile)) {
            throw new ImageExtensionNotSupportedException("La imagen debe ser de tipo jpg, jpeg o png.");
        }

        Map<String, String> imagenMap = cloudinaryService.uploadImage(file, "usuarios");
        String publicIdImagen = imagenMap.get("publicId");
        String imagenUrl = imagenMap.get("url");

        Usuario usuario = Usuario.builder()
                .nombreCompleto(usuarioRequest.getNombreCompleto())
                .telefono(usuarioRequest.getTelefono())
                .password(passwordEncoder.encode(usuarioRequest.getPassword()))
                .direccion(usuarioRequest.getDireccion())
                .nombreImagen(publicIdImagen)
                .imagenUrl(imagenUrl)
                .correo(usuarioRequest.getCorreo())
                .rol(rol)
                .coordenadas(usuarioRequest.getCoordenadas())
                .build();


        usuarioRepository.save(usuario);

        return new UsuarioCreadoResponse(usuario.getId(),usuario.getCorreo(), "Usuario creado exitosamente.");
    }

    @Override
    public Page<Usuario> listarUsuarios(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    @Override
    public Usuario buscarUsuarioPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsuarioNotFountException("Usuario no encontrado."));
    }
}
