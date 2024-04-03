package org.anderson.pezumart.controllers.api;

import jakarta.validation.Valid;
import org.anderson.pezumart.controllers.request.ActualizarUsuarioRequest;
import org.anderson.pezumart.controllers.request.RegistrarUsuarioRequest;
import org.anderson.pezumart.controllers.response.UsuarioActualizadoResponse;
import org.anderson.pezumart.controllers.response.UsuarioCreadoResponse;
import org.anderson.pezumart.controllers.response.UsuarioEliminadoResponse;
import org.anderson.pezumart.entity.Usuario;
import org.anderson.pezumart.exceptions.UsuarioNotFountException;
import org.anderson.pezumart.repository.UsuarioRepository;
import org.anderson.pezumart.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/listar")
    public ResponseEntity<Page<Usuario>> listarUsuarios(Pageable pageable) {
        Page<Usuario> usuarios = usuarioService.listarUsuarios(pageable);

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/buscar")
    public ResponseEntity<Usuario> obtenerUsuarioPorCoincidenciaDeNombre(@RequestParam("nombre") String nombre) {
        Usuario usuario = usuarioService.buscarUsuarioPorNombre(nombre);

        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFountException("Usuario no encontrado"));

        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/crear")
    public ResponseEntity<UsuarioCreadoResponse> crearUsuario(@RequestPart("file") MultipartFile file
                                          ,@RequestPart("usuario") @Valid RegistrarUsuarioRequest usuario) {
        UsuarioCreadoResponse usuarioCreadoResponse = usuarioService.registrarUsuario(usuario, file);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(usuarioCreadoResponse.getId())
                .toUri();

        return ResponseEntity.created(location).body(usuarioCreadoResponse);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<UsuarioActualizadoResponse> actualizarUsuario(@RequestPart(value = "imagen", required = false) MultipartFile imagen,
                                               @RequestPart("usuario") @Valid ActualizarUsuarioRequest usuarioRequest, @PathVariable Long id) {

        UsuarioActualizadoResponse usuarioActualizadoResponse = usuarioService.actualizarUsuario(usuarioRequest, imagen, id);

        return ResponseEntity.ok(usuarioActualizadoResponse);
    }


    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<UsuarioEliminadoResponse> eliminarUsuario(@PathVariable Long id) {
        UsuarioEliminadoResponse usuarioEliminadoResponse = usuarioService.eliminarUsuario(id);

        return ResponseEntity.ok(usuarioEliminadoResponse);
    }
}
