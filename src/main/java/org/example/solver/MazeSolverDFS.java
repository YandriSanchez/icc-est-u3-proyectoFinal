package org.example.solver;

import java.util.ArrayList;
import java.util.HashMap; // Para invertir el camino si se construye de fin a inicio
import java.util.LinkedHashSet;
import java.util.List; // Usaremos Stack para DFS
import java.util.Map; // Para mantener el orden de visita si es importante
import java.util.Set;
import java.util.Stack; // Para reconstruir el camino

import org.example.model.Cell;
import org.example.model.MazeResult;
import org.example.model.MazeSolver;

/**
 * Implementación del algoritmo de resolución de laberintos Depth-First Search (DFS) de forma iterativa.
 * DFS explora el laberinto lo más profundo posible a lo largo de cada rama antes de retroceder.
 * Utiliza una pila (Stack) para gestionar las celdas a visitar.
 * NO garantiza encontrar el camino más corto.
 */
public class MazeSolverDFS implements MazeSolver {

    private boolean[][] grid;
    private Set<Cell> visited; // Conjunto para almacenar todas las celdas visitadas
    private Map<Cell, Cell> parentMap; // Mapa para reconstruir el camino (simula el backtracking en la recursión)

    @Override
    public MazeResult getPath(boolean[][] grid, Cell start, Cell end) {
        // Reiniciamos las estructuras de datos para cada nueva búsqueda
        this.grid = grid;
        this.visited = new LinkedHashSet<>();
        this.parentMap = new HashMap<>();

        // Verificaciones básicas del laberinto
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
            // (simulando una recursión).
            // Si el orden de tus recursivos era Abajo, Derecha (para el 2-dir)
            // o Derecha, Abajo, Arriba, Izquierda (para el 4-dir), aquí podrías ajustarlo.
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

        // Devolvemos el resultado: el camino encontrado y todas las celdas visitadas
        return new MazeResult(path, visited);
    }

    /**
     * Verifica si una celda está dentro de los límites del laberinto, es un camino (no muro)
     * y no ha sido visitada previamente.
     * @param cell La celda a verificar.
     * @return true si la celda es válida para la exploración, false en caso contrario.
     */
    private boolean isValid(Cell cell) {
        if (!isInMaze(cell)) {
            return false; // Fuera de los límites del laberinto
        }
        // Verifica si es un muro (grid[row][col] == false) o si ya fue visitada
        return grid[cell.getRow()][cell.getCol()] && !visited.contains(cell);
    }

    /**
     * Verifica si una celda está dentro de los límites del laberinto.
     * @param cell La celda a verificar.
     * @return true si la celda está dentro del laberinto, false en caso contrario.
     */
    private boolean isInMaze(Cell cell) {
        int row = cell.getRow();
        int col = cell.getCol();
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
    }
}