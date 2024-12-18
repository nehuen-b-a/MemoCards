# MemoCards

MemoCards es una aplicación diseñada para estudiantes que desean mejorar sus habilidades de memorización utilizando tarjetas virtuales (flashcards). La plataforma organiza y optimiza el estudio mediante un sistema de **repetición espaciada** adaptado a la **curva del olvido**, lo que permite trasladar la información de la memoria a corto plazo a la memoria a largo plazo.

## Estructura del Repositorio

El repositorio está organizado de la siguiente manera:

```plaintext
MemoCards/
|-- src/                     # Código fuente principal del proyecto
|   |-- model/               # Lógica de datos y comunicación con SQL Server
|   |-- view/                # Vistas HTML generadas con Handlebars y Bootstrap
|   |-- controller/          # Controladores que manejan solicitudes HTTP
|   |-- application/         # Configuración principal de Spring Boot
|-- public/                  # Archivos estáticos públicos
|-- docs/                    # Documentación adicional del proyecto
|-- tests/                   # Pruebas unitarias y de integración
|-- pom.xml                  # Dependencias y configuración de Maven
|-- README.md                # Documentación principal del repositorio
|-- .gitignore               # Archivos y carpetas ignoradas por Git
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
- **Vista**: Generada de forma dinámica con Handlebars y Bootstrap.
- **Controlador**: Maneja las solicitudes HTTP, expone APIs RESTful y coordina entre el modelo y la vista.

### 2. Sistema de Intervalos Personalizados
- Configuración de intervalos iniciales en la fase de aprendizaje (15 minutos, 1 día, 3 días).
- Ajuste dinámico de intervalos en función del rendimiento del usuario.
- Manejo automático de estados:
  - **Aprendizaje**: Tarjetas nuevas con intervalos cortos.
  - **Repaso**: Intervalos crecientes para tarjetas memorizadas.
  - **Reaprendizaje**: Reinicio del proceso para tarjetas olvidadas.

### 3. Seguridad
- Implementación de autenticación y autorización con Spring Security.
- Cifrado de contraseñas utilizando BCrypt.
- Validación de entradas para prevenir ataques de SQL Injection y CSRF.

### 4. Interfaz Dinámica con AJAX
- Uso de AJAX para actualizar datos en tiempo real sin recargar la página.
- Respuesta rápida y eficiente para acciones como marcar tarjetas como "Correcta" o "Olvidada".

### 5. Reportes y Estadísticas
- Visualización del **progreso diario** y **general**.
- Clasificación de tarjetas según su estado:
  - Nuevas (Aprendizaje)
  - Estudiando (en proceso de repaso)
  - Reaprendiendo
- Métricas clave:
  - Tarjetas repasadas.
  - Retención de información.
  - Niveles de dificultad.

## Pruebas

Las pruebas se encuentran en la carpeta `tests/`. Para ejecutar las pruebas unitarias, utiliza el siguiente comando:

```bash
mvn test
```

## Contribución

¡Las contribuciones son bienvenidas! Para contribuir a MemoCards:

1. Haz un **fork** del repositorio.
2. Crea una rama para tu funcionalidad o corrección:

   ```bash
   git checkout -b feature/nueva-funcionalidad
   ```

3. Realiza tus cambios y haz un commit:

   ```bash
   git commit -m "Añadir nueva funcionalidad X"
   ```

4. Envía un **Pull Request**.

## Tecnologías Utilizadas

- **Backend**: Java con Spring Boot (Spring Data JPA, Spring Security).
- **Base de Datos**: SQL Server.
- **Frontend**: Handlebars, Bootstrap, HTML, CSS y JavaScript (AJAX).

## Licencia

Este proyecto está bajo la licencia MIT. Consulta el archivo `LICENSE` para más detalles.

---

Desarrollado para mejorar el aprendizaje y optimizar el estudio mediante un enfoque científico y personalizable. ¡Estudia más inteligentemente con MemoCards! 📚
