package org.anderson.pezumart.service.impl;

import org.anderson.pezumart.controllers.response.UsuarioEliminadoResponse;
import org.anderson.pezumart.controllers.request.ActualizarUsuarioRequest;
import org.anderson.pezumart.controllers.request.RegistrarUsuarioRequest;
import org.anderson.pezumart.controllers.response.UsuarioActualizadoResponse;
import org.anderson.pezumart.controllers.response.UsuarioCreadoResponse;
import org.anderson.pezumart.entity.*;
import org.anderson.pezumart.exceptions.*;
import org.anderson.pezumart.repository.*;
import org.anderson.pezumart.service.CloudinaryService;
import org.anderson.pezumart.service.UsuarioService;
import org.anderson.pezumart.utils.auth.UsuarioAutenticadoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ImagenProductoRepository imagenProductoRepository;

    @Autowired
    private ProductoDestacadoRepository productoDestacadoRepository;

    @Override
    public UsuarioCreadoResponse registrarUsuario(RegistrarUsuarioRequest registrarUsuarioRequest, MultipartFile file) {

         Optional<Usuario> usuarioOptional = usuarioRepository.findByCorreo(registrarUsuarioRequest.getCorreo());
         if(usuarioOptional.isPresent()) {
             throw new UsuarioExistsException("El usuario ya existe.");
         }

        Long idRol = (long) registrarUsuarioRequest.getRol();
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
                .nombreCompleto(registrarUsuarioRequest.getNombreCompleto())
                .telefono(registrarUsuarioRequest.getTelefono())
                .password(passwordEncoder.encode(registrarUsuarioRequest.getPassword()))
                .direccion(registrarUsuarioRequest.getDireccion())
                .nombreImagen(publicIdImagen)
                .imagenUrl(imagenUrl)
                .correo(registrarUsuarioRequest.getCorreo())
                .rol(rol)
                .coordenadas(registrarUsuarioRequest.getCoordenadas())
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

    @Override
    public Usuario buscarUsuarioPorNombre(String nombre) {
        return usuarioRepository.findByNombreCompletoContainingIgnoreCase(nombre)
                .orElseThrow(() -> new UsuarioNotFountException("Usuario no encontrado."));
    }

    @Override
    public UsuarioActualizadoResponse actualizarUsuario(ActualizarUsuarioRequest actualizarUsuarioRequest, MultipartFile file, Long id) {

        Usuario usuarioDB = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFountException("Usuario no encontrado."));

        if(file != null && !file.isEmpty()) {
            cloudinaryService.deleteImage(usuarioDB.getNombreImagen());

            Map<String, String> imagenData = cloudinaryService.uploadImage(file, "usuarios");

            String publicIdImagen = imagenData.get("publicId");
            String imagenUrl = imagenData.get("url");

            usuarioDB.setNombreImagen(publicIdImagen);
            usuarioDB.setImagenUrl(imagenUrl);

        }

        usuarioDB.setNombreCompleto(actualizarUsuarioRequest.getNombreCompleto());
        usuarioDB.setTelefono(actualizarUsuarioRequest.getTelefono());
        usuarioDB.setDireccion(actualizarUsuarioRequest.getDireccion());
        usuarioDB.setCoordenadas(actualizarUsuarioRequest.getCoordenadas());

        usuarioRepository.save(usuarioDB);

        return UsuarioActualizadoResponse.builder()
                .nombreCompleto(usuarioDB.getNombreCompleto())
                .imagenUrl(usuarioDB.getImagenUrl())
                .mensaje("Usuario actualizado " + usuarioDB.getNombreCompleto() + " exitosamente.")
                .build();
    }

    @Override
    public UsuarioEliminadoResponse eliminarUsuario(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFountException("Usuario no encontrado."));

        List<Producto> productos = productoRepository.findAllByUsuarioId(id);
        List<List<ImagenProducto>> imagenesProductos = productos.stream()
                .map(producto -> imagenProductoRepository.findAllByProductoId(producto.getId()))
                .toList();

        imagenesProductos.forEach(imagenProductos -> {
            imagenProductos.forEach(imagenProducto -> {
                cloudinaryService.deleteImage(imagenProducto.getNombreImagen());
            });

            imagenProductoRepository.deleteAll(imagenProductos);
        });

        productoRepository.deleteAll(productos);

        cloudinaryService.deleteImage(usuario.getNombreImagen());
        usuarioRepository.delete(usuario);

        return UsuarioEliminadoResponse.builder()
                .fecha(LocalDateTime.now())
                .id(id)
                .mensaje("Usuario eliminado exitosamente.")
                .nombreCompleto(usuario.getNombreCompleto())
                .build();
    }
}
