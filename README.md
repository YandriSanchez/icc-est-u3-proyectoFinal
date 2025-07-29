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

<img width="2972" height="945" alt="Image" src="https://github.com/user-attachments/assets/37f51bfb-0ad7-471f-be59-bbb9a5278bf7" />

### 📘 Explicación del Diagrama UML de Clases

Este diagrama UML representa la arquitectura fundamental de tu aplicación de resolución de laberintos, siguiendo el patrón **Modelo-Vista-Controlador (MVC)**. Nos enfocamos en las interacciones clave entre el Modelo (`Maze`, `Cell`) y el Controlador (`MazeController`), así como en la jerarquía de los algoritmos de resolución.

---

#### 🧩 Componentes (Clases e Interfaces)

##### Main
- Punto de entrada de la aplicación.
- Inicializa y configura los componentes del MVC.
- Conecta `MazeController` con `Maze`.

##### MazeController
- Funciona como el cerebro de la aplicación.
- Intermediario entre la vista (no detallada) y el modelo (`Maze`).
- Maneja eventos del usuario (resolver, seleccionar inicio/fin).
- Orquesta el proceso de resolución con instancias de `MazeSolver`.
- Métodos clave:
  - `startSolvingMaze`
  - `processAndDisplayResults`
  - Control de animación y estado del laberinto.

##### Maze
- Modelo de datos del laberinto.
- Matriz 2D (`grid`) de booleanos: `true` (camino), `false` (pared).
- Almacena dimensiones (`rows`, `cols`).
- Métodos:
  - `isWall`
  - `setWall`
  - `resetGrid`

##### Cell
- Clase de datos simple que representa una celda.
- Atributos: `row`, `col`.
- Métodos: `equals`, `hashCode`.

##### MazeSolver (Interfaz)
- Contrato común para algoritmos de resolución.
- Método que define: `getPath`.
- Permite interacción uniforme con el controlador.

##### MazeResult
- Encapsula el resultado de la resolución.
- Contiene:
  - `List<Cell>` → Camino encontrado.
  - `Set<Cell>` → Celdas exploradas.

##### MazeSolverBFS, MazeSolverDFS, MazeSolverRecursive, MazeSolverRecursiveComplet, MazeSolverRecursiveCompletBT
- Implementaciones concretas de `MazeSolver`.
- Algoritmos de resolución específicos (BFS, DFS, recursivo, etc.).

---

#### 🔗 Relaciones entre Componentes

| Relación | Descripción |
|---------|-------------|
| `Main --- MazeController` | Inicialización: `Main` crea instancia del controlador. |
| `MazeController --- Maze` | Asociación directa, consulta y modifica el modelo. |
| `MazeController --- MazeSolver` | Uso de interfaz para permitir flexibilidad en algoritmos. |
| `MazeController ..> Cell` | Dependencia para representar celdas en animaciones. |
| `Maze *-- Cell` | Composición: el `Maze` está hecho de celdas (`Cell`). |
| `MazeSolver <|-- MazeSolverBFS` etc. | Implementación de la interfaz `MazeSolver`. |
| `MazeSolver ---> MazeResult` | Retorno: el método `getPath` devuelve un `MazeResult`. |

---

Este diseño modular permite:
- Intercambiar algoritmos fácilmente.
- Mantener separadas las responsabilidades entre modelo, vista y controlador.
- Expandir la funcionalidad sin romper la arquitectura.


### 📸 Capturas de la Interfaz

#### Ejemplo 1: Laberinto Básico con Solución BFS

<img width="784" height="695" alt="Image" src="https://github.com/user-attachments/assets/7261dbc1-c6fa-4471-988c-39abf3294b9d" />

#### Ejemplo 2: Laberinto Complejo con Solución Recursiva

<img width="785" height="696" alt="Image" src="https://github.com/user-attachments/assets/80c6acb0-7d86-47a4-8427-372edf8104a5" />

---

## 💻 Código Ejemplo de un Algoritmo

