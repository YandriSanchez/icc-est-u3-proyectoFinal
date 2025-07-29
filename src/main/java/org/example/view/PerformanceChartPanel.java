package org.example.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * <p>La clase {@code PerformanceChartPanel} es un componente Swing que extiende {@code JPanel}
 * y está diseñado para mostrar un gráfico de líneas utilizando la librería JFreeChart.</p>
 * <p>Este panel visualiza el rendimiento de diferentes algoritmos de resolución de laberintos,
 * mostrando el tiempo que cada algoritmo tardó en nanosegundos.</p>
 * <p>Requiere la librería JFreeChart en el classpath para su correcto funcionamiento.</p>
 */
public class PerformanceChartPanel extends JPanel {

    /**
     * <p>Construye una nueva instancia de {@code PerformanceChartPanel}.</p>
     * <p>Crea un gráfico de líneas que muestra el tiempo de ejecución (en nanosegundos)
     * para cada algoritmo proporcionado.</p>
     *
     * @param algorithms Una {@code List} de cadenas de texto que representan los nombres
     * de los algoritmos (ej. "Método BFS", "Método DFS"). Estos se usarán
     * como categorías en el eje X del gráfico.
     * @param times      Una {@code List} de valores {@code Long} que representan el tiempo
     * de ejecución en nanosegundos correspondiente a cada algoritmo
     * en la lista {@code algorithms}. Los índices deben coincidir.
     */
    public PerformanceChartPanel(List<String> algorithms, List<Long> times) {
        // Establece el layout del panel a BorderLayout para que el ChartPanel ocupe todo el espacio.
        setLayout(new BorderLayout());

        // Crea un dataset para almacenar los datos del gráfico.
        // DefaultCategoryDataset es adecuado para gráficos de categoría (líneas, barras).
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Itera sobre las listas de algoritmos y tiempos para poblar el dataset.
        // Cada par (tiempo, nombre_del_algoritmo) se añade como un valor.
        for (int i = 0; i < algorithms.size(); i++) {
            // Añade el valor de tiempo, la serie ("Tiempo (ns)"), y la categoría (nombre del algoritmo).
            dataset.addValue(times.get(i), "Tiempo (ns)", algorithms.get(i));
        }

        // Crea el objeto JFreeChart, que representa el gráfico en sí.
        JFreeChart chart = ChartFactory.createLineChart(
                "Rendimiento de Algoritmos de Resolución de Laberintos", // Título principal del gráfico
                "Algoritmo Solver",     // Etiqueta para el eje X (categorías)
                "Tiempo (ns)",          // Etiqueta para el eje Y (valores numéricos)
                dataset,                // El dataset que contiene los datos a graficar
                PlotOrientation.VERTICAL, // La orientación del gráfico (vertical, eje Y hacia arriba)
                true,                   // Indica si debe mostrar una leyenda (true para "Tiempo (ns)")
                true,                   // Indica si debe mostrar tooltips (información al pasar el ratón)
                false                   // Indica si debe generar URLs para las series (no necesario aquí)
        );

        // Opcional: Personalización básica del gráfico.
        // Establece el color de fondo del área del gráfico.
        chart.setBackgroundPaint(Color.white);

        // Puedes acceder al objeto CategoryPlot para realizar personalizaciones más detalladas.
        // Por ejemplo, para cambiar el color de las líneas de la cuadrícula:
        // CategoryPlot plot = chart.getCategoryPlot();
        // plot.setRangeGridlinePaint(Color.lightGray); // O Color.black, etc.
        // plot.setDomainGridlinePaint(Color.lightGray);

        // Crea un ChartPanel, que es un componente Swing que puede mostrar un JFreeChart.
        // También maneja la interacción del usuario con el gráfico (zoom, pan).
        ChartPanel chartPanel = new ChartPanel(chart);
        // Establece el tamaño preferido del panel para un diseño inicial.
        chartPanel.setPreferredSize(new Dimension(750, 550));
        // Añade el ChartPanel al PerformanceChartPanel, haciendo que ocupe todo el espacio central.
        add(chartPanel, BorderLayout.CENTER);
    }
}