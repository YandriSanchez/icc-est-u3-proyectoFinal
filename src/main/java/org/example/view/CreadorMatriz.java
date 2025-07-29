package org.example.view;

import javax.swing.JOptionPane;

/**
 * Utilidad para solicitar las dimensiones (filas, columnas) de un laberinto.
 * Valida que las entradas sean enteros mayores a 4.
 */
public class CreadorMatriz {

    /**
     * Solicita al usuario el número de filas y columnas del laberinto.
     * Asegura que las entradas sean números enteros mayores que 4.
     * Permite al usuario cancelar la operación.
     *
     * @return int[2] donde [0]=filas, [1]=columnas, o null si se cancela.
     */
    public static int[] solicitarTamanioMatriz() {
        String filasStr = JOptionPane.showInputDialog(null, "Introduce el número de filas (mayor que 4):", "Tamaño del Laberinto", JOptionPane.PLAIN_MESSAGE);

        if (filasStr == null) {
            return null;
        }

        int filas;
        try {
            filas = Integer.parseInt(filasStr);
            if (filas <= 4) {
                JOptionPane.showMessageDialog(null, "El número de filas debe ser mayor que 4.", "Error", JOptionPane.ERROR_MESSAGE);
                return solicitarTamanioMatriz();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada inválida. Por favor, introduce un número para las filas.", "Error", JOptionPane.ERROR_MESSAGE);
            return solicitarTamanioMatriz();
        }

        String columnasStr = JOptionPane.showInputDialog(null, "Introduce el número de columnas (mayor que 4):", "Tamaño del Laberinto", JOptionPane.PLAIN_MESSAGE);

        if (columnasStr == null) {
            return null;
        }

        int columnas;
        try {
            columnas = Integer.parseInt(columnasStr);
            if (columnas <= 4) {
                JOptionPane.showMessageDialog(null, "El número de columnas debe ser mayor que 4.", "Error", JOptionPane.ERROR_MESSAGE);
                return solicitarTamanioMatriz();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada inválida. Por favor, introduce un número para las columnas.", "Error", JOptionPane.ERROR_MESSAGE);
            return solicitarTamanioMatriz();
        }

        return new int[]{filas, columnas};
    }
}
