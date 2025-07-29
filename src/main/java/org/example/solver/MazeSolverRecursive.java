package org.example.solver;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.example.model.Cell;
import org.example.model.MazeResult;
import org.example.model.MazeSolver;

/**
 * Implementación de un algoritmo recursivo básico para resolver laberintos.
 * Este solucionador intenta encontrar un camino moviéndose hacia abajo o hacia la derecha.
 * No realiza backtracking del camino, por lo que la lista 'path' reflejará la última
 * ruta explorada, incluso si no conduce al destino final.
 */
public class MazeSolverRecursive implements MazeSolver {

    private boolean[][] grid;
    private Cell end;
    private List<Cell> path = new ArrayList<>();
    private Set<Cell> visited =  new LinkedHashSet<>();


    /**
     * Calcula y devuelve el resultado de la resolución de un laberinto utilizando un algoritmo recursivo.
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
     * Intenta encontrar un camino desde la celda actual hasta la celda final de forma recursiva.
     * Explora moviéndose hacia abajo o hacia la derecha. Las celdas exploradas se añaden a 'visited'
     * y las celdas en la ruta actual de exploración a 'path'.
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
        
        // Si hemos llegado a la celda final, retornamos true
        if(current.equals(end)){
            return true;
        } 

        return findPath( new Cell(current.getRow()+1,current.getCol())) ||
                 findPath( new Cell(current.getRow(),current.getCol()+1));
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
     * Esta lista puede contener el camino completo si se encontró, o la última
     * ruta explorada si no se llegó al destino.
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

    /**
     * Devuelve el conjunto de todas las celdas visitadas (exploradas) por el algoritmo.
     *
     * @return Un conjunto de objetos Cell que fueron visitados.
     */
    public Set<Cell> getVisited() {
        return visited;
    }

    /**
     * Establece el conjunto de celdas visitadas.
     *
     * @param visited El nuevo conjunto de celdas visitadas.
     */
    public void setVisited(Set<Cell> visited) {
        this.visited = visited;
    }
}
