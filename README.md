# 🗺️ Aplicación de Resolución de Laberintos

## 📌 Información General

- **Title:** Maze Solver Application
- **Course:** Data Structures
- **Degree:** Computer Science
- **Student:** Yandri Eduardo Sanchez Yanza
- **Institutional Email:** ysanchezy@est.ups.edu.ec
- **Date:** July 28, 2025
- **Professor:** Ing. Pablo Torres

---

## 🛠️ Descripción del Problema

Este proyecto aborda el problema clásico de la ciencia de la computación de la **navegación y búsqueda de caminos en laberintos**. El desafío principal consiste en encontrar un camino óptimo (si existe) desde un punto de inicio definido hasta un punto final designado dentro de una estructura de laberinto dada, que puede contener obstáculos (paredes). Además, el proyecto tiene como objetivo visualizar el proceso de búsqueda y comparar la eficiencia de varios enfoques algorítmicos en términos de tiempo de ejecución y longitud del camino.

---

## 💡 Propuesta de Solución

La solución implementa una interfaz gráfica de usuario (GUI) que permite a los usuarios crear laberintos personalizados de forma interactiva, seleccionar puntos de inicio y fin, y visualizar el proceso de búsqueda de caminos utilizando diferentes algoritmos.

### 📚 Marco Teórico

El proyecto utiliza y compara varios algoritmos fundamentales de recorrido y búsqueda en grafos:

* **Búsqueda en Amplitud (BFS - Breadth-First Search):**
    * **Concepto:** Un algoritmo de recorrido de grafos que explora todos los nodos vecinos en el nivel de profundidad actual antes de pasar a los nodos del siguiente nivel de profundidad. Utiliza una cola para mantener un registro de los próximos nodos a visitar.
    * **Aplicación en Laberintos:** Garantiza encontrar el **camino más corto** en términos de número de pasos en un grafo no ponderado (como un laberinto basado en cuadrícula donde cada paso tiene un costo de 1). Explora el laberinto capa por capa.

* **Búsqueda en Profundidad (DFS - Depth-First Search):**
    * **Concepto:** Un algoritmo de recorrido de grafos que explora lo más lejos posible a lo largo de cada rama antes de retroceder. Normalmente utiliza una pila (implícita o explícitamente a través de recursión) para mantener un registro de los nodos a visitar.
    * **Aplicación en Laberintos:** Explora profundamente en un camino antes de intentar alternativas. Aunque no garantiza el camino más corto, puede ser muy eficiente simplemente para *encontrar* un camino, especialmente en laberintos con pasillos largos y sinuosos.

* **Recursión (Backtracking Recursivo):**
    * **Concepto:** Una técnica de resolución de problemas donde la solución a un problema depende de las soluciones a instancias más pequeñas del mismo problema. El backtracking implica probar sistemáticamente todos los caminos posibles para encontrar una solución, y si un camino conduce a un callejón sin salida, "retrocede" hasta el último punto de decisión e intenta un camino diferente.
    * **Aplicación en Laberintos:** Muchas implementaciones de DFS son naturalmente recursivas. Este enfoque modela directamente la idea de probar un camino, marcar celdas visitadas, y si es un callejón sin salida, desmarcar celdas a medida que retrocede. A menudo se utiliza también para la generación de laberintos.

* **Backtracking (Concepto General):**
    * **Concepto:** Una técnica algorítmica general para resolver problemas que construyen candidatos a las soluciones de forma incremental, y abandonan un candidato ("backtrack") tan pronto como se determina que el candidato no puede completarse para formar una solución válida.
    * **Aplicación en Laberintos:** Si bien DFS es una forma de backtracking, los algoritmos de backtracking más explícitos podrían mantener un registro del camino de decisión completo y revertir el estado de manera más formal. El solucionador "recursivo" en este proyecto utiliza implícitamente el backtracking al regresar de las llamadas recursivas.

### 💻 Tecnologías Utilizadas

* **Java:** El lenguaje de programación principal para la lógica de la aplicación.
* **Swing:** El kit de herramientas GUI de Java para construir la interfaz de usuario interactiva.
* **Maven (Opcional, pero recomendado para la gestión del proyecto):** Para la gestión de dependencias y la construcción del proyecto.

### Diagrama UML

Un diagrama de clases UML completo que ilustra las relaciones entre `Main`, `MazeView`, `MazeController`, `Maze`, `Cell`, `MazeSolver` (interfaz) y las implementaciones concretas de los solucionadores (`MazeSolverBFS`, `MazeSolverDFS`, `MazeSolverRecursive`, etc.).

