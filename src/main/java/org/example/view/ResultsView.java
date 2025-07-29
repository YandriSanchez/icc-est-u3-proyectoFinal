package org.example.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * Ventana para mostrar los resultados de la resolución del laberinto en una tabla.
 * Permite visualizar métricas como cantidad de celdas del camino y tiempo de ejecución.
 */
public class ResultsView extends JFrame {

    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private static final String CSV_FILE = "maze_results.csv";

    /**
     * Constructor de ResultsView. Configura la ventana y carga los resultados del CSV.
     */
    public ResultsView() {
        super("Resultados de Solución del Laberinto");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo esta ventana
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        setupTable();
        loadResultsFromCsv();
        setupButtons();

        setVisible(true);
    }

    /** Configura la tabla y su modelo para mostrar los resultados. */
    private void setupTable() {
        String[] columnNames = {"Método Solver", "Cantidad de Celdas", "Tiempo (ns)"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Las celdas no son editables
            }
        };
        resultsTable = new JTable(tableModel);
        resultsTable.setFillsViewportHeight(true); // Hace que la tabla ocupe toda la altura disponible
        add(new JScrollPane(resultsTable), BorderLayout.CENTER); // Envuelve la tabla en un JScrollPane
    }

    /**
     * Carga los resultados desde el archivo CSV y los muestra en la tabla.
     * Si el archivo no existe o está vacío, la tabla estará vacía.
     */
    private void loadResultsFromCsv() {
        tableModel.setRowCount(0); // Limpia la tabla antes de cargar
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    tableModel.addRow(data);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo CSV de resultados no encontrado. Se creará uno nuevo al guardar.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer los resultados: " + e.getMessage(), "Error de Lectura", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Configura los botones "Eliminar Resultados" y "Ver Gráfica". */
    private void setupButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton deleteButton = new JButton("Eliminar Resultados");
        JButton viewGraphButton = new JButton("Ver Gráfica");

        deleteButton.addActionListener(e -> deleteResults());
        viewGraphButton.addActionListener(e -> createAndShowPerformanceChart());

        buttonPanel.add(deleteButton);
        buttonPanel.add(viewGraphButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void createAndShowPerformanceChart() {
        // Preparar los datos para el gráfico
        List<String> algorithms = new ArrayList<>();
        List<Long> times = new ArrayList<>();

        TableModel model = resultsTable.getModel();
        int rowCount = model.getRowCount();

        // Asumiendo que la columna 0 es el Algoritmo y la columna 2 es el Tiempo (en nanosegundos)
        // Ajusta los índices de columna si tu tabla tiene una estructura diferente.
        int algorithmCol = 0; // Columna para el nombre del algoritmo
        int timeCol = 2;      // Columna para el tiempo en nanosegundos

        for (int i = 0; i < rowCount; i++) {
            try {
                String algorithmName = (String) model.getValueAt(i, algorithmCol);
                // Asegúrate de que el valor sea un Long o conviértelo si es String
                Object timeValue = model.getValueAt(i, timeCol);
                long timeNano;

                switch (timeValue) {
                    case Long aLong -> timeNano = aLong;
                    case Integer integer -> timeNano = integer.longValue();
                    case String timeStr -> timeNano = Long.parseLong(timeStr.replaceAll("[^\\d]", "")); // Elimina " ns" y parsea
                    default -> {
                        System.err.println("Tipo de dato de tiempo inesperado: " + timeValue.getClass().getName());
                        continue; // Saltar esta fila si no se puede parsear el tiempo
                    }
                }
                
                algorithms.add(algorithmName);
                times.add(timeNano);

            } catch (NumberFormatException ex) {
                System.err.println("Error al obtener datos de la tabla para el gráfico: " + ex.getMessage());
            }
        }

        if (algorithms.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay datos disponibles en la tabla para generar el gráfico.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Crear y mostrar la ventana del gráfico
        SwingUtilities.invokeLater(() -> {
            JFrame chartFrame = new JFrame("Rendimiento de Algoritmos de Laberinto");
            chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Solo cierra la ventana del gráfico
            chartFrame.setSize(800, 600);
            chartFrame.setLocationRelativeTo(this); // Centrar respecto a la ventana principal

            // Aquí es donde usaremos JFreeChart. Necesitarás añadir la librería.
            // Para fines de ejemplo, asumiré que tienes JFreeChart configurado.
            PerformanceChartPanel chartPanel = new PerformanceChartPanel(algorithms, times);
            chartFrame.add(chartPanel);
            chartFrame.setVisible(true);
        });
    }

    /** Elimina el archivo CSV de resultados y limpia la tabla. */
    private void deleteResults() {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que quieres eliminar todos los resultados?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            File csv = new File(CSV_FILE);
            if (csv.delete()) {
                tableModel.setRowCount(0); // Limpia la tabla en la GUI
                JOptionPane.showMessageDialog(this, "Resultados eliminados exitosamente.", "Eliminación Completa", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("Archivo CSV de resultados eliminado.");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el archivo de resultados.", "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("Error: No se pudo eliminar el archivo CSV de resultados.");
            }
        }
    }

    /**
 * <p>Guarda una lista de resultados en el archivo CSV.</p>
 * <p>Si el archivo no existe o está vacío, se añadirán los encabezados.</p>
 * <p>Si un resultado para un algoritmo ya existe en el archivo, su entrada se actualizará.
 * Si es un algoritmo nuevo, se añadirá al final.</p>
 * <p>Cada resultado es un array de Strings con [Nombre del Método, Cantidad de Celdas, Tiempo (ns)].</p>
 *
 * @param newResults La lista de resultados a guardar/actualizar.
 */
public static void saveResultsToCsv(List<String[]> newResults) {
    String CSV_FILE = "maze_results.csv"; // Define aquí la constante si no está globalmente
    File file = new File(CSV_FILE);
    List<String> fileLines = new ArrayList<>();
    String header = "Algoritmo,Longitud del Camino,Tiempo (ns)";
    boolean headerExists = false;

    // 1. Leer el archivo CSV existente (si lo hay)
    if (file.exists() && file.length() > 0) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            if ((line = br.readLine()) != null) {
                // Comprobar si la primera línea es el encabezado esperado
                if (line.equals(header)) {
                    headerExists = true;
                    fileLines.add(line); // Añadir el encabezado existente a la lista
                } else {
                    // Si la primera línea no es el encabezado, añadirla de todos modos
                    // o decidir si se debe sobrescribir todo el archivo si no tiene formato.
                    // Para este caso, la añadimos y asumimos que se corregirá si es necesario.
                    fileLines.add(line);
                }
            }
            while ((line = br.readLine()) != null) {
                fileLines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV existente: " + e.getMessage());
            return; // Salir si hay un error de lectura
        }
    }

    // Asegurarse de que el encabezado exista si el archivo estaba vacío o no existía
    if (!headerExists) {
        // Si el archivo estaba completamente vacío o no existía, o si no tenía encabezado, lo añadimos.
        // Lo ponemos al principio de la lista de líneas si no está ya.
        if (fileLines.isEmpty() || !fileLines.get(0).equals(header)) {
             fileLines.add(0, header);
        }
    }


    // 2. Procesar los nuevos resultados: actualizar o añadir
    for (String[] newRow : newResults) {
        String newAlgorithmName = newRow[0]; // El nombre del algoritmo del nuevo resultado
        boolean foundAndUpdated = false;

        // Iterar desde la segunda línea (después del encabezado)
        for (int i = 1; i < fileLines.size(); i++) {
            String existingLine = fileLines.get(i);
            String[] existingData = existingLine.split(",");
            if (existingData.length > 0 && existingData[0].equals(newAlgorithmName)) {
                // Si encontramos una coincidencia, actualizamos la línea
                fileLines.set(i, String.join(",", newRow));
                foundAndUpdated = true;
                break; // Salir del bucle interno, ya actualizamos esta fila
            }
        }

        if (!foundAndUpdated) {
            // Si no se encontró el algoritmo, añadir la nueva línea al final
            fileLines.add(String.join(",", newRow));
        }
    }

    // 3. Reescribir el archivo completo con los datos actualizados
    try (PrintWriter pw = new PrintWriter(new FileWriter(file, false))) { // ¡'false' para sobrescribir todo el archivo!
        for (String line : fileLines) {
            pw.println(line);
        }
        System.out.println("Resultados actualizados/añadidos en " + CSV_FILE);
    } catch (IOException e) {
        System.err.println("Error al reescribir resultados en CSV: " + e.getMessage());
    }
}
}