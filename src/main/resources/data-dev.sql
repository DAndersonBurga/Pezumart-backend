INSERT INTO rol (id, rol) VALUES ( 1, 'ADMINISTRADOR'),
                                 (2, 'USUARIO');

INSERT INTO categoria (id, nombre, imagen) VALUES (1, 'Tecnología', 'https://media.istockphoto.com/id/1488294044/es/foto/el-empresario-trabaja-en-una-computadora-port%C3%A1til-mostrando-un-panel-de-an%C3%A1lisis-de-negocios.webp?b=1&s=612x612&w=0&k=20&c=KqRI6iHb1zI9OjWEGkRx5Skq--TNSzSy3HqNMeC_Rps='),
                                         (2, 'Hogar', 'https://media.istockphoto.com/id/1487766414/es/foto/el-luminoso-dise%C3%B1o-interior-de-la-sala-de-estar-con-ventanas-y-paredes-beige-est%C3%A1-amueblado.webp?b=1&s=612x612&w=0&k=20&c=Dz4-wROqXBiBZ9-6JgTdJfVdB8EhFiy6JPO0rXCzIOg='),
                                         (3, 'Deporte', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR9oB2SVj0mPVlwiQP5ORMx068IDcILh1V16Av1Itry7w&s'),
                                         (4, 'Moda', 'https://www.chio-lecca.edu.pe/cdn/shop/articles/chio-lecca-blog-estilo-y-moda.jpg?v=1673918894'),
                                         (5, 'Comida', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTcWencAc-MEb5kK4wuadjEjBf6J1bukwtQOzuiu2ACsQ&s'),
                                         (6, 'Otros', 'https://i0.wp.com/get.witei.com/wp-content/uploads/2022/04/cartera-de-productos-ejemplos.jpg?fit=1200%2C675&ssl=1');

INSERT INTO usuario (id, nombre_completo, telefono, nombre_imagen, imagen_url, coordenadas, password, correo, id_rol)
VALUES (1, 'Anderson', '51987654321', 'promax',
        'https://avatars.akamai.steamstatic.com/151aefd44798d4e20b7cd9de1968c3add900f8ed_full.jpg', '',
        '$2y$10$EqBr.jYMlEVQjbgMP.KFQuWHQwPP9MyyWkvH0eBh901DO8uFFdzbe', 'pepito123@gmail.com', 1)
