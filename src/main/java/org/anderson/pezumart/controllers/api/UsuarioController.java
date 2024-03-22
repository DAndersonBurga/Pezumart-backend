package org.anderson.pezumart.controllers.api;

import jakarta.validation.Valid;
import org.anderson.pezumart.controllers.request.UsuarioRequest;
import org.anderson.pezumart.controllers.response.UsuarioCreadoResponse;
import org.anderson.pezumart.entity.Usuario;
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

    @GetMapping("/listar")
    public ResponseEntity<?> listarUsuarios(Pageable pageable) {
        Page<Usuario> usuarios = usuarioService.listarUsuarios(pageable);


        return ResponseEntity.ok(usuarios);
    }

    // Falta implementar
    @GetMapping("/buscar")
    public ResponseEntity<?> obtenerUsuarioPorCoincidenciaDeNombre(@RequestParam("query") String query) {

        return ResponseEntity.ok("Usuario");
    }

    // Falta implementar
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerUsuarioPorId(@PathVariable Long id) {

        return ResponseEntity.ok("Hola");
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearUsuario(@RequestPart("file") MultipartFile file
                                          ,@RequestPart("usuario") @Valid UsuarioRequest usuario) {
        UsuarioCreadoResponse usuarioCreadoResponse = usuarioService.registrarUsuario(usuario, file);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(usuarioCreadoResponse.getId())
                .toUri();

        return ResponseEntity.created(location).body(usuarioCreadoResponse);
    }
}
