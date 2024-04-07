package org.anderson.pezumart.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_categoria", referencedColumnName = "id", nullable = false)
    private Categoria categoria;

    @Column(nullable = false, length = 30)
    private String nombre;

    @Column(nullable = false ,length = 300)
    private String descripcion;

    @Column(nullable = false)
    private boolean disponible;

    @Column(nullable = false)
    @Check(constraints = "precio > 0")
    private double precio;

    @Column(name = "cantidad_disponible")
    @Check(constraints = "cantidad_disponible >= 0")
    private int cantidadDisponible;

    @Column(name = "fecha_creacion")
    @CreationTimestamp
    private LocalDateTime fechaCreacion;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "producto")
    private List<ImagenProducto> imagenes;
}
