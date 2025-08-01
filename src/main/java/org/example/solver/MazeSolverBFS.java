package org.example.solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.example.model.Cell;
import org.example.model.MazeResult;
import org.example.model.MazeSolver;

/**
 * Implementación del algoritmo de resolución de laberintos Breadth-First Search (BFS).
 * BFS explora el laberinto nivel por nivel, garantizando encontrar el camino más corto.
 * Utiliza una cola (Queue) para gestionar las celdas a visitar.
 */
public class MazeSolverBFS implements MazeSolver {

    private boolean[][] grid;
    private Set<Cell> visited;
    private Map<Cell, Cell> parentMap;

    /**
     * Calcula y devuelve el resultado de la resolución de un laberinto utilizando el algoritmo BFS.
     * Este resultado incluye el camino más corto encontrado (si existe) y todas las celdas visitadas
     * durante el proceso de búsqueda.
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

        // Cola para BFS: almacena las celdas a explorar
        Queue<Cell> queue = new LinkedList<>();

        // Añadir la celda inicial a la cola y marcarla como visitada
        queue.offer(start);
        visited.add(start);

        boolean found = false;

        // Bucle principal de BFS
        while (!queue.isEmpty()) {
            Cell current = queue.poll(); // Saca la primera celda de la cola (FIFO)

            // Si llegamos al destino, hemos encontrado el camino más corto
            if (current.equals(end)) {
                found = true;
                break;
            }

            // Explora los vecinos de la celda actual (arriba, abajo, izquierda, derecha)
            // A diferencia de los solvers recursivos, BFS no se llama a sí mismo recursivamente
            // sino que añade vecinos a la cola.
            Cell[] neighbors = new Cell[] {
                new Cell(current.getRow() - 1, current.getCol()), // Arriba
                new Cell(current.getRow() + 1, current.getCol()), // Abajo
                new Cell(current.getRow(), current.getCol() - 1), // Izquierda
                new Cell(current.getRow(), current.getCol() + 1)  // Derecha
            };

            for (Cell neighbor : neighbors) {
                if (isValid(neighbor)) { // Verifica si el vecino es válido y no ha sido visitado
                    visited.add(neighbor); // Marca el vecino como visitado
                    parentMap.put(neighbor, current); // Guarda que 'current' es el padre de 'neighbor'
                    queue.offer(neighbor); // Añade el vecino a la cola para explorarlo más tarde
                }
            }
        }

        // Reconstruir el camino si se encontró el destino
        List<Cell> path = new ArrayList<>();
        if (found) {
            Cell current = end;
            while (current != null) {
                path.add(0, current); 
                current = parentMap.get(current); // Retrocede usando el mapa de padres
            }
        }

        // Devolvemos el resultado: el camino encontrado y todas las celdas visitadas
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
