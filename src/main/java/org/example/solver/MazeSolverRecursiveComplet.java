package org.example.solver;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.example.model.Cell;
import org.example.model.MazeResult;
import org.example.model.MazeSolver;

/**
 * Implementación de un algoritmo recursivo de resolución de laberintos que explora las cuatro direcciones (arriba, abajo, izquierda, derecha).
 * A diferencia de un DFS estándar o un solver recursivo básico, este solver no incluye una lógica de "backtracking"
 * explícita para el camino; simplemente añade las celdas a la ruta. La lista 'path' reflejará
 * la secuencia de celdas visitadas en el orden de la primera ruta exitosa encontrada.
 */
public class MazeSolverRecursiveComplet implements MazeSolver {

    private boolean[][] grid;
    private Cell end;
    private final List<Cell> path = new ArrayList<>();
    private final Set<Cell> visited =  new LinkedHashSet<>();


    /**
     * Calcula y devuelve el resultado de la resolución de un laberinto utilizando un algoritmo recursivo completo.
     * Este resultado incluye el camino encontrado (si existe) y todas las celdas visitadas
     * durante el proceso de búsqueda. Si no se encuentra un camino, la lista 'path' estará vacía,
     * pero 'visited' seguirá conteniendo las celdas exploradas.
     *
     * @param grid La cuadrícula booleana del laberinto, donde 'true' es camino y 'false' es muro.
     * @param start La celda de inicio desde la cual comenzar la búsqueda.
     * @param end La celda de destino a la que se debe llegar.
     * @return Un objeto MazeResult que contiene la lista del camino y el conjunto de celdas visitadas.
     */
    @Override
    public MazeResult getPath(boolean[][] grid, Cell start, Cell end) {
        path.clear();
        visited.clear();
        this.grid = grid;
        this.end = end;
        if(grid == null || grid.length == 0){
            return new MazeResult(path, visited);
        }
        if(findPath(start)){
            return new MazeResult(path, visited);
        }else {
            return new MazeResult(new ArrayList<>(), visited);
        }
    }

    /**
     * Intenta encontrar un camino desde la celda actual hasta la celda final de forma recursiva,
     * explorando en las cuatro direcciones posibles (abajo, derecha, arriba, izquierda).
     * Las celdas exploradas se añaden a 'visited' y las celdas en la ruta de exploración actual a 'path'.
     *
     * @param current La celda actual desde la que se intenta encontrar un camino.
     * @return true si se encontró un camino hacia la celda final desde la celda actual, false en caso contrario.
     */
    public boolean findPath(Cell current){
        if(!isInMaze(current)){
            return false;
        }
        if(!isValid(current)) return false;

        visited.add(current);
        path.add(current);

        // Si se ha llegado a la celda final, retorna true
        if(current.equals(end)){
            return true;
        } 
        
        // Explora en las cuatro direcciones posibles
        return findPath( new Cell(current.getRow()+1,current.getCol())) || // Abajo
               findPath( new Cell(current.getRow(),current.getCol()+1)) || // Derecha
               findPath( new Cell(current.getRow()-1,current.getCol()))||  // Arriba
               findPath( new Cell(current.getRow(),current.getCol()-1));   // Izquierda
    }

    /**
     * Verifica si una celda es válida para la exploración. Una celda es válida si no es un muro
     * y no ha sido visitada previamente.
     *
     * @param current La celda a verificar.
     * @return true si la celda es válida, false en caso contrario.
     */
    private boolean isValid(Cell current) {
        return !(!grid[current.getRow()][current.getCol()] || visited.contains(current));
    }

    /**
     * Verifica si una celda está dentro de los límites de la cuadrícula del laberinto.
     *
     * @param current La celda a verificar.
     * @return true si la celda está dentro del laberinto, false en caso contrario.
     */
    private boolean isInMaze(Cell current) {
        int row = current.getRow();
        int col = current.getCol();
        return !(row<0|| row >= grid.length || col<0 || col>= grid[0].length);
    }     
}
