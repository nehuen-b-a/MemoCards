# MemoCards

MemoCards es una aplicación diseñada para estudiantes que desean mejorar sus habilidades de memorización utilizando tarjetas virtuales (flashcards). La plataforma organiza y optimiza el estudio mediante un sistema de **repetición espaciada** adaptado a la **curva del olvido**, lo que permite trasladar la información de la memoria a corto plazo a la memoria a largo plazo.

## Estructura del Repositorio

El repositorio está organizado de la siguiente manera:

```plaintext
MemoCards/
├── documentation/               # Documentación y diagramas del proyecto
│   ├── MemoCards_Requerimientos.pdf
│   └── diagrams/
│       └── DiagramaDeClases.puml
├── src/                          # Código fuente principal del proyecto
│   ├── main/
│   │   ├── java/
│   │   │   └── neh/memocards/    # Paquete principal del proyecto
│   │   │       ├── controllers/ # Controladores que gestionan las solicitudes HTTP
│   │   │       ├── domain/      # Lógica de negocio
│   │   │       │   ├── entities/
│   │   │       │   │   ├── estudio/      # Entidades relacionadas con el estudio
│   │   │       │   │   ├── memocard/     # Entidades relacionadas con las tarjetas
│   │   │       │   │   └── usuarios/     # Entidades relacionadas con usuarios y roles
│   │   │       │   └── validador/        # Validadores para contraseñas y otras reglas
│   │   │       ├── dtos/          # Objetos de transferencia de datos
│   │   │       └── view/          # Lógica relacionada con la presentación
│   │   └── resources/
│   │       ├── application.properties  # Configuración de Spring Boot
│   │       ├── public/            # Recursos estáticos (HTML, CSS, imágenes)
│   │       ├── static/            # Archivos estáticos
│   │       └── templates/         # Plantillas de la vista (si se utilizan)
│   └── test/
│       └── java/neh/memocards/    # Pruebas unitarias y de integración
├── .idea/                        # Configuración del IDE
├── .mvn/                         # Configuración de Maven Wrapper
├── target/                       # Archivos generados al compilar el proyecto
├── pom.xml                       # Configuración y dependencias de Maven
├── README.md                     # Documentación principal
└── .gitignore                    # Archivos y carpetas ignorados por Git
```

## Instalación

Para ejecutar el proyecto localmente, sigue los siguientes pasos:

1. Clona el repositorio:

   ```bash
   git clone https://github.com/usuario/MemoCards.git
   cd MemoCards
   ```

2. Configura la base de datos SQL Server:
   - Crea una base de datos para MemoCards.
   - Actualiza el archivo `application.properties` con las credenciales y configuraciones de la base de datos.

3. Compila y ejecuta el proyecto:

   ```bash
   mvn spring-boot:run
   ```

4. Abre [http://localhost:8080](http://localhost:8080) para ver la aplicación en tu navegador.

## Características Principales

### 1. Arquitectura MVC
- **Modelo**: Implementado con Spring Data JPA para interactuar con SQL Server.
- **Vista**: Generada de forma dinámica (HTML, CSS, JS y Bootstrap).
- **Controlador**: Maneja las solicitudes HTTP y expone APIs RESTful.

### 2. Sistema de Intervalos Personalizados
- Configuración de intervalos iniciales en la fase de aprendizaje (15 minutos, 1 día, 3 días).
- Ajuste dinámico de intervalos en función del rendimiento del usuario.

### 3. Seguridad
- Implementación de autenticación y autorización con Spring Security.
- Cifrado de contraseñas utilizando BCrypt.

### 4. Interfaz Dinámica con AJAX
- Uso de AJAX para actualizar datos en tiempo real sin recargar la página.

### 5. Reportes y Estadísticas
- Métricas clave: tarjetas repasadas, retención de información, niveles de dificultad.

## Pruebas

Para ejecutar las pruebas unitarias:

```bash
mvn test
```

## Contribución

¡Las contribuciones son bienvenidas! Sigue estos pasos:

1. Haz un **fork** del repositorio.
2. Crea una rama para tu funcionalidad o corrección.
3. Realiza tus cambios y haz un commit.
4. Envía un **Pull Request**.

## Tecnologías Utilizadas

- **Backend**: Java con Spring Boot.
- **Base de Datos**: SQL Server.
- **Frontend**: HTML, CSS, Bootstrap y JavaScript (AJAX).

## Licencia

Este proyecto está bajo la licencia MIT. Consulta el archivo `LICENSE` para más detalles.

---

