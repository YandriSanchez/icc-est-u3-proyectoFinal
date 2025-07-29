package org.example.model;

/**
 * Define el contrato para cualquier algoritmo capaz de resolver un laberinto.
 * Las implementaciones de esta interfaz deben encontrar un camino desde
 * una celda de inicio a una celda de fin en una cuadrícula de laberinto,
 * y registrar las celdas visitadas durante el proceso.
 */
public interface MazeSolver {
    /**
     * Calcula y devuelve el resultado de la resolución de un laberinto.
     * Este resultado incluye el camino encontrado (si existe) y todas las celdas visitadas
     * durante la búsqueda.
     *
     * @param grid La cuadrícula booleana del laberinto, donde 'true' es camino y 'false' es muro.
     * @param start La celda de inicio desde la cual comenzar la búsqueda.
     * @param end La celda de destino a la que se debe llegar.
     * @return Un objeto MazeResult que contiene la lista del camino y el conjunto de celdas visitadas.
     */
    MazeResult getPath(boolean[][] grid,Cell star, Cell end);
}
