package org.example.view;

import javax.swing.JPanel;

/**
 * Representa visualmente una celda individual en la cuadrícula del laberinto.
 * Extiende JPanel para ser mostrada en una interfaz gráfica.
 */
public class MazeCellPanel extends JPanel {
    private final int row;
    private final int col;
    private boolean isObstacle;

    /**
     * Crea un nuevo panel de celda de laberinto con las coordenadas especificadas.
     * Inicialmente, la celda no es un obstáculo.
     *
     * @param row La fila de la celda.
     * @param col La columna de la celda.
     */
    public MazeCellPanel(int row, int col) {
        this.row = row;
        this.col = col;
        this.isObstacle = false;
    }

    /**
     * Obtiene la fila de la celda.
     *
     * @return El índice de la fila.
     */
    public int getRow() {
        return row;
    }

    /**
     * Obtiene la columna de la celda.
     *
     * @return El índice de la columna.
     */
    public int getCol() {
        return col;
    }

    /**
     * Verifica si la celda es un obstáculo.
     *
     * @return true si es un obstáculo, false en caso contrario.
     */
    public boolean isObstacle() {
        return isObstacle;
    }

    /**
     * Establece si la celda es un obstáculo.
     *
     * @param obstacle true para hacerla un obstáculo, false para que sea camino.
     */
    public void setObstacle(boolean obstacle) {
        isObstacle = obstacle;
    }
}


