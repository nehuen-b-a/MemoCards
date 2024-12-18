# MemoCards

MemoCards es una aplicación diseñada para estudiantes que desean mejorar sus habilidades de memorización utilizando tarjetas virtuales (flashcards). La plataforma organiza y optimiza el estudio mediante un sistema de **repetición espaciada** adaptado a la **curva del olvido**, lo que permite trasladar la información de la memoria a corto plazo a la memoria a largo plazo.

## Estructura del Repositorio

El repositorio está organizado de la siguiente manera:

```plaintext
MemoCards/
|-- src/                     # Código fuente principal del proyecto
|   |-- components/          # Componentes reutilizables del frontend
|   |-- services/            # Lógica de negocio y manejo de datos
|   |-- utils/               # Utilidades y funciones auxiliares
|   |-- App.js               # Punto de entrada principal de la aplicación
|-- public/                  # Archivos estáticos públicos
|-- docs/                    # Documentación adicional del proyecto
|-- tests/                   # Pruebas unitarias y de integración
|-- package.json             # Dependencias y scripts del proyecto
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

2. Instala las dependencias necesarias:

   ```bash
   npm install
   ```

3. Ejecuta la aplicación en modo desarrollo:

   ```bash
   npm start
   ```

4. Abre [http://localhost:3000](http://localhost:3000) para ver la aplicación en tu navegador.

## Características Principales

### 1. Gestión de Mazos y Tarjetas
- **CRUD**: Creación, lectura, actualización y eliminación de mazos y tarjetas.
- Configuración personalizada del contenido: preguntas y respuestas.
- Organización de tarjetas por **categorías** o **etiquetas**.

### 2. Sistema de Intervalos Personalizados
- Configuración de intervalos iniciales en la fase de aprendizaje (15 minutos, 1 día, 3 días).
- Ajuste dinámico de intervalos en función del rendimiento del usuario.
- Manejo automático de estados:
  - **Aprendizaje**: Tarjetas nuevas con intervalos cortos.
  - **Repaso**: Intervalos crecientes para tarjetas memorizadas.
  - **Reaprendizaje**: Reinicio del proceso para tarjetas olvidadas.

### 3. Reportes y Estadísticas
- Visualización del **progreso diario** y **general**.
- Clasificación de tarjetas según su estado:
  - Nuevas (Aprendizaje)
  - Estudiando (en proceso de repaso)
  - Reaprendiendo
- Métricas clave:
  - Tarjetas repasadas.
  - Retención de información.
  - Niveles de dificultad.

### 4. Personalización de la Experiencia
- Ajuste de parámetros clave del algoritmo:
  - Factor de facilidad inicial.
  - Bonus adicional para respuestas fáciles.
  - Intervalos máximos y mínimos.
- Adaptación del sistema para objetivos de estudio:
  - **Corto plazo** (exámenes próximos).
  - **Largo plazo** (retención permanente).

## Pruebas

Las pruebas se encuentran en la carpeta `tests/`. Para ejecutar las pruebas unitarias, utiliza el siguiente comando:

```bash
npm test
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

- **Frontend**: React
- **Backend**: Node.js con Express
- **Base de Datos**: PostgreSQL

## Licencia

Este proyecto está bajo la licencia MIT. Consulta el archivo `LICENSE` para más detalles.

---

Desarrollado para mejorar el aprendizaje y optimizar el estudio mediante un enfoque científico y personalizable. ¡Estudia más inteligentemente con MemoCards! 📚
