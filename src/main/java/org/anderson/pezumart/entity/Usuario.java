package org.anderson.pezumart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60, unique = true, name = "nombre_completo")
    private String nombreCompleto;

    @Column(nullable = false, length = 15, unique = true)
    private String telefono;

    @Column(name = "nombre_imagen",length = 100, unique = true)
    private String nombreImagen;

    @Column(name = "imagen_url", length = 300, unique = true)
    private String imagenUrl;

    @Column(length = 100)
    private String direccion;

    @Column(length = 40)
    private String coordenadas;

    @Column(nullable = false, length = 60)
    @JsonIgnore
    private String password;

    @Column(nullable = false, length = 100, unique = true)
    private String correo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol", referencedColumnName = "id", nullable = false)
    private Rol rol;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return Set.of(new SimpleGrantedAuthority("ROLE_" + rol.getRol().name()));
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return correo;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
