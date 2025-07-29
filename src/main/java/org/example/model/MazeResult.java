package org.example.model;

import java.util.List;
import java.util.Set;

/**
 * Encapsula los resultados de un algoritmo de resolución de laberintos.
 * Contiene el camino encontrado (si existe) y el conjunto de todas las celdas visitadas
 * durante el proceso de búsqueda.
 */
public class MazeResult {
    private List<Cell> path;
    private Set<Cell> visited;

    /**
     * Construye una nueva instancia de MazeResult con el camino y las celdas visitadas especificadas.
     *
     * @param path Una lista de celdas que forman el camino de solución del laberinto. Estará vacía si no se encontró un camino.
     * @param visited Un conjunto de todas las celdas exploradas por el algoritmo.
     */
    public MazeResult(List<Cell> path, Set<Cell> visited) {
        this.path = path;
        this.visited = visited;
    }

    /**
     * Devuelve la lista de celdas que forman el camino de solución.
     * Si no se encontró un camino, esta lista estará vacía.
     *
     * @return Una lista de objetos Cell que representan el camino.
     */
    public List<Cell> getPath() {return path;}

    /**
     * Establece la lista de celdas que forman el camino de solución.
     *
     * @param path La nueva lista de celdas para el camino.
     */
    public void setPath(List<Cell> path) {this.path = path;}

    /**
     * Devuelve el conjunto de todas las celdas visitadas (exploradas) por el algoritmo.
     *
     * @return Un conjunto de objetos Cell que fueron visitados.
     */
    public Set<Cell> getVisited() {return visited;}

    /**
     * Establece el conjunto de celdas visitadas.
     *
     * @param visited El nuevo conjunto de celdas visitadas.
     */
    public void setVisited(Set<Cell> visited) {this.visited = visited;}

    /**
     * Devuelve una representación en cadena de los resultados del laberinto,
     * incluyendo el camino y las celdas visitadas.
     *
     * @return Una cadena que describe el camino y las celdas visitadas.
     */
    @Override
    public String toString() {
        return "Path=" + path + "\nVisited=" + visited;
    }
}
