package org.example.model;

/**
 * Representa la estructura de un laberinto como una cuadrícula booleana.
 * En esta cuadrícula, {true} indica una celda transitable (camino)
 * y {false} indica una celda bloqueada (muro).
 */
public class Maze {
    private boolean[][] grid;

    /**
     * Construye una nueva instancia de Maze con la cuadrícula especificada.
     *
     * @param grid La cuadrícula booleana que define el diseño del laberinto.
     */
    public Maze(boolean[][] grid){
        this.grid = grid;
    }

    /**
     * Establece una nueva cuadrícula para el laberinto.
     *
     * @param grid La nueva cuadrícula booleana que define el laberinto.
     */
    public void setGrid(boolean[][] grid) {
        this.grid = grid;
    }

    /**
     * Devuelve la cuadrícula booleana actual del laberinto.
     *
     * @return La cuadrícula booleana que representa el laberinto.
     */
    public boolean[][] getGrid() {
        return grid;
    }
}