```java
/**
 * Implementación del algoritmo de resolución de laberintos Depth-First Search (DFS) de forma iterativa.
 * DFS explora el laberinto lo más profundo posible a lo largo de cada rama antes de retroceder.
 * Utiliza una pila (Stack) para gestionar las celdas a visitar.
 * NO garantiza encontrar el camino más corto.
 */
public class MazeSolverDFS implements MazeSolver {

  private boolean[][] grid;
  private Set<Cell> visited;
  private Map<Cell, Cell> parentMap;

  /**
   * Calcula y devuelve el resultado de la resolución de un laberinto utilizando el algoritmo DFS iterativo.
   * Este resultado incluye el camino encontrado (si existe) y todas las celdas visitadas
   * durante el proceso de búsqueda. El camino encontrado no está garantizado de ser el más corto.
   *
   * @param grid La cuadrícula booleana del laberinto, donde 'true' es camino y 'false' es muro.
   * @param start La celda de inicio desde la cual comenzar la búsqueda.
   * @param end La celda de destino a la que se debe llegar.
   * @return Un objeto MazeResult que contiene la lista del camino encontrado y el conjunto de celdas visitadas.
   */
  @Override
  public MazeResult getPath(boolean[][] grid, Cell start, Cell end) {
    this.grid = grid;
    this.visited = new LinkedHashSet<>();
    this.parentMap = new HashMap<>();

    // Validación inicial: si el laberinto es nulo, vacío o las celdas de inicio/fin son nulas
    if (grid == null || grid.length == 0 || start == null || end == null) {
      return new MazeResult(new ArrayList<>(), new LinkedHashSet<>());
    }

    // Pila para DFS: almacena las celdas a explorar
    Stack<Cell> stack = new Stack<>();

    // Añadir la celda inicial a la pila y marcarla como visitada
    stack.push(start);
    visited.add(start);

    boolean found = false;

    // Bucle principal de DFS
    while (!stack.isEmpty()) {
      Cell current = stack.pop(); // Saca la celda superior de la pila (LIFO)

      // Si llegamos al destino
      if (current.equals(end)) {
        found = true;
        break; // Salimos del bucle
      }

      // Explora los vecinos de la celda actual (en cualquier orden, pero este es común para DFS)
      // El orden de los vecinos puede influir en el camino encontrado por DFS.
      // Iterar en un orden específico puede simular el comportamiento de tus solvers recursivos.
      Cell[] neighbors = new Cell[] {
              new Cell(current.getRow() + 1, current.getCol()), // Abajo
              new Cell(current.getRow(), current.getCol() + 1), // Derecha
              new Cell(current.getRow() - 1, current.getCol()), // Arriba
              new Cell(current.getRow(), current.getCol() - 1)  // Izquierda
      };

      // Para DFS iterativo, es común procesar los vecinos en orden inverso para que
      // el primer vecino en ser considerado (por ejemplo, 'abajo') sea el último
      // en ser añadido a la pila, y por lo tanto, el primero en ser desapilado
      // Para DFS genérico, el orden no es crítico para la completitud, pero sí para el camino encontrado.
      for (Cell neighbor : neighbors) {
        if (isValid(neighbor)) { // Verifica si el vecino es válido y no ha sido visitado
          visited.add(neighbor); // Marca el vecino como visitado
          parentMap.put(neighbor, current); // Guarda que 'current' es el padre de 'neighbor'
          stack.push(neighbor); // Añade el vecino a la pila para explorarlo más tarde
        }
      }
    }

    // Reconstruir el camino si se encontró el destino
    List<Cell> path = new ArrayList<>();
    if (found) {
      Cell current = end;
      while (current != null) {
        path.add(0, current); // Añadir al principio para obtener el orden correcto
        current = parentMap.get(current); // Retrocede usando el mapa de padres
      }
    }
    return new MazeResult(path, visited);
  }

  /**
   * Verifica si una celda está dentro de los límites del laberinto, es un camino (no muro)
   * y no ha sido visitada previamente.
   *
   * @param cell La celda a verificar.
   * @return true si la celda es válida para la exploración, false en caso contrario.
   */
  private boolean isValid(Cell cell) {
    if (!isInMaze(cell)) {
      return false;
    }
    return grid[cell.getRow()][cell.getCol()] && !visited.contains(cell);
  }

  /**
   * Verifica si una celda está dentro de los límites de la cuadrícula del laberinto.
   *
   * @param cell La celda a verificar.
   * @return true si la celda está dentro del laberinto, false en caso contrario.
   */
  private boolean isInMaze(Cell cell) {
    int row = cell.getRow();
    int col = cell.getCol();
    return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
  }
}
```

---

## 4. Conclusiones

* **Yandri Eduardo Sánchez Yanza:**
    * Análisis del Algoritmo Óptimo del Grupo de Algoritmos: A raíz del desarrollo e implementación realizado, junto con la evaluación de cada uno de los algoritmos que en el desarrollo se mostraron (algoritmos BFS, DFS, Recursivos y sus variantes de Backtracking), he llegado a la conclusión de que el **BFS (Breadth First Search - Búsqueda en Amplitud) es el algoritmo más óptimo para encontrar el camino mínimo en laberintos no ponderados**. Para laberintos basados en cuadrícula donde el coste por cada paso es uniforme (1), el algoritmo BFS cumple la condición de ser el más óptimo dado que el laberinto se explora "capa a capa" y la primera vez que se alcanza la celda final (la meta) será, por definición, el camino más corto (en número de pasos); contrario a otros tipos de algoritmos (DFS o recursivos) que, aunque puedan resultar computacionalmente más eficientes para simplemente *encontrar* un camino, no garantizan la corteza de la longitud del camino; como, por ejemplo, en el caso de DFS, en el que la ruta puede ser muy larga y tortuosa antes de volver atrás y encontrar una opción más corta. Igualmente, variaciones del backtracking (con las que se pueden establecer diferentes estrategias de exploración) no cuentan con la propiedad de encontrar el camino mínimo que caracteriza al BFS. Por tanto, en la misma línea, para un caso en el que la longitud del camino sea una métrica a tener en consideración, el BFS es, de lejos, una opción superior.
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