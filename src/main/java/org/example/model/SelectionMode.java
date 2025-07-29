package org.example.model;

/**
 * Define los diferentes modos de selección disponibles para interactuar con las celdas del laberinto
 * en la interfaz de usuario.
 */
public enum SelectionMode {
    /**
     * Modo en el que no hay ninguna celda seleccionada para un propósito específico.
     */
    NONE,
    /**
     * Modo para seleccionar la celda de inicio del laberinto.
     */
    START_CELL,
    /**
     * Modo para seleccionar la celda de fin (destino) del laberinto.
     */
    END_CELL,
    /**
     * Modo para seleccionar celdas y convertirlas en obstáculos (muros) o eliminarlos.
     */
    OBSTACLE_CELL
}
