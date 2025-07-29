package org.example.controller;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.Timer;

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
    public AtomicInteger animationIndex;
    public AtomicInteger pathAnimationIndex;
    private Timer animationTimer;
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
     * Inicia el proceso de resolución y visualización del laberinto en modo AUTOMÁTICO.
     * Muestra celdas visitadas y, si se encuentra, el camino.
     * Si no se selecciona inicio o fin, muestra una advertencia.
     *
     * @param solverType El nombre del algoritmo seleccionado (ej. "BFS", "Metodo Recursivo").
     */
    public void startSolvingMaze(String solverType) {
        if (startCell == null || endCell == null) {
            JOptionPane.showMessageDialog(view,
                "Por favor, selecciona las celdas de inicio y fin antes de resolver.",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Limpia cualquier visualización previa y sincroniza los muros
        resetPathColorsInView(); 
        syncViewToModelWalls();

        MazeSolver solver = solversMap.get(solverType);
        if (solver == null) {
            JOptionPane.showMessageDialog(view, "Algoritmo de resolución no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        new SwingWorker<MazeResult, Void>() {
            @Override
            protected MazeResult doInBackground() throws Exception {
                // Ejecuta el algoritmo en un hilo de fondo
                return solver.getPath(maze.getGrid(), startCell, endCell);
            }

            @Override
            protected void done() {
                try {
                    MazeResult result = get();
                    // Almacena las listas completas para la animación
                    currentVisitedCellsAnimation = new ArrayList<>(result.getVisited());
                    currentPathCellsAnimation = result.getPath(); 

                    // Reinicia los índices de animación para empezar desde el principio
                    animationIndex.set(0);
                    pathAnimationIndex.set(0); 

                    // Detiene cualquier animación anterior si está corriendo
                    if (animationTimer != null && animationTimer.isRunning()) {
                        animationTimer.stop();
                    }

                    // Inicia el timer para la animación automática SIEMPRE que haya celdas visitadas.
                    // Si currentVisitedCellsAnimation está vacío, simplemente el timer no animará nada
                    // y el JOptionPane de "Sin Exploración" o "Sin Camino" se manejará
                    // en el método animateNextCell() cuando se determine que no hay más celdas que pintar.
                    if (!currentVisitedCellsAnimation.isEmpty()) {
                        animationTimer = new Timer(ANIMATION_DELAY_MS, (ActionEvent e) -> {
                            animateNextCell();
                        });
                        animationTimer.start();
                    } else {
                        // Si currentVisitedCellsAnimation está vacío, llama a animateNextCell una vez
                        // para que maneje el mensaje de "sin exploración" o "sin camino"
                        // si no hay nada que animar.
                        animateNextCell(); 
                    }
                } catch (HeadlessException | InterruptedException | ExecutionException e) {
                    JOptionPane.showMessageDialog(view, "Error al resolver el laberinto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    /**
     * Realiza un solo paso de la animación del laberinto en modo "Paso a Paso".
     * Recalcula el camino si es la primera vez o si los índices se resetearon,
     * y luego pinta la siguiente celda en la secuencia. Las celdas se acumulan.
     *
     * @param solverType El nombre del algoritmo a usar (ej. "DFS", "BFS").
     */
    public void performSingleStep(String solverType) {
        if (startCell == null || endCell == null) {
            JOptionPane.showMessageDialog(view,
                "Por favor, selecciona las celdas de inicio y fin antes de resolver.",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Siempre detener cualquier animación automática previa.
        stopAnimation();
        // Sincronizar los muros antes de resolver en modo paso a paso
        syncViewToModelWalls(); 

        MazeSolver solver = solversMap.get(solverType);
        if (solver == null) {
            JOptionPane.showMessageDialog(view, "Algoritmo de resolución no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Si no tenemos datos de la animación actual, o si estamos en el primer paso,
        if (currentVisitedCellsAnimation == null || (animationIndex.get() == 0 && pathAnimationIndex.get() == 0)) {
             new SwingWorker<MazeResult, Void>() {
                @Override
                protected MazeResult doInBackground() throws Exception {
                    return solver.getPath(maze.getGrid(), startCell, endCell);
                }

                @Override
                protected void done() {
                    try {
                        MazeResult result = get();
                        currentVisitedCellsAnimation = new ArrayList<>(result.getVisited());
                        currentPathCellsAnimation = result.getPath();

                        // Ahora que tenemos los datos, animamos el primer paso
                        animateNextStepForManualMode();

                    } catch (InterruptedException | ExecutionException e) {
                        JOptionPane.showMessageDialog(view, "Error al obtener el camino para paso a paso: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }.execute();
        } else {
            // Si ya tenemos los datos, simplemente animamos el siguiente paso
            animateNextStepForManualMode();
        }
    }

    /**
     * Detiene cualquier animación de laberinto que esté actualmente en progreso.
     * Asegura que el temporizador de animación se detenga.
     */
    public void stopAnimation() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
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
        if (currentVisitedCellsAnimation != null && animationIndex.get() < currentVisitedCellsAnimation.size()) {
            Cell cellToAnimate = currentVisitedCellsAnimation.get(animationIndex.get());
            MazeCellPanel panel = view.getCellPanels()[cellToAnimate.getRow()][cellToAnimate.getCol()];

            // Solo pinta si no es inicio o fin, y no es un muro
            if (!cellToAnimate.equals(startCell) && !cellToAnimate.equals(endCell) && maze.getGrid()[cellToAnimate.getRow()][cellToAnimate.getCol()]) {
                panel.setBackground(MazeView.VISITED_COLOR);
            }
            panel.revalidate();
            panel.repaint();
            animationIndex.getAndIncrement();
        } 
        // Pinta celdas del camino (una vez que todas las visitadas han sido pintadas)
        else if (currentPathCellsAnimation != null && pathAnimationIndex.get() < currentPathCellsAnimation.size()) {
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
        }
    }

    /**
     * Método auxiliar para animar el siguiente paso en el modo "Paso a Paso".
     * Llama a {#animateNextCell()} para pintar la siguiente celda en la secuencia.
     * Muestra un mensaje si no hay celdas para animar.
     */
    private void animateNextStepForManualMode() {
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
                // ¡IMPORTANTE! Si es un obstáculo, píntalo de WALL_COLOR
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
