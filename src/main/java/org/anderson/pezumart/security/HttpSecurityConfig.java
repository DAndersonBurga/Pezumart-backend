package org.anderson.pezumart.security;

import org.anderson.pezumart.entity.enums.ERol;
import org.anderson.pezumart.security.filter.JwtAuthenticationFilter;
import org.anderson.pezumart.security.handler.CustomAccessDeniedHandler;
import org.anderson.pezumart.security.handler.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class HttpSecurityConfig {

    @Autowired
    private AuthenticationProvider daoAuthenticationProvider;

    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Seguridad basada en tokens
            .authenticationProvider(daoAuthenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(authHttp -> {

                // CATEGORÍAS
                authHttp.requestMatchers(HttpMethod.GET, "/categoria/listar").permitAll();

                // PRODUCTOS
                authHttp.requestMatchers(HttpMethod.GET,"/producto/listar").permitAll();
                authHttp.requestMatchers(HttpMethod.GET,"/producto/destacados").permitAll();
                authHttp.requestMatchers(HttpMethod.GET,"/producto/{id}").permitAll();
                authHttp.requestMatchers(HttpMethod.GET,"/producto/ultimos").permitAll();
                authHttp.requestMatchers(HttpMethod.POST, "/producto/buscar").permitAll();
                authHttp.requestMatchers(HttpMethod.POST, "/producto/categoria/{id}").permitAll();

                authHttp.requestMatchers(HttpMethod.GET,"/producto/listar/mis-productos").
                        hasAnyRole(ERol.USUARIO.name(), ERol.ADMINISTRADOR.name());

                authHttp.requestMatchers(HttpMethod.GET,"/producto/actualizar/{id}").
                        hasAnyRole(ERol.USUARIO.name(), ERol.ADMINISTRADOR.name());

                authHttp.requestMatchers(HttpMethod.POST, "/producto/crear")
                        .hasAnyRole(ERol.USUARIO.name(), ERol.ADMINISTRADOR.name());

                authHttp.requestMatchers(HttpMethod.PUT, "/producto/actualizar/{id}")
                        .hasAnyRole(ERol.USUARIO.name(), ERol.ADMINISTRADOR.name());

                authHttp.requestMatchers(HttpMethod.DELETE, "/producto/eliminar/{id}")
                        .hasAnyRole(ERol.USUARIO.name(), ERol.ADMINISTRADOR.name());

                authHttp.requestMatchers(HttpMethod.DELETE, "/producto/destacado/eliminar/{id}")
                        .hasRole(ERol.ADMINISTRADOR.name());

                authHttp.requestMatchers(HttpMethod.POST, "/producto/destacar")
                        .hasRole(ERol.ADMINISTRADOR.name());


                // USUARIOS
                authHttp.requestMatchers(HttpMethod.GET, "/usuario/listar")
                        .hasRole(ERol.ADMINISTRADOR.name());

                authHttp.requestMatchers(HttpMethod.GET, "/usuario/buscar")
                        .hasRole(ERol.ADMINISTRADOR.name());

                authHttp.requestMatchers(HttpMethod.POST, "/usuario/crear")
                        .hasRole(ERol.ADMINISTRADOR.name());

                authHttp.requestMatchers(HttpMethod.GET, "/usuario/{id}")
                        .hasRole(ERol.ADMINISTRADOR.name());

                authHttp.requestMatchers(HttpMethod.DELETE, "/usuario/eliminar/{id}")
                        .hasRole(ERol.ADMINISTRADOR.name());

                authHttp.requestMatchers(HttpMethod.PUT, "/usuario/actualizar/{id}")
                        .hasAnyRole(ERol.ADMINISTRADOR.name());

                // ROLES
                authHttp.requestMatchers(HttpMethod.GET, "/rol/listar")
                        .hasRole(ERol.ADMINISTRADOR.name());

                // AUTENTICACIÓN
                authHttp.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();
                authHttp.requestMatchers(HttpMethod.GET, "/auth/validate").permitAll();
            })
            .exceptionHandling(exception -> {
                exception.accessDeniedHandler(customAccessDeniedHandler);
                exception.authenticationEntryPoint(customAuthenticationEntryPoint);
            })
            .build();
    }

}
