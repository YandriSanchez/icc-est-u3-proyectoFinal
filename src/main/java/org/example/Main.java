package org.example;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.example.controller.MazeController;
import org.example.view.CreadorMatriz;
import org.example.view.MazeView;

/**
 * Clase principal que inicia la aplicación del creador y solucionador de laberintos.
 * Coordina la solicitud de dimensiones, la creación de la vista y el controlador.
 */
public class Main {
    /**
     * Método de entrada principal de la aplicación.
     * Solicita las dimensiones del laberinto al usuario e inicializa la GUI.
     *
     * @param args Argumentos de la línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        // Solicita al usuario el tamaño de la matriz
        int[] dimensions = CreadorMatriz.solicitarTamanioMatriz();

        // Si el usuario cancela la operación, cierra la aplicación
        if (dimensions == null) {
            JOptionPane.showMessageDialog(
                null,
                "Operación cancelada por el usuario. La aplicación se cerrará.",
                "Cancelado",
                JOptionPane.WARNING_MESSAGE
            );
            System.exit(0);
        }

        final int filas = dimensions[0];
        final int columnas = dimensions[1];

        // Ejecuta la interfaz gráfica en el Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            MazeView view = new MazeView(filas, columnas);
            MazeController controller = new MazeController(view, filas, columnas); 
            view.setController(controller); 
            view.setVisible(true);
        });
    }
}