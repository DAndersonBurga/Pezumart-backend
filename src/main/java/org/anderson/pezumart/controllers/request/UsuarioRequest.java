package org.anderson.pezumart.controllers.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Getter
@Setter
public class UsuarioRequest implements Serializable {

    @NotBlank
    @Length(max = 60)
    private String nombreCompleto;

    @NotBlank
    @Length(max = 15)
    private String telefono;

    @NotBlank
    @Email(regexp = "[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*@[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*[.][a-zA-Z]{2,5}")
    @Length(max = 100)
    private String correo;

    @NotBlank
    @Length(max = 60)
    private String password;

    @Min(1)
    private int rol;

    @NotBlank
    @Length(max = 100)
    private String direccion;

    @Length(max = 40)
    private String coordenadas;

}
