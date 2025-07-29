# 🗺️ Aplicación de Resolución de Laberintos

## 📌 Información General

* **Título:** Maze Solver Application
* **Curso:** Estructura de Datos
* **Carrera:** Ingeniería de Sistemas
* **Estudiante:** Yandri Eduardo Sánchez Yanza
* **Correo Institucional:** ysanchezy@est.ups.edu.ec
* **Fecha:** 28 de julio de 2025
* **Profesor:** Ing. Pablo Torres

---

## 🛠️ Descripción del Problema

Este proyecto aborda el desafío fundamental de la ciencia de la computación: la **navegación y búsqueda de caminos en estructuras de laberintos**. El objetivo principal es desarrollar una aplicación que permita identificar y visualizar un camino óptimo (si existe) desde un punto de inicio definido hasta un punto final designado, dentro de un laberinto que puede contener obstáculos (paredes). Un aspecto crucial del proyecto es la **visualización en tiempo real del proceso de búsqueda** y la **comparación de la eficiencia** de diversos algoritmos en términos de tiempo de ejecución y la longitud del camino encontrado.

---

## 💡 Propuesta de Solución

La solución propuesta es una aplicación con una **Interfaz Gráfica de Usuario (GUI)** intuitiva que facilita a los usuarios la creación interactiva de laberintos personalizados, la selección de las celdas de inicio y fin, y la observación animada del proceso de resolución mediante diferentes algoritmos de búsqueda de caminos.

### 📚 Marco Teórico

El proyecto implementa y compara la eficiencia de varios algoritmos fundamentales de recorrido y búsqueda en grafos:

* **Búsqueda en Amplitud (BFS - Breadth-First Search):**
    * **Concepto:** Explora todos los nodos vecinos en el nivel de profundidad actual antes de avanzar al siguiente nivel. Utiliza una **cola** para gestionar el orden de visita.
    * **Aplicación en Laberintos:** Es ideal para encontrar el **camino más corto** en laberintos no ponderados (donde cada paso tiene el mismo costo), ya que expande la búsqueda de manera uniforme desde el punto de inicio.

* **Búsqueda en Profundidad (DFS - Depth-First Search):**
    * **Concepto:** Explora lo más profundo posible a lo largo de cada rama antes de retroceder. Se implementa típicamente usando una **pila** (explícita o implícitamente a través de recursión).
    * **Aplicación en Laberintos:** Permite encontrar un camino rápidamente al adentrarse en una dirección, pero no garantiza que sea el camino más corto. Puede ser útil en laberintos con muchos callejones sin salida.

* **Recursión (Backtracking Recursivo):**
    * **Concepto:** Una técnica de programación donde una función se llama a sí misma para resolver problemas que se dividen en subproblemas más pequeños del mismo tipo. El "backtracking" es el proceso de retroceder cuando un camino no conduce a una solución.
    * **Aplicación en Laberintos:** Representa una forma natural de explorar caminos. Si una ruta lleva a un callejón sin salida, la recursión permite "deshacer" los pasos y explorar alternativas.

* **Backtracking (Concepto General):**
    * **Concepto:** Una estrategia algorítmica general para resolver problemas que construyen candidatos a soluciones de forma incremental. Si en algún punto se determina que un candidato no puede conducir a una solución válida, el algoritmo "retrocede" para probar una alternativa diferente.
    * **Aplicación en Laberintos:** Aunque DFS es una forma de backtracking, el concepto general se refiere a la capacidad de revertir decisiones y explorar nuevos senderos cuando el actual resulta inviable.

### 💻 Tecnologías Utilizadas

* **Java:** El lenguaje de programación principal para la lógica de la aplicación, aprovechando su robustez y su ecosistema.
* **Swing:** El conjunto de herramientas estándar de Java para el desarrollo de interfaces gráficas de usuario (GUI), utilizado para crear la experiencia interactiva del usuario.
* **Maven:** Una herramienta de automatización de construcción y gestión de proyectos que facilita la gestión de dependencias (como JFreeChart) y la estandarización del proceso de compilación del proyecto.
* **JFreeChart:** Una biblioteca de gráficos Java gratuita para crear gráficos profesionales de calidad en aplicaciones Swing. Utilizada para visualizar el rendimiento de los algoritmos.

### 📈 Diagrama UML

[Aquí se agregará el diagrama de clases UML completo que ilustra las relaciones entre los componentes de la aplicación (Main, MazeView, MazeController, Maze, Cell, MazeSolver y sus implementaciones concretas, MazeResult, PerformanceChartPanel, ResultsView).]

*Explicación de los componentes y relaciones del diagrama.*

### 📸 Capturas de la Interfaz

