package org.example.solver;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.example.model.Cell;
import org.example.model.MazeResult;
import org.example.model.MazeSolver;

public class MazeSolverRecursiveCompletBT implements MazeSolver{
    private List<Cell> path = new ArrayList<>();
    private Set<Cell> visited =  new LinkedHashSet<>();
    private boolean[][] grid;
    private Cell end;

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
        }
        return new MazeResult(new ArrayList<>(),new LinkedHashSet<>());
    }

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
        if(findPath( new Cell(current.getRow()+1,current.getCol())) ||
            findPath( new Cell(current.getRow(),current.getCol()+1)) || 
            findPath( new Cell(current.getRow()-1,current.getCol()))||
            findPath( new Cell(current.getRow(),current.getCol()-1))){
            return true;
        }
        path.remove(path.size()-1);
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
