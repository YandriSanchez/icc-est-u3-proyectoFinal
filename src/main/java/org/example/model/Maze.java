// src/main/java/org/example/modelo/Maze.java
package org.example.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Maze {
    public boolean[][] grid; // Cambiar a private y usar getter/setter es una buena práctica
    private int size; // Esta variable 'size' solo almacena el número de filas, deberías tener cols también o ajustar su uso.
                      // Para laberintos rectangulares, 'size' no es suficiente. Usaremos grid.length y grid[0].length.

    public Maze(boolean[][] grid){
        this.grid = grid;
        // La variable 'size' aquí solo almacena el número de filas.
        // Podrías tener: this.rows = grid.length; this.cols = grid[0].length;
        // Por ahora, para no romper tu código, la mantengo, pero tenlo en cuenta.
        size = grid.length;
    }

    // --- NUEVO: Setter para la cuadrícula ---
    public void setGrid(boolean[][] grid) {
        this.grid = grid;
        this.size = grid.length; // Actualizar size si se cambia la cuadrícula
    }

    // --- NUEVO: Getter para la cuadrícula (si grid se hace private) ---
    public boolean[][] getGrid() {
        return grid;
    }

    public void printMaze() {
        for (int row = 0; row < grid.length; row++) { // Usar grid.length para filas
            for (int col = 0; col < grid[row].length; col++) { // Usar grid[row].length para columnas
                System.out.print((grid[row][col] ? "-" : "*")+ " ");
            }
            System.out.println();
        }
    }

    public void printMazeVisited(MazeResult path){
        Set<Cell> visited = path.getVisited();
        for(int i =0; i<grid.length;i++){ // Usar grid.length para filas
            for(int j =0;j<grid[0].length;j++){ // Usar grid[0].length para columnas
                Cell current = new Cell(i, j);
            if (visited.contains(current)) {
                System.out.print(" o ");
            } else if (grid[i][j]) {
                System.out.print(" - ");
            } else {
                System.out.print(" * ");
            }
            }
            System.out.println();
        }
    }

    public void printMazePath(MazeResult path){
        List<Cell> paths = path.getPath();
        Set<Cell> visited = new HashSet<>(paths);
        for(int i =0; i<grid.length;i++){ // Usar grid.length para filas
            for(int j =0;j<grid[0].length;j++){ // Usar grid[0].length para columnas
                Cell current = new Cell(i, j);
            if (visited.contains(current)) {
                System.out.print(" o ");
            } else if (grid[i][j]) {
                System.out.print(" - ");
            } else {
                System.out.print(" * ");
            }
            }
            System.out.println();
        }
    }
}