#### Ejemplo 1: Laberinto Básico con Solución BFS

[Aquí se insertará una captura de pantalla que demuestre un laberinto simple y el camino encontrado por el algoritmo **BFS**, resaltando las celdas visitadas y el camino final.]

#### Ejemplo 2: Laberinto Complejo con Solución Recursiva

[Aquí se insertará una captura de pantalla que ilustre una estructura de laberinto más compleja y el camino encontrado por el algoritmo **Recursivo**, mostrando su patrón de exploración.]

---

## 💻 Código Ejemplo de un Algoritmo

[Aquí se agregará el código comentado y explicado de uno de los algoritmos de resolución de laberintos implementados (por ejemplo, DFS, BFS, o uno de los recursivos).]

---

## 4. Conclusiones

* **Yandri Eduardo Sánchez Yanza:**
    * **Análisis del Algoritmo Óptimo:** Tras la implementación y pruebas de los diversos algoritmos (BFS, DFS, Recursivos y sus variantes de Backtracking), he concluido que el **BFS (Búsqueda en Amplitud) es el algoritmo más óptimo para encontrar el camino más corto en laberintos no ponderados**, como los laberintos basados en cuadrícula donde cada paso tiene un costo uniforme de 1. La naturaleza de BFS de explorar el laberinto "capa por capa" asegura que la primera vez que se alcanza la celda final, el camino recorrido es, por definición, el más corto en términos de número de pasos. En contraste, algoritmos como DFS o las implementaciones recursivas, aunque pueden ser computacionalmente eficientes para simplemente *encontrar* cualquier camino, no garantizan la minimización de la longitud del camino. DFS, por ejemplo, podría explorar una ruta muy larga y tortuosa antes de retroceder y encontrar una opción más corta. Las variantes de backtracking, si bien ofrecen diferentes estrategias de exploración, tampoco poseen la propiedad intrínseca de encontrar el camino mínimo que caracteriza a BFS. Por lo tanto, para escenarios donde la longitud del camino es una métrica crítica, BFS se posiciona como la elección superior.

---

## 5. Recomendaciones y Aplicaciones Futuras

### Recomendaciones:
* **Optimización del Rendimiento:** Para laberintos de grandes dimensiones, considere la implementación del algoritmo **A\*** (A-Star). Este algoritmo incorpora una función heurística que guía la búsqueda de manera más eficiente, superando a BFS en grafos ponderados o con un gran número de nodos.
* **Generación de Laberintos:** Integrar algoritmos de generación de laberintos (ej., Backtracker Recursivo, Kruskal, Prim) permitiría a los usuarios crear laberintos aleatorios dinámicamente, enriqueciendo la interactividad y la capacidad de prueba de la aplicación.
* **Obstáculos Dinámicos:** Añadir la capacidad de introducir obstáculos dinámicos o de modificar el diseño del laberinto en tiempo real, abriendo la puerta a escenarios de búsqueda de caminos más complejos y reactivos.
* **Mejoras Visuales:** Implementar indicaciones visuales más sofisticadas, como el resaltado de la celda activa actual, animaciones para el proceso de retroceso (backtracking), o representaciones visuales del "frente de búsqueda" de cada algoritmo.
* **Experiencia de Usuario (UX):** Mejorar el manejo de errores y la retroalimentación al usuario, ofreciendo mensajes más claros cuando no se encuentre un camino, cuando las selecciones de inicio/fin son inválidas, o durante procesos largos.

### Aplicaciones Futuras:
* **Robótica y Navegación Autónoma:** Los algoritmos de búsqueda de caminos son fundamentales para que los robots naveguen de forma segura y eficiente en entornos complejos, evitando colisiones y alcanzando destinos.
* **Desarrollo de Videojuegos (Inteligencia Artificial):** Ampliamente utilizados para la trayectoria y el comportamiento de personajes no jugadores (NPCs), la IA enemiga y el diseño de niveles que requieren rutas óptimas.
* **Enrutamiento de Redes:** Algoritmos como BFS son la base para encontrar las rutas más cortas o eficientes para paquetes de datos en redes de comunicación e internet.
* **Logística y Servicios de Entrega:** Optimización de rutas para flotas de vehículos, minimizando el tiempo de entrega y el consumo de combustible, lo que tiene un impacto directo en la eficiencia operativa.
* **Asignación de Recursos:** Encontrar caminos óptimos para la distribución de recursos o el flujo de información en sistemas complejos, desde la gestión de cadenas de suministro hasta la optimización de flujos de trabajo.
* **Servicios de Emergencia:** Guía de vehículos de emergencia o personal de rescate a través de entornos urbanos congestionados o zonas de desastre, permitiendo una respuesta rápida y eficaz.