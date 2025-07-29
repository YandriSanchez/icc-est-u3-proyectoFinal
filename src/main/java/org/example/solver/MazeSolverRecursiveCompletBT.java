package org.example.solver;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.example.model.Cell;
import org.example.model.MazeResult;
import org.example.model.MazeSolver;

/**
 * Implementación de un algoritmo recursivo completo para resolver laberintos con backtracking.
 * Este solucionador explora las cuatro direcciones (arriba, abajo, izquierda, derecha)
 * y retrocede (backtracking) si una ruta no lleva al destino.
 * Esto asegura que la lista 'path' contenga solo las celdas del camino exitoso.
 */
public class MazeSolverRecursiveCompletBT implements MazeSolver{
    private List<Cell> path = new ArrayList<>();
    private final Set<Cell> visited =  new LinkedHashSet<>();
    private boolean[][] grid;
    private Cell end;

    /**
     * Calcula y devuelve el resultado de la resolución de un laberinto utilizando
     * un algoritmo recursivo con backtracking. Este resultado incluye el camino
     * encontrado (si existe) y todas las celdas visitadas durante el proceso de búsqueda.
     * Si no se encuentra un camino, la lista 'path' estará vacía.
     *
     * @param grid La cuadrícula booleana del laberinto, donde 'true' es camino y 'false' es muro.
     * @param star La celda de inicio desde la cual comenzar la búsqueda.
     * @param end La celda de destino a la que se debe llegar.
     * @return Un objeto MazeResult que contiene la lista del camino y el conjunto de celdas visitadas.
     */
    @Override
    public MazeResult getPath(boolean[][] grid, Cell star, Cell end) {
        path.clear();
        visited.clear();
        this.grid = grid;
        this.end = end;
        if(grid == null||grid.length==0) return new MazeResult(new ArrayList<>(),new LinkedHashSet<>());
        if(findPath(star)){
            MazeResult resultado = new MazeResult(path, visited);
            return resultado;
        }else {
            return new MazeResult(new ArrayList<>(), visited);
        }
    }

    /**
     * Intenta encontrar un camino desde la celda actual hasta la celda final de forma recursiva,
     * explorando en las cuatro direcciones posibles (abajo, derecha, arriba, izquierda).
     * Las celdas exploradas se añaden a 'visited' y 'path'. Si una ruta no lleva al destino,
     * la celda se remueve de 'path' (backtracking).
     *
     * @param current La celda actual desde la que se intenta encontrar un camino.
     * @return true si se encontró un camino hacia la celda final desde la celda actual, false en caso contrario.
     */
    private boolean findPath(Cell current) {
        if(!isInMaze(current)){
            return false;
        }
        if(!isValid(current)) return false;

        visited.add(current);
        path.add(current);
        if(current.equals(end)){
            return true;
        } 
        if(findPath( new Cell(current.getRow()+1,current.getCol())) || // Abajo
           findPath( new Cell(current.getRow(),current.getCol()+1)) || // Derecha
           findPath( new Cell(current.getRow()-1,current.getCol()))||  // Arriba
           findPath( new Cell(current.getRow(),current.getCol()-1))){  // Izquierda
            return true;
        }
        path.remove(path.size()-1); // Backtracking: remueve la celda del camino si no lleva a una solución.
        return false;         
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

    /**
     * Devuelve la lista de celdas que forman el camino de solución actual.
     * Esta lista contendrá el camino completo si se encontró, o estará vacía si no.
     *
     * @return Una lista de objetos Cell que representan el camino.
     */
    public List<Cell> getPath() {
        return path;
    }

    /**
     * Establece la lista de celdas para el camino.
     *
     * @param path La nueva lista de celdas para el camino.
     */
    public void setPath(List<Cell> path) {
        this.path = path;
    }
}
