# PEZUMART - BACKEND

## Ver [Frontend](https://github.com/DAndersonBurga/Pezumart-Frontend)

## Application properties
### Cloudinary
Este proyecto hace uso de cloudinary por lo que debes reemplazar la siguiente configuración con tus credenciales:
```properties
# CLOUDINARY
cloudinary.cloud-name=${CLOUDINARY_CLOUD_NAME="cloudname"}
cloudinary.api-key=${CLOUDINARY_API_KEY="cloudinaryapikey"}
cloudinary.api-secret=${CLOUDINARY_API_SECRET="cloudinaryapisecret"}
```

### JWT
Debes reemplazar la siguiente configuración con un secret-key y un expiration-in-minutes validos:
````properties
# JWT
security.jwt.secret-key=${JWT_SECRET}
security.jwt.expiration-in-minutes=${JWT_EXPIRATION_IN_MINUTES}
````

Por ejemplo:
```properties
# JWT
security.jwt.secret-key=${JWT_SECRET:8C32bLj5gBWwjej8NlRHnV3fZejlLLsM}
security.jwt.expiration-in-minutes=${JWT_EXPIRATION_IN_MINUTES:1440}
```

### Frontend [ver](https://github.com/DAndersonBurga/Pezumart-Frontend)
Debes reemplazar la siguiente configuración con la url del frontend:
```properties
# FRONTEND
frontend.url=${FRONTEND_URL="http://localhost:5173"}
```

### Swagger
Puedes acceder a la documentación de los endpoints con swagger en la siguiente url:
```
http://localhost:8080/api/v1/doc/swagger-ui
```

### Perfiles
Este proyecto tiene dos perfiles de configuración:
- `dev` (default): Perfil de desarrollo
- `tests`: Perfil de testing con base de datos H2
Para cambiar de perfil modifica el:
```properties
spring.profiles.active=dev
```
```properties
spring.profiles.active=tests
```

### Perfil Dev
Este perfil se conecta a una base de datos MySQL, por lo que debes reemplazar la siguiente configuración con tus credenciales:
```properties
# DATASOURCE
spring.datasource.username=${DATABASE_USERNAME="username"}
spring.datasource.password=${DATABASE_PASSWORD="password"}
spring.datasource.url=${DATABASE_URL="jdbc:mysql....."}
```

Puedes modificar estas configuraciones en el archivo `application-dev.properties`
```properties
# JPA
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.defer-datasource-initialization=true
spring.jpa.open-in-view=false
````

Puedes modificar la configuración de inicialización de la base de datos, pero ten en cuenta que la primera vez es necesario, ya que carga algunos datos por defecto como un usuario, los roles, categorías.

```properties
spring.sql.init.mode=always
spring.sql.init.platform=dev
````

### Perfil Tests
Este perfil se conecta a una base de datos H2, por lo que no es necesario modificar ninguna configuración.

```properties
# DATASOURCE
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=password
spring.datasource.driver-class-name=org.h2.Driver

# JPA
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
````

### ¿Comó ejecutar los tests?
Para ejecutar los tests debes cambiar el perfil de configuración a `tests`:
```properties
spring.profiles.active=tests
```
Luego ejecuta el comando:
```shell
./mvnw clean test
```

### ¿Comó ejecutar el proyecto?
Para ejecutar el proyecto debes cambiar el perfil de configuración a `dev`:
```properties
spring.profiles.active=dev
```
Luego ejecuta el comando:
```shell
./mvnw spring-boot:run
```