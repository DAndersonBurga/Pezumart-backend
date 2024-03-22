package org.anderson.pezumart.service.auth;

import org.anderson.pezumart.controllers.request.LoginRequest;
import org.anderson.pezumart.controllers.response.LoginResponse;
import org.anderson.pezumart.entity.Usuario;
import org.anderson.pezumart.service.UsuarioService;
import org.anderson.pezumart.utils.auth.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioService usuarioService;

    private Map<String, Object> generateExtraClaims(Usuario usuario) {

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("nombre", usuario.getNombreCompleto());
        extraClaims.put("rol", usuario.getRol().getRol().name()); // Es un Enum y debe convertirse a string

        return extraClaims;
    }


    public LoginResponse login(LoginRequest loginRequest) {

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginRequest.getCorreo(),
                loginRequest.getPassword()
        );

        authenticationManager.authenticate(authentication);

        // Traer el usuario de la base de datos y generar el token jwt
        Usuario usuario = usuarioService.buscarUsuarioPorCorreo(loginRequest.getCorreo());
        String jwt = jwtUtils.generarToken(usuario, generateExtraClaims(usuario));

        return new LoginResponse(jwt);
    }

    public boolean validateToken(String jwt) {
        try {
            jwtUtils.extraerUsername(jwt);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
