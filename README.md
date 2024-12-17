# MemoCards

MemoCards es una aplicación diseñada para estudiantes que desean mejorar sus habilidades de memorización utilizando tarjetas virtuales (flashcards). La plataforma organiza y optimiza el estudio mediante un sistema de **repetición espaciada** adaptado a la **curva del olvido**, lo que permite trasladar la información de la memoria a corto plazo a la memoria a largo plazo.

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

## Funcionamiento del Algoritmo de Intervalos
MemoCards utiliza la **repetición espaciada** para optimizar los repasos, basándose en la **curva del olvido**. Los intervalos se ajustan automáticamente según las respuestas del usuario:

1. **Otra vez (Olvidada)**:
   - Reinicia el intervalo a valores iniciales.
   - Reduce el factor de facilidad en un 25%.
2. **Difícil**:
   - Multiplica el intervalo actual por **1.2x**.
   - Reduce el factor de facilidad en un 15%.
3. **Bien (Correcta)**:
   - Multiplica el intervalo actual por el **factor de facilidad** (2.5x por defecto).
4. **Fácil**:
   - Incrementa el intervalo en un **30% adicional**.
   - Aumenta el factor de facilidad en un 15%.

## Flujo General del Sistema
1. El usuario crea un mazo y añade tarjetas con contenido personalizado.
2. Durante una sesión de estudio:
   - Se presentan tarjetas nuevas con intervalos iniciales cortos.
   - El usuario selecciona una opción (Otra vez, Difícil, Bien, Fácil).
3. Las tarjetas progresan a la fase de **Repaso** si se recuerdan correctamente.
4. Si una tarjeta es olvidada en el estado de Repaso, regresa al estado de **Reaprendizaje**.
5. El sistema ajusta automáticamente los intervalos según el desempeño del usuario.

## Parámetros Configurables
| Parámetro                | Descripción                                   | Valor por Defecto |
|---------------------------|-----------------------------------------------|------------------|
| Intervalo Inicial         | Primer repaso de una tarjeta nueva           | 15 min, 1 día, 3 días |
| Factor de Facilidad       | Multiplicador para respuestas correctas      | 2.5x             |
| Bonus para Fácil          | Incremento adicional en intervalos fáciles    | 130%             |
| Intervalo para Difícil   | Multiplicador para respuestas difíciles       | 1.2x             |
| Intervalo Mínimo         | Intervalo mínimo para tarjetas reaprendidas   | 1 día            |
| Intervalo Máximo         | Máximo tiempo entre repasos                 | 240 días         |
| Umbral de Sanguijuelas    | Límite de olvidos antes de etiquetar tarjetas | 8 olvidos        |

## Requerimientos del Sistema
1. Permitir la creación y gestión de mazos y tarjetas.
2. Manejar automáticamente los estados **Aprendizaje**, **Repaso** y **Reaprendizaje**.
3. Ajustar los intervalos de repaso según las respuestas del usuario.
4. Proporcionar reportes detallados del progreso.
5. Configuración avanzada de los parámetros del algoritmo.

## Tecnologías Recomendadas (Opcional)
- **Frontend**: React, Angular o Vue.js.
- **Backend**: Node.js con Express, Django o Flask.
- **Base de Datos**: PostgreSQL o MongoDB.

---

Desarrollado para mejorar el aprendizaje y optimizar el estudio mediante un enfoque científico y personalizable. ¡Estudia más inteligentemente con MemoCards! 📚