* **`Main`:** El punto de entrada de la aplicación, responsable de inicializar los componentes MVC.
* **`MazeView`:** Representa la interfaz gráfica de usuario, mostrando la cuadrícula del laberinto y los controles de usuario. Observa la entrada del usuario.
* **`MazeController`:** Actúa como intermediario entre `MazeView` y `Maze`. Maneja los eventos de usuario desde la vista, los procesa y actualiza tanto el modelo como la vista. Orquesta el proceso de resolución.
* **`Maze`:** El componente del modelo, que representa la estructura de datos interna del laberinto (por ejemplo, una matriz 2D de booleanos que indican caminos o paredes). Contiene la lógica para la manipulación del laberinto (por ejemplo, establecer paredes).
* **`Cell`:** Una clase de datos simple que representa una única coordenada (fila, columna) dentro del laberinto.
* **`MazeSolver` (Interfaz):** Define el contrato para cualquier algoritmo de resolución de laberintos, típicamente incluyendo un método para encontrar un camino.
* **Clases de Solucionadores Concretos (por ejemplo, `MazeSolverBFS`, `MazeSolverDFS`, `MazeSolverRecursive`):** Implementan la interfaz `MazeSolver`, proporcionando la lógica específica para cada algoritmo.

![Diagrama UML]()

*Explicación de los componentes y relaciones del diagrama.*

### 📸 Capturas de la Interfaz

#### Ejemplo 1: Laberinto Básico con Solución BFS

Esta captura de pantalla demuestra un laberinto simple y el camino encontrado por el algoritmo **BFS**, resaltando las celdas visitadas y el camino final.

![Laberinto Ejemplo 1 - BFS]()

#### Ejemplo 2: Laberinto Complejo con Solución Recursiva

Esta captura de pantalla ilustra una estructura de laberinto más compleja y el camino encontrado por el algoritmo **Recursivo**, mostrando su patrón de exploración.

![Laberinto Ejemplo 2 - Recursivo]()

---

## 4. Conclusiones

* **[Tu Nombre]:**
    * **Análisis del Algoritmo Óptimo:** Tras implementar y probar los diversos algoritmos (BFS, DFS, Recursivos, y variantes de Backtracking), es evidente que **BFS es el algoritmo más óptimo para encontrar el camino más corto en laberintos no ponderados (como laberintos basados en cuadrícula donde cada paso cuesta 1 unidad)**. Esto se debe a que BFS explora todos los nodos alcanzables nivel por nivel, garantizando que la primera vez que llega al objetivo (celda final), ha encontrado el camino con el número mínimo de pasos. Mientras que otros algoritmos como DFS o los métodos Recursivos son más sencillos de implementar o pueden ser más rápidos en ciertas configuraciones específicas de laberinto para *simplemente encontrar un camino*, no garantizan la optimización en términos de longitud del camino. DFS, por ejemplo, podría adentrarse en un camino largo y sinuoso antes de encontrar una alternativa más corta. Las variantes de backtracking ofrecen diferentes estrategias de exploración pero tampoco garantizan inherentemente el camino más corto como lo hace BFS. Por lo tanto, para aplicaciones donde el camino más corto es un requisito estricto, BFS es superior.

---

## 5. Recomendaciones y Aplicaciones Futuras

### Recomendaciones:
* **Optimización del Rendimiento:** Para laberintos muy grandes, considere implementar optimizaciones como la búsqueda A\*, que utiliza una heurística para guiar su búsqueda y puede ser significativamente más rápida que BFS para la búsqueda de caminos en grafos ponderados o grandes.
* **Generación de Laberintos:** Integre un algoritmo de generación de laberintos (por ejemplo, Backtracker Recursivo, Kruskal, Prim) para permitir a los usuarios generar laberintos aleatorios directamente dentro de la aplicación, mejorando la rejugabilidad y la prueba de diversos escenarios.
* **Obstáculos Dinámicos:** Implemente la funcionalidad para obstáculos dinámicos o cambios en el diseño del laberinto, permitiendo escenarios más complejos de búsqueda de caminos en tiempo real.
* **Mejoras Visuales:** Añada indicaciones visuales más sofisticadas, como diferentes colores para la celda activa actual, animaciones para el retroceso, o indicadores visuales para los frentes de búsqueda.
* **Experiencia de Usuario:** Mejore el manejo de errores y la retroalimentación al usuario. Por ejemplo, proporcione mensajes más claros cuando no se encuentre un camino o cuando se realicen selecciones no válidas.

### Aplicaciones Futuras:
* **Robótica y Navegación Autónoma:** Los algoritmos de búsqueda de caminos son fundamentales para que los robots naveguen en entornos desconocidos o dinámicos, evitando obstáculos y alcanzando objetivos de manera eficiente.
* **Desarrollo de Juegos (IA):** Se utilizan ampliamente para el movimiento de personajes no jugadores (NPC), la búsqueda de caminos de la IA enemiga y el diseño de niveles en videojuegos.
* **Enrutamiento de Redes:** Algoritmos similares a BFS se utilizan para encontrar las rutas más cortas o eficientes para los paquetes de datos a través de redes informáticas.
* **Logística y Servicios de Entrega:** Optimización de las rutas de entrega para vehículos, minimizando el tiempo de viaje y el consumo de combustible.
* **Asignación de Recursos:** Encontrar caminos óptimos para la distribución de recursos o el flujo de información en sistemas complejos.
* **Servicios de Emergencia:** Guía de vehículos o personal de emergencia a través de entornos urbanos complejos o zonas de desastre.