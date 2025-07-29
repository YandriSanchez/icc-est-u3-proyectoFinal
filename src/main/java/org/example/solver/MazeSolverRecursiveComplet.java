package org.example.solver;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.example.model.Cell;
import org.example.model.MazeResult;
import org.example.model.MazeSolver;

public class MazeSolverRecursiveComplet implements MazeSolver {

    private boolean[][] grid;
    private Cell end;
    private List<Cell> path = new ArrayList<>();
    private Set<Cell> visited =  new LinkedHashSet<>();


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
        }
        return new MazeResult(new ArrayList<>(), new LinkedHashSet<>());
    }

    public boolean findPath(Cell current){
        if(!isInMaze(current)){
            return false;
        }
        if(!isValid(current)) return false;

        visited.add(current);
        path.add(current); // Añade la celda al camino

        if(current.equals(end)){
            return true;
        } 
        
        // Intenta ir en las 4 direcciones. Si no hay camino, simplemente retorna false.
        // No se quita la celda del camino, simula "quedarse atascado".
        if(findPath( new Cell(current.getRow()+1,current.getCol())) ||
            findPath( new Cell(current.getRow(),current.getCol()+1)) || 
            findPath( new Cell(current.getRow()-1,current.getCol()))||
            findPath( new Cell(current.getRow(),current.getCol()-1))){
            return true;
        }
        
        // ¡Línea de backtracking eliminada!
        // path.remove(path.size()-1); // ELIMINADO
        
        return false;
    }


    private boolean isValid(Cell current) {
        if(!grid[current.getRow()][current.getCol()] || visited.contains(current)) return false;
        return true;
    }

    private boolean isInMaze(Cell current) {
        int row = current.getRow();
        int col = current.getCol();
        if(row<0|| row >= grid.length || col<0 || col>= grid[0].length){
            return false;
        }
        return true;
    }    
}
