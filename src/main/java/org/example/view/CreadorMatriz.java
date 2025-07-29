package org.example.view;

import javax.swing.JOptionPane;

public class CreadorMatriz {

    /**
     * Solicita al usuario el número de filas y columnas para la matriz del laberinto.
     * Valida que las entradas sean números enteros positivos.
     * Permite al usuario cancelar la operación en cualquier momento.
     *
     * @return Un arreglo de int[2] donde [0] es el número de filas y [1] es el número de columnas.
     * Retorna null si el usuario cancela la operación en cualquiera de los cuadros de diálogo.
     */
    public static int[] solicitarTamanioMatriz() {
        String filasStr = JOptionPane.showInputDialog(null, "Introduce el número de filas para el laberinto:", "Tamaño del Laberinto", JOptionPane.PLAIN_MESSAGE);

        // Si el usuario cancela el cuadro de diálogo de filas
        if (filasStr == null) {
            return null;
        }

        int filas;
        try {
            filas = Integer.parseInt(filasStr);
            if (filas <= 0) {
                JOptionPane.showMessageDialog(null, "El número de filas debe ser mayor que cero.", "Error", JOptionPane.ERROR_MESSAGE);
                return solicitarTamanioMatriz(); // Vuelve a solicitar si la entrada es inválida
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada inválida. Por favor, introduce un número para las filas.", "Error", JOptionPane.ERROR_MESSAGE);
            return solicitarTamanioMatriz(); // Vuelve a solicitar si no es un número
        }

        String columnasStr = JOptionPane.showInputDialog(null, "Introduce el número de columnas para el laberinto:", "Tamaño del Laberinto", JOptionPane.PLAIN_MESSAGE);

        // Si el usuario cancela el cuadro de diálogo de columnas
        if (columnasStr == null) {
            return null;
        }

        int columnas;
        try {
            columnas = Integer.parseInt(columnasStr);
            if (columnas <= 0) {
                JOptionPane.showMessageDialog(null, "El número de columnas debe ser mayor que cero.", "Error", JOptionPane.ERROR_MESSAGE);
                return solicitarTamanioMatriz(); // Vuelve a solicitar si la entrada es inválida
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada inválida. Por favor, introduce un número para las columnas.", "Error", JOptionPane.ERROR_MESSAGE);
            return solicitarTamanioMatriz(); // Vuelve a solicitar si no es un número
        }

        return new int[]{filas, columnas}; // Retorna las dimensiones si son válidas
    }
}
