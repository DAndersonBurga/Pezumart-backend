package org.anderson.pezumart.security;

import org.anderson.pezumart.exceptions.UsuarioNotFountException;
import org.anderson.pezumart.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Configuration
public class SecurityBeansInjector {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration; // Clase provehída he inyectada por spring security

    @Bean // Manejador de autenticación, se encarga de elegir el proveedor de autenticación a usar
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // Authentication manager por defecto
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder( passwordEncoder() );
        daoAuthenticationProvider.setUserDetailsService( userDetailsService() );

        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public UserDetailsService userDetailsService() {
        return (username) ->
                usuarioRepository.findByCorreo(username)
                .orElseThrow(() -> new UsuarioNotFountException("Usuario no encontrado"));
    }

}
