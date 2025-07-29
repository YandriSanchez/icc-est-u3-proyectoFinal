package org.example;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.example.controller.MazeController;
import org.example.view.CreadorMatriz;
import org.example.view.MazeView;

public class Main {
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
            //Crea la instancia del MazeController
            MazeController controller = new MazeController(view, filas, columnas); 
            //Pasa la referencia del controlador a la vista
            view.setController(controller); 
            view.setVisible(true);
        });
    }
}