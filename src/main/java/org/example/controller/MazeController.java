package org.example.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.example.model.Cell;
import org.example.model.Maze;
import org.example.model.MazeResult;
import org.example.model.MazeSolver;
import org.example.solver.MazeSolverBFS;
import org.example.solver.MazeSolverDFS;
import org.example.solver.MazeSolverRecursive;
import org.example.solver.MazeSolverRecursiveComplet;
import org.example.solver.MazeSolverRecursiveCompletBT;
import org.example.view.MazeCellPanel;
import org.example.view.MazeView;

/**
 * <p>La clase {MazeController} actúa como el cerebro de la aplicación del laberinto,
 * manejando la lógica de interacción entre la vista ({MazeView}) y el modelo ({Maze},
 * {MazeSolver}).</p>
 * <p>Es responsable de:</p>
 * <ul>
 * <li>Inicializar y gestionar el estado del laberinto (muros, celdas de inicio/fin).</li>
 * <li>Coordinar la resolución del laberinto utilizando diferentes algoritmos.</li>
 * <li>Controlar la visualización animada de los procesos de búsqueda de caminos.</li>
 * <li>Responder a las acciones del usuario, como seleccionar celdas o iniciar la resolución.</li>
 * </ul>
 */
public final class MazeController {

    private final MazeView view;
    private Maze maze;

    private Cell startCell;
    private Cell endCell;

    private final Map<String, MazeSolver> solversMap;

    // Listas para la animación
    public List<Cell> currentVisitedCellsAnimation;
    public List<Cell> currentPathCellsAnimation;
    private SwingWorker<Void, Cell> animationWorker;
    public AtomicInteger animationIndex;
    public AtomicInteger pathAnimationIndex;
    public AtomicInteger visitedIndex;
    private boolean isAnimationRunning = false;
    private volatile boolean isAnimatingPathPhase = false;
    private final int ANIMATION_DELAY_MS = 130;// Retraso en milisegundos

    /**
     * Construye una nueva instancia de {MazeController}.
     * Inicializa la vista del laberinto, el modelo del laberinto y los solucionadores disponibles.
     *
     * @param view La instancia de {MazeView} asociada a este controlador.
     * @param initialRows El número inicial de filas para el laberinto.
     * @param initialCols El número inicial de columnas para el laberinto.
     */
    public MazeController(MazeView view, int initialRows, int initialCols) {
        this.view = view;
        this.maze = new Maze(new boolean[initialRows][initialCols]);
        initializeMazeGrid(initialRows, initialCols);

        this.solversMap = new HashMap<>();
        solversMap.put("Metodo Recursivo", new MazeSolverRecursive());
        solversMap.put("Metodo Recursivo Completo", new MazeSolverRecursiveComplet());
        solversMap.put("Metodo Recursivo Completo BT", new MazeSolverRecursiveCompletBT());
        solversMap.put("Metodo DFS", new MazeSolverDFS());
        solversMap.put("Metodo BFS", new MazeSolverBFS());

        this.animationIndex = new AtomicInteger(0);
        this.pathAnimationIndex = new AtomicInteger(0);
        this.visitedIndex = new AtomicInteger(0);
    }

    /**
     * Inicializa la cuadrícula del laberinto con todas las celdas como caminos (no muros).
     *
     * @param rows El número de filas de la nueva cuadrícula.
     * @param cols El número de columnas de la nueva cuadrícula.
     */
    public void initializeMazeGrid(int rows, int cols) {
        boolean[][] newGrid = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                newGrid[i][j] = true; // Todas las celdas son caminos por defecto
            }
        }
        this.maze.setGrid(newGrid);
    }

    /**
     * Inicia el proceso de resolución del laberinto usando el algoritmo seleccionado.
     * Muestra el camino encontrado y las celdas visitadas.
     *
     * @param algorithmName El nombre del algoritmo a utilizar.
     */
    public void startSolvingMaze(String algorithmName) {
        if (startCell == null || endCell == null) {
            showMessage("Por favor, selecciona las celdas de inicio y fin antes de resolver.", "Advertencia", "WARNING");
            return;
        }

        // Obtener el estado actual de los muros desde la vista
        boolean[][] grid = view.getCellPanelsState();
        maze.setGrid(grid); // Actualizar el grid del modelo con los muros de la vista

        MazeSolver solver;
        long startTime;
        long endTime;
        MazeResult result;

        // Determinar el solver y ejecutar
        switch (algorithmName) {
            case "Metodo Recursivo" -> {
                solver = new MazeSolverRecursive();
                startTime = System.nanoTime();
                result = solver.getPath(maze.getGrid(), startCell, endCell);
                endTime = System.nanoTime();
                processAndDisplayResults(result, "Metodo Recursivo", (endTime - startTime));
            }
            case "Metodo Recursivo Completo" -> {
                solver = new MazeSolverRecursiveComplet();
                startTime = System.nanoTime();
                result = solver.getPath(maze.getGrid(), startCell, endCell);
                endTime = System.nanoTime();
                processAndDisplayResults(result, "Metodo Recursivo Completo", (endTime - startTime));
            }
            case "Metodo Recursivo Completo BT" -> {
                solver = new MazeSolverRecursiveCompletBT();
                startTime = System.nanoTime();
                result = solver.getPath(maze.getGrid(), startCell, endCell);
                endTime = System.nanoTime();
                processAndDisplayResults(result, "Metodo Recursivo Completo BT", (endTime - startTime));
            }
            case "Metodo BFS" -> {
                solver = new MazeSolverBFS();
                startTime = System.nanoTime();
                result = solver.getPath(maze.getGrid(), startCell, endCell);
                endTime = System.nanoTime();
                processAndDisplayResults(result, "Metodo BFS", (endTime - startTime));
            }
            case "Metodo DFS" -> {
                solver = new MazeSolverDFS();
                startTime = System.nanoTime();
                result = solver.getPath(maze.getGrid(), startCell, endCell);
                endTime = System.nanoTime();
                processAndDisplayResults(result, "Metodo DFS", (endTime - startTime));
            }
            default -> {
                showMessage("Algoritmo no reconocido.", "Error", "ERROR");
            }
        }
    }

    /**
     * Procesa los resultados de un algoritmo de resolución y los muestra de forma animada.
     * Este método ahora inicia un SwingWorker para la animación gradual.
     *
     * @param result El MazeResult obtenido del solver.
     * @param methodName El nombre del método que generó el resultado.
     * @param elapsedTime El tiempo que tardó el algoritmo en nanosegundos.
     */
    private void processAndDisplayResults(MazeResult result, String methodName, long elapsedTime) {
        // Si ya hay una animación corriendo, la detenemos o mostramos un mensaje
        if (isAnimationRunning) {
            showMessage("Ya hay una animación en curso. Deteniendo la animación anterior para iniciar una nueva.", "Advertencia", "WARNING");
            stopAnimation(); // Detener la animación anterior
            // Esperar un breve momento para que el hilo anterior termine de limpiar si es necesario
            try { Thread.sleep(50); } catch (InterruptedException ex) { Thread.currentThread().interrupt(); }
        }

        // Limpiar colores previos y reiniciar índices de animación para la nueva ejecución
        resetPathColorsInView(); // Llama a MazeView para limpiar el panel
        resetAnimationIndices(); // Reinicia los índices y las listas para la animación manual, aunque no se usen aquí directamente
        isAnimationRunning = true; // Marcamos que una animación automática está activa
        isAnimatingPathPhase = false; // Aseguramos que empezamos en la fase de celdas visitadas


        // Almacenar los resultados para que el SwingWorker los use.
        // Convertimos el Set<Cell> a List<Cell> para garantizar un orden de publicación predecible.
        // Asegúrate de que MazeResult.getVisited() devuelve Set<Cell>
        this.currentVisitedCellsAnimation = new ArrayList<>(result.getVisited());
        this.currentPathCellsAnimation = result.getPath();

        // Crear y ejecutar el SwingWorker para la animación
        animationWorker = new SwingWorker<Void, Cell>() {
            @Override
            protected Void doInBackground() throws Exception {
                // 1. Animar PRIMERO todas las celdas visitadas
                for (Cell cell : currentVisitedCellsAnimation) {
                    if (isCancelled()) return null; // Salir si la animación se cancela
                    // Publica la celda solo si no es inicio/fin o muro
                    if (!cell.equals(startCell) && !cell.equals(endCell) && maze.getGrid()[cell.getRow()][cell.getCol()]) {
                        publish(cell); // Envía la celda al método process (en el EDT)
                    }
                    Thread.sleep(ANIMATION_DELAY_MS); // Retardo para el efecto de animación
                }

                if (!isCancelled()) {
                    Thread.sleep(ANIMATION_DELAY_MS); // Por ejemplo, el triple del delay normal
                    isAnimatingPathPhase = true; // Cambiamos a la fase de animación del camino
                }

                // 2. Animar LUEGO el camino final
                if (currentPathCellsAnimation != null && !currentPathCellsAnimation.isEmpty()) {
                    for (Cell cell : currentPathCellsAnimation) {
                        if (isCancelled()) return null; // Salir si la animación se cancela
                        // Solo publica la celda del camino si no es inicio o fin
                        if (!cell.equals(startCell) && !cell.equals(endCell)) {
                            publish(cell); // Envía la celda al método process (en el EDT)
                        }
                        Thread.sleep(ANIMATION_DELAY_MS); // Retardo para el efecto de animación
                    }
                }
                return null;
            }

            @Override
            protected void process(List<Cell> chunks) {
                // Este método se ejecuta en el Event Dispatch Thread (EDT)
                // Se encarga de actualizar la interfaz de usuario de forma segura.
                for (Cell cell : chunks) {
                    // Lógica modificada para pintar según la fase actual
                    if (isAnimatingPathPhase) { // Si estamos en la fase de pintar el camino
                        if (!cell.equals(startCell) && !cell.equals(endCell)) {
                            view.updateCell(cell.getRow(), cell.getCol(), MazeView.PATH_COLOR);
                        }
                    } else { // Si estamos en la fase de pintar las celdas visitadas
                        // Solo pinta si no es inicio/fin y no es un muro
                        if (!cell.equals(startCell) && !cell.equals(endCell) && maze.getGrid()[cell.getRow()][cell.getCol()]) {
                            view.updateCell(cell.getRow(), cell.getCol(), MazeView.VISITED_COLOR);
                        }
                    }
                    // Asegurar que inicio y fin siempre mantengan sus colores correctos
                    view.updateCell(startCell.getRow(), startCell.getCol(), MazeView.START_COLOR);
                    view.updateCell(endCell.getRow(), endCell.getCol(), MazeView.END_COLOR);
                }
            }

            @Override
            protected void done() {
                // Este método se ejecuta en el EDT cuando doInBackground termina o ha sido cancelado.
                isAnimationRunning = false; // La animación ha finalizado.
                isAnimatingPathPhase = false; // Resetear la bandera de fase al finalizar

                // Asegurar que las celdas de inicio y fin siempre tengan el color correcto al final
                view.updateCell(startCell.getRow(), startCell.getCol(), MazeView.START_COLOR);
                view.updateCell(endCell.getRow(), endCell.getCol(), MazeView.END_COLOR);

                try {
                    // Llamar a get() para propagar cualquier excepción que haya ocurrido en doInBackground
                    get();
                    // Mostrar el mensaje final y guardar los resultados SOLO UNA VEZ al final de la animación
                    if (result.getPath() != null && !result.getPath().isEmpty()) {
                        showMessage("Camino encontrado por " + methodName + " en " + elapsedTime + " ns. Longitud: " + (result.getPath().size() - 1) + " celdas.", "Éxito", "INFORMATION");
                        view.addSolverResult(methodName, result.getPath().size() - 1, elapsedTime);
                    } else {
                        showMessage("No se encontró camino por " + methodName + " en " + elapsedTime + " ns.", "Sin Camino", "INFORMATION");
                        view.addSolverResult(methodName, -1, elapsedTime);
                    }
                } catch (InterruptedException | ExecutionException e) { // Catch ambas excepciones
                    // Capturar y manejar excepciones del SwingWorker (ej. InterruptedException si se cancela)
                    if (e instanceof java.util.concurrent.CancellationException) {
                        System.out.println("Animación de '" + methodName + "' cancelada.");
                        showMessage("La animación de '" + methodName + "' fue cancelada.", "Cancelado", "INFORMATION");
                    } else {
                        showMessage("Error inesperado durante la animación de '" + methodName + "': " + e.getMessage(), "Error", "ERROR");
                    }
                }
            }
        };

        animationWorker.execute(); // Inicia el SwingWorker para que la animación comience.
    }

    /**
     * Realiza un solo paso en la animación del algoritmo de resolución.
     * @param algorithmName El nombre del algoritmo para la animación.
     */
    public void performSingleStep(String algorithmName) {
        if (startCell == null || endCell == null) {
            showMessage("Por favor, selecciona las celdas de inicio y fin antes de resolver paso a paso.", "Advertencia", "WARNING");
            return;
        }

        if (!isAnimationRunning) { // Si es la primera ejecución paso a paso
            // Esto solo se ejecuta una vez al inicio de un nuevo "paso a paso"
            // Se debe obtener el resultado completo del solver para la animación
            boolean[][] grid = view.getCellPanelsState();
            maze.setGrid(grid);

            MazeSolver solver;
            switch (algorithmName) {
                case "Metodo Recursivo" -> solver = new MazeSolverRecursive();
                case "Metodo Recursivo Completo" -> solver = new MazeSolverRecursiveComplet();
                case "Metodo Recursivo Completo BT" -> solver = new MazeSolverRecursiveCompletBT();
                case "Metodo BFS" -> solver = new MazeSolverBFS();
                case "Metodo DFS" -> solver = new MazeSolverDFS();
                default -> {
                    showMessage("Algoritmo no reconocido.", "Error", "ERROR"); return;
                }
            }

            long startTime = System.nanoTime();
            MazeResult result = solver.getPath(maze.getGrid(), startCell, endCell);
            long endTime = System.nanoTime();

            Set<Cell> visitedSet = result.getVisited();
            currentVisitedCellsAnimation = new ArrayList<>(visitedSet);
            currentPathCellsAnimation = result.getPath();

            view.addSolverResult(algorithmName, (currentPathCellsAnimation != null ? currentPathCellsAnimation.size() -1 : -1), (endTime - startTime));

            isAnimationRunning = true; // En modo "paso a paso", esta bandera podría indicar "animación inicializada"
            animationIndex.set(0);
            pathAnimationIndex.set(0);
            visitedIndex.set(0);
            isAnimatingPathPhase = false;
        }

        // Lógica de avance de la animación
        animateNextStep();
    }

    /** Muestra un mensaje al usuario. */
    public void showMessage(String message, String title, String type) {
        int messageType;
        messageType = switch (type.toUpperCase()) {
            case "INFORMATION" -> JOptionPane.INFORMATION_MESSAGE;
            case "WARNING" -> JOptionPane.WARNING_MESSAGE;
            case "ERROR" -> JOptionPane.ERROR_MESSAGE;
            default -> JOptionPane.PLAIN_MESSAGE;
        };
        JOptionPane.showMessageDialog(view, message, title, messageType);
    }

    /**
     * Detiene la animación SwingWorker actual si está en curso.
     * Útil para un botón "Detener" o al iniciar una nueva animación.
     */
    public void stopAnimation() {
        if (animationWorker != null && !animationWorker.isDone()) {
            animationWorker.cancel(true); // true para intentar interrumpir el hilo si está durmiendo
            showMessage("Animación detenida.", "Información", "INFORMATION");
        }
        isAnimationRunning = false;
        isAnimatingPathPhase = false;
    }

    /**
     * Avanza un paso en la animación del laberinto, típicamente llamado por un botón "Paso a Paso".
     * Si no se han cargado datos de animación, solicita al usuario que inicie la resolución.
     */
    public void stepAnimation() {
        if (currentVisitedCellsAnimation == null || animationIndex == null) {
            JOptionPane.showMessageDialog(view, "Primero debes resolver el laberinto con el modo 'Paso a Paso'.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Asegurarse de que no estamos en modo automático
        stopAnimation(); // Detener cualquier timer si por alguna razón sigue corriendo
        animateNextCell();
    }


    /**
     * Lógica compartida para animar la siguiente celda en modo automático o manual.
     * Primero pinta las celdas visitadas, luego las del camino.
     * Al finalizar la animación, muestra un mensaje indicando si se encontró un camino o no.
     */
    private void animateNextCell() {
        // Pinta celdas visitadas
        if (!isAnimatingPathPhase && visitedIndex.get() < currentVisitedCellsAnimation.size()) {
            Cell cellToAnimate = currentVisitedCellsAnimation.get(visitedIndex.get());
            MazeCellPanel panel = view.getCellPanels()[cellToAnimate.getRow()][cellToAnimate.getCol()];

            // Solo pinta si no es inicio o fin, y no es un muro
            if (!cellToAnimate.equals(startCell) && !cellToAnimate.equals(endCell) && maze.getGrid()[cellToAnimate.getRow()][cellToAnimate.getCol()]) {
                panel.setBackground(MazeView.VISITED_COLOR);
            }
            panel.revalidate();
            panel.repaint();
            visitedIndex.getAndIncrement();

            // Si terminamos de pintar las visitadas, cambiamos a la fase del camino
            if (visitedIndex.get() >= currentVisitedCellsAnimation.size()) {
                isAnimatingPathPhase = true;
            }
        }
        // Pinta celdas del camino (una vez que todas las visitadas han sido pintadas)
        else if (isAnimatingPathPhase && currentPathCellsAnimation != null && pathAnimationIndex.get() < currentPathCellsAnimation.size()) {
            Cell pathCell = currentPathCellsAnimation.get(pathAnimationIndex.get());
            MazeCellPanel panel = view.getCellPanels()[pathCell.getRow()][pathCell.getCol()];

            // Solo pinta si no es inicio o fin
            if (!pathCell.equals(startCell) && !pathCell.equals(endCell)) {
                panel.setBackground(MazeView.PATH_COLOR);
            }
            panel.revalidate();
            panel.repaint();
            pathAnimationIndex.getAndIncrement();
        }
        // Animación terminada
        else {
            stopAnimation(); // Detener el timer si es modo automático
            if (currentPathCellsAnimation == null || currentPathCellsAnimation.isEmpty()) {
                JOptionPane.showMessageDialog(view,
                    "No se encontró un camino, se muestran las celdas exploradas.",
                    "Sin Camino", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(view,
                    "¡Camino encontrado!",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
            isAnimatingPathPhase = false;
        }
        view.updateCell(startCell.getRow(), startCell.getCol(), MazeView.START_COLOR);
        view.updateCell(endCell.getRow(), endCell.getCol(), MazeView.END_COLOR);
    }

    /**
     * Método auxiliar para animar el siguiente paso en el modo "Paso a Paso".
     * Llama a {#animateNextCell()} para pintar la siguiente celda en la secuencia.
     * Muestra un mensaje si no hay celdas para animar.
     */
    private void animateNextStep() {
        // Asegúrate de que las listas de animación estén cargadas
        if (currentVisitedCellsAnimation == null || (currentVisitedCellsAnimation.isEmpty() && 
            (currentPathCellsAnimation == null || currentPathCellsAnimation.isEmpty()))) {
            JOptionPane.showMessageDialog(view, "No hay celdas para animar. Verifica si se encontró un camino o celdas visitadas.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        animateNextCell();
    }

    /**
     * Restablece los índices de la animación a cero.
     * Esto prepara el controlador para una nueva animación desde el inicio.
     */
    public void resetAnimationIndices() {
        animationIndex.set(0);
        pathAnimationIndex.set(0);
        visitedIndex.set(0);
        isAnimatingPathPhase = false;
    }

    /**
     * Restablece el laberinto a su estado inicial, eliminando cualquier camino o celdas visitadas
     * y restableciendo las celdas de inicio y fin. Los muros permanecen.
     */
    public void resetMaze() {
        resetPathColorsInView(); // Esto despinta la solución y respeta obstáculos
        resetAnimationIndices();

        startCell = null;
        endCell = null;
        if(view != null) {
            view.resetSelectedCells();
        }
        isAnimatingPathPhase = false;
    }

    /**
     * Restablece los colores de todas las celdas en la vista del laberinto a sus colores por defecto,
     * respetando las celdas de inicio, fin y los muros.
     * Detiene cualquier animación en curso.
     */
    public void resetPathColorsInView() {
        stopAnimation();
        if (view == null) return;
        MazeCellPanel[][] panels = view.getCellPanels();
        if (panels == null) return;

        for (int i = 0; i < panels.length; i++) {
            for (int j = 0; j < panels[0].length; j++) {
                MazeCellPanel cellPanel = panels[i][j];

                // Prioridad: inicio/fin, luego obstáculos, luego caminos normales
                if (view.getSelectedStartCell() != null && i == view.getSelectedStartCell().getRow() && j == view.getSelectedStartCell().getCol()) {
                    cellPanel.setBackground(MazeView.START_COLOR);
                } else if (view.getSelectedEndCell() != null && i == view.getSelectedEndCell().getRow() && j == view.getSelectedEndCell().getCol()) {
                    cellPanel.setBackground(MazeView.END_COLOR);
                }
                else if (cellPanel.isObstacle()) {
                    cellPanel.setBackground(MazeView.WALL_COLOR);
                }
                // Si no es ninguno de los anteriores, es un camino normal, píntalo de blanco
                else {
                    cellPanel.setBackground(MazeView.DEFAULT_CELL_COLOR);
                }
                cellPanel.revalidate();
                cellPanel.repaint();
            }
        }
        isAnimatingPathPhase = false;
    }


    /**
     * Sincroniza el estado de las paredes del {MazeView} con el modelo {Maze} del controlador.
     * Esto es crucial antes de resolver para que el algoritmo trabaje con el laberinto
     * tal como el usuario lo ha dibujado en la interfaz.
     */
    public void syncViewToModelWalls() {
        boolean[][] currentViewGrid = view.getCellPanelsState();
        if (currentViewGrid != null) {
            this.maze.setGrid(currentViewGrid);
            System.out.println("Modelo de laberinto actualizado con las paredes de la vista.");
        }
    }

    /**
     * Obtiene la celda de inicio actualmente seleccionada.
     *
     * @return La celda de inicio.
     */
    public Cell getStartCell() {
        return startCell;
    }

    /**
     * Establece la celda de inicio para la resolución del laberinto.
     *
     * @param startCell La celda a establecer como inicio.
     */
    public void setStartCell(Cell startCell) {
        this.startCell = startCell;
        System.out.println("Controlador: Celda de inicio fijada a " + startCell);
    }

    /**
     * Obtiene la celda de fin actualmente seleccionada.
     *
     * @return La celda de fin.
     */
    public Cell getEndCell() {
        return endCell;
    }

    /**
     * Establece la celda de fin para la resolución del laberinto.
     *
     * @param endCell La celda a establecer como fin.
     */
    public void setEndCell(Cell endCell) {
        this.endCell = endCell;
        System.out.println("Controlador: Celda de fin fijada a " + endCell);
    }

    /**
     * Obtiene el objeto {Maze} actualmente gestionado por el controlador.
     *
     * @return El objeto {Maze}.
     */
    public Maze getMaze() {
        return maze;
    }

    /**
     * Establece un nuevo objeto {Maze} para el controlador.
     *
     * @param maze El nuevo objeto {Maze} a establecer.
     */
    public void setMaze(Maze maze) {
        this.maze = maze;
    }
}
