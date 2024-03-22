package org.anderson.pezumart.entity;

import jakarta.persistence.*;
import lombok.*;
import org.anderson.pezumart.entity.enums.ERol;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private ERol rol;
}
