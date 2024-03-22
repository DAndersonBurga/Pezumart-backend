INSERT INTO rol (id, rol) VALUES ( 1, 'ADMINISTRADOR'),
                                 (2, 'USUARIO');

INSERT INTO categoria (id, nombre) VALUES (1, 'Tecnología'),
                                         (2, 'Hogar'),
                                         (3, 'Deporte'),
                                         (4, 'Moda'),
                                         (5, 'Mascotas'),
                                         (6, 'Otros');

INSERT INTO usuario (id, nombre_completo, telefono, nombre_imagen, imagen_url, coordenadas, password, correo, id_rol)
VALUES (1, 'Anderson Daniel Burga Davila', '51987654321', 'promax',
        'https://avatars.akamai.steamstatic.com/151aefd44798d4e20b7cd9de1968c3add900f8ed_full.jpg', '',
        '$2a$12$DebRIgwfYo55jXiFOodUqulynxahiAYfK3tvZF.H4Z6NxM7.Tw45K', 'promax123boom@gmail.com', 1)