package org.example.model;

/**
 * Representa una celda individual dentro de la cuadrícula del laberinto.
 * Cada celda se define por sus coordenadas de fila y columna.
 * Esta clase también proporciona métodos para comparación y hashing,
 * esenciales para su uso en colecciones como conjuntos y mapas.
 */
public class Cell {
    int row;
    int col;

    /**
     * Construye una nueva instancia de Celda con la fila y columna especificadas.
     *
     * @param row El índice de la fila de la celda.
     * @param col El índice de la columna de la celda.
     */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Devuelve el índice de la fila de esta celda.
     *
     * @return El índice de la fila.
     */
    public int getRow() {return row;}

    /**
     * Establece el índice de la fila de esta celda.
     *
     * @param row El nuevo índice de la fila.
     */
    public void setRow(int row) {this.row = row;}

    /**
     * Devuelve el índice de la columna de esta celda.
     *
     * @return El índice de la columna.
     */
    public int getCol() {return col;}

    /**
     * Establece el índice de la columna de esta celda.
     *
     * @param col El nuevo índice de la columna.
     */
    public void setCol(int col) {this.col = col;}

    /**
     * Devuelve una representación en cadena de la celda en el formato "[fila , columna] ;".
     *
     * @return Una cadena que representa las coordenadas de la celda.
     */
    @Override
    public String toString() {
        return "["+ row + " , " + col + "] ;";
    }

    /**
     * Genera un código hash para esta celda basado en su fila y columna.
     * Este método es esencial para el almacenamiento y recuperación eficientes
     * de objetos Celda en colecciones basadas en hash.
     *
     * @return El código hash para esta celda.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + row;
        result = prime * result + col;
        return result;
    }

    /**
     * Compara esta celda con el objeto especificado. El resultado es verdadero si y solo si
     * el argumento no es nulo y es un objeto Celda que representa las mismas coordenadas
     * de fila y columna que esta celda.
     *
     * @param obj El objeto con el que comparar esta Celda.
     * @return Verdadero si el objeto dado representa una Celda equivalente a esta, falso en caso contrario.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Cell other = (Cell) obj;
        if (row != other.row)
            return false;
        return col == other.col;
    }
}
