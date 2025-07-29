package org.example.controller;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.event.*;
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
import org.example.solver.MazeSolverRecursiveComplet; // Para el índice de animación
import org.example.solver.MazeSolverRecursiveCompletBT;
import org.example.view.MazeCellPanel;
import org.example.view.MazeView;


public class MazeController {

    private MazeView view;
    private Maze maze;

    private Cell startCell;
    private Cell endCell;

    private Map<String, MazeSolver> solversMap;

    // --- Nuevas variables para la animación y el modo "Paso a Paso" ---
    public List<Cell> currentVisitedCellsAnimation;
    public List<Cell> currentPathCellsAnimation;
    public AtomicInteger animationIndex;
    public AtomicInteger pathAnimationIndex;
    private Timer animationTimer;
    private final int ANIMATION_DELAY_MS = 150;// Retraso en milisegundos

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
        this.pathAnimationIndex = new AtomicInteger(0); // Asegúrate de que este esté inicializado
    }

    public void initializeMazeGrid(int rows, int cols) {
        boolean[][] newGrid = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                newGrid[i][j] = true; // Todas las celdas son caminos por defecto
            }
        }
        this.maze.setGrid(newGrid);
    }

    // --- Métodos de Acción ---

    /**
     * Inicia el proceso de resolución y visualización del laberinto en modo AUTOMÁTICO.
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

                    // Inicia el timer para la animación automática
                    if (!currentVisitedCellsAnimation.isEmpty()) { // Siempre se debe animar las visitadas
                        animationTimer = new Timer(ANIMATION_DELAY_MS, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                animateNextCell();
                            }
                        });
                        animationTimer.start();
                    } else {
                        // Si no se visitó ninguna celda (por ejemplo, inicio = fin o muro al lado)
                        if (currentPathCellsAnimation == null || currentPathCellsAnimation.isEmpty()) {
                            JOptionPane.showMessageDialog(view,
                                "No se encontró un camino desde el inicio hasta el fin.",
                                "Sin Camino", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(view, "Error al resolver el laberinto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    /**
     * Realiza un solo paso de la animación del laberinto en modo "Paso a Paso".
     * Recalcula el camino si es la primera vez o si los índices se resetearon,
     * y luego pinta la siguiente celda en la secuencia. Las celdas se acumulan.
     * @param solverType El nombre del algoritmo a usar.
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

        // ¡IMPORTANTE! resetPathColorsInView() NO SE LLAMA AQUÍ.
        // Esto permite que las celdas se acumulen con cada clic de "Paso a Paso".
        // La limpieza completa solo la hará el botón "Limpiar Laberinto".

        // Sincronizar los muros antes de resolver en modo paso a paso
        syncViewToModelWalls(); 

        MazeSolver solver = solversMap.get(solverType);
        if (solver == null) {
            JOptionPane.showMessageDialog(view, "Algoritmo de resolución no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Si es la primera vez que se presiona "Paso a Paso" o si los índices se resetearon,
        // necesitamos ejecutar el solver para obtener las listas de celdas.
        // Esto evita recalcular el laberinto en cada paso si ya lo tenemos.
        if (currentVisitedCellsAnimation == null || animationIndex.get() == 0 && pathAnimationIndex.get() == 0) {
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

                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(view, "Error al obtener el camino para paso a paso: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }.execute();
        } else {
            // Si ya tenemos los datos, simplemente animamos el siguiente paso
            animateNextStepForManualMode();
        }
    }

    // El método stopAnimation() se mantiene igual:
    public void stopAnimation() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
    }

    /**
     * Avanza un paso en la animación del laberinto.
     * Llamado por el botón "Paso a Paso".
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
                    "No se encontró un camino desde el inicio hasta el fin.",
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
     * Simplemente llama a animateNextCell, ya que currentVisitedCellsAnimation y
     * currentPathCellsAnimation ya deben estar cargados.
     */
    private void animateNextStepForManualMode() {
        // Asegúrate de que las listas de animación estén cargadas
        if (currentVisitedCellsAnimation == null || currentVisitedCellsAnimation.isEmpty() && 
            (currentPathCellsAnimation == null || currentPathCellsAnimation.isEmpty())) {
            JOptionPane.showMessageDialog(view, "No hay celdas para animar. Verifica si se encontró un camino o celdas visitadas.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        animateNextCell();
    }

    public void resetAnimationIndices() {
        animationIndex.set(0);
        pathAnimationIndex.set(0);
    }

    // El método resetMaze() (para el botón "Limpiar") se mantiene como lo definimos:
    // borrará TODO, incluyendo obstáculos, si es su intención.
    // Si "Limpiar" solo debe borrar la solución, este método debería ser más simple
    // (solo llamar a resetPathColorsInView() y resetear inicio/fin).
    public void resetMaze() {
        resetPathColorsInView(); // Esto despinta la solución y respeta obstáculos
        resetAnimationIndices();

        // Opcional: Si limpiar significa borrar también los obstáculos
        // int rows = maze.getGrid().length;
        // int cols = maze.getGrid()[0].length;
        // maze = new Maze(new boolean[rows][cols]);
        // initializeMazeGrid(rows, cols); // Reinicia el modelo a solo caminos

        startCell = null;
        endCell = null;
        if(view != null) {
            view.resetSelectedCells(); // Asegúrate de que este método en MazeView resetea visualmente las celdas de inicio/fin
            // Y si resetMaze no borra obstáculos del modelo, la vista debe recorrer los paneles
            // y si no son inicio/fin, y eran obstáculos, dejarlos como estaban.
            // O, si se borran del modelo, también borrarlos de la vista.
        }
    }

    // El método resetPathColorsInView() es CRÍTICO para no borrar los obstáculos
    // Asegúrate de que siga siendo como te lo he mostrado antes, respetando cellPanel.isObstacle()
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
     * Sincroniza el estado de las paredes del MazeView al Maze del controlador.
     * Esto es crucial antes de resolver para que el algoritmo trabaje con el laberinto
     * tal como el usuario lo ha dibujado.
     */
    public void syncViewToModelWalls() {
        boolean[][] currentViewGrid = view.getCellPanelsState();
        if (currentViewGrid != null) {
            this.maze.setGrid(currentViewGrid);
            System.out.println("Modelo de laberinto actualizado con las paredes de la vista.");
        }
    }

    public Cell getStartCell() {
        return startCell;
    }

    public void setStartCell(Cell startCell) {
        this.startCell = startCell;
        System.out.println("Controlador: Celda de inicio fijada a " + startCell);
    }

    public Cell getEndCell() {
        return endCell;
    }

    public void setEndCell(Cell endCell) {
        this.endCell = endCell;
        System.out.println("Controlador: Celda de fin fijada a " + endCell);
    }

    public Maze getMaze() {
        return maze;
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
    }
}
