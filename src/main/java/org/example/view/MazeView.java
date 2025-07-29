package org.example.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.example.controller.MazeController;
import org.example.model.Cell;
import org.example.model.SelectionMode;

public class MazeView extends JFrame {

    private JMenuBar menuBar;
    private JMenu archivoMenu, ayudaMenu;
    private JMenuItem nuevoLaberintoItem, verResultadosItem, acercaDeItem;
    private JButton celdaInicioButton, celdaFinalButton, obstaculoParedButton;
    private JPanel mazePanel;
    private MazeCellPanel[][] cellPanels;
    private JComboBox<String> algoritmoComboBox;
    private JButton resolverButton, pasoAPasoButton, limpiarButton;

    private SelectionMode currentSelectionMode = SelectionMode.NONE;
    private MazeCellPanel selectedStartCellPanel = null;
    private MazeCellPanel selectedEndCellPanel = null;
    private Cell selectedStartCell = null;
    private Cell selectedEndCell = null;

    // --- Definición de Colores para el Laberinto ---
    public static final Color DEFAULT_CELL_COLOR = Color.WHITE;     // Color por defecto para las celdas (caminos)
    public static final Color WALL_COLOR = Color.BLACK;             // Color para las paredes/obstáculos
    public static final Color START_COLOR = Color.GREEN;            // Color para la celda de inicio
    public static final Color END_COLOR = Color.RED;                // Color para la celda de fin
    public static final Color VISITED_COLOR = new Color(230, 230, 230); // Color para celdas visitadas (gris claro)
    public static final Color PATH_COLOR = Color.BLUE;

    // Referencia al controlador
    private MazeController controller;

    private boolean isStepByStepActive = false; // Nueva bandera para controlar el estado del "Paso a Paso"

    public MazeView(int filas, int columnas) {
        super("Maze Creator");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla

        setupMenuBar();
        JPanel topButtonsPanel = new JPanel();
        setupTopButtons(topButtonsPanel);
        setupMazePanel(filas, columnas);
        JPanel bottomControlsPanel = new JPanel();
        setupBottomControls(bottomControlsPanel);

        add(topButtonsPanel, BorderLayout.NORTH);
        add(mazePanel, BorderLayout.CENTER);
        add(bottomControlsPanel, BorderLayout.SOUTH);
    }

    public void setController(MazeController controller) {
        this.controller = controller;
    }

    private void setupMenuBar() {
        menuBar = new JMenuBar();
        archivoMenu = new JMenu("Archivos");
        nuevoLaberintoItem = new JMenuItem("Nuevo Laberinto");
        verResultadosItem = new JMenuItem("Ver Resultados"); // Por ahora sin funcionalidad
        archivoMenu.add(nuevoLaberintoItem);
        archivoMenu.add(verResultadosItem);

        ayudaMenu = new JMenu("Ayuda");
        acercaDeItem = new JMenuItem("Acerca de");
        ayudaMenu.add(acercaDeItem);

        menuBar.add(archivoMenu);
        menuBar.add(ayudaMenu);
        setJMenuBar(menuBar);

        nuevoLaberintoItem.addActionListener(e -> onNuevoLaberintoAction());
        verResultadosItem.addActionListener(e -> onVerResultadosAction());
        acercaDeItem.addActionListener(e -> onAcercaDeAction());
    }

    private void setupTopButtons(JPanel panel) {
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        celdaInicioButton = new JButton("Celda de Inicio");
        celdaFinalButton = new JButton("Celda de Final");
        obstaculoParedButton = new JButton("Obstáculo o Pared");

        panel.add(celdaInicioButton);
        panel.add(celdaFinalButton);
        panel.add(obstaculoParedButton);

        celdaInicioButton.addActionListener(e -> onCeldaInicioAction());
        celdaFinalButton.addActionListener(e -> onCeldaFinalAction());
        obstaculoParedButton.addActionListener(e -> onObstaculoParedAction());
    }

    private void setupMazePanel(int filas, int columnas) {
        mazePanel = new JPanel();
        mazePanel.setLayout(new GridLayout(filas, columnas));
        cellPanels = new MazeCellPanel[filas][columnas];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                MazeCellPanel cell = new MazeCellPanel(i, j);
                cell.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                cell.setPreferredSize(new Dimension(30, 30));
                cell.setBackground(DEFAULT_CELL_COLOR); // Color por defecto (camino)

                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        onCellClicked(cell);
                    }
                });

                cellPanels[i][j] = cell;
                mazePanel.add(cell);
            }
        }
        mazePanel.revalidate(); // Asegura que el panel se redibuje
        mazePanel.repaint();
    }

    private void setupBottomControls(JPanel panel) {
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.add(new JLabel("Algoritmo: "));

        // --- CAMBIO CLAVE AQUÍ: AÑADIR BFS y DFS al ComboBox ---
        String[] algoritmos = {
            "Metodo Recursivo",
            "Metodo Recursivo Completo",
            "Metodo Recursivo Completo BT",
            "Metodo BFS", // Nuevo algoritmo
            "Metodo DFS"  // Nuevo algoritmo
        };
        algoritmoComboBox = new JComboBox<>(algoritmos);
        algoritmoComboBox.setSelectedItem("Metodo Recursivo Completo BT"); // Puedes cambiar este por el que quieras por defecto
        panel.add(algoritmoComboBox);

        resolverButton = new JButton("Resolver");
        pasoAPasoButton = new JButton("Paso a Paso");
        limpiarButton = new JButton("Limpiar");

        panel.add(resolverButton);
        panel.add(pasoAPasoButton);
        panel.add(limpiarButton);

        resolverButton.addActionListener(e -> onResolverAction());
        pasoAPasoButton.addActionListener(e -> onPasoAPasoAction());
        limpiarButton.addActionListener(e -> onLimpiarAction());
    }

    // --- Métodos de Acción del Menú y Botones Superiores ---

    private void onNuevoLaberintoAction() {
        System.out.println("Acción: Nuevo Laberinto - Solicitando nuevas dimensiones.");
        // Detener cualquier animación en curso antes de cambiar el laberinto
        if (controller != null) {
            controller.stopAnimation();
        }

        int[] newDims = CreadorMatriz.solicitarTamanioMatriz();

        if (newDims != null) {
            final int newFilas = newDims[0];
            final int newColumnas = newDims[1];

            dispose(); // Cierra la ventana actual

            // Crea una nueva vista y un nuevo controlador para el nuevo laberinto
            SwingUtilities.invokeLater(() -> {
                MazeView newView = new MazeView(newFilas, newColumnas);
                MazeController newController = new MazeController(newView, newFilas, newColumnas);
                newView.setController(newController);
                newView.setVisible(true);
            });
        } else {
            JOptionPane.showMessageDialog(this,
                "Creación de nuevo laberinto cancelada.",
                "Cancelado",
                JOptionPane.WARNING_MESSAGE
            );
            System.out.println("Creación de nuevo laberinto cancelada por el usuario.");
        }
    }

    private void onVerResultadosAction() {
        System.out.println("Acción: Ver Resultados (sin acción implementada).");
    }

    private void onAcercaDeAction() {
        String linkText = "<html>Aplicación Creador de Laberintos con recursividad<br>" +
                          "Desarrollado por: Yandri Sanchez<br>" +
                          "El código lo puedes revisar en: <a href=\"https://github.com/YandriSanchez/icc-est-u3-proyectoFinal\">https://github.com/YandriSanchez/icc-est-u3-proyectoFinal</a></html>";

        JLabel messageLabel = new JLabel(linkText);
        messageLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        messageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/YandriSanchez/icc-est-u3-proyectoFinal"));
                } catch (Exception ex) {
                    System.err.println("Error al abrir el enlace: " + ex.getMessage());
                    JOptionPane.showMessageDialog(null,
                        "No se pudo abrir el enlace. Copia y pega en tu navegador:\n" +
                        "https://github.com/YandriSanchez/icc-est-u3-proyectoFinal",
                        "Error de Navegación", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JOptionPane.showMessageDialog(this,
                                      messageLabel,
                                      "Acerca de",
                                      JOptionPane.INFORMATION_MESSAGE);
    }

    private void onCeldaInicioAction() {
        System.out.println("Modo de selección: Celda de Inicio");
        currentSelectionMode = SelectionMode.START_CELL;
    }

    private void onCeldaFinalAction() {
        System.out.println("Modo de selección: Celda de Final");
        currentSelectionMode = SelectionMode.END_CELL;
    }

    private void onObstaculoParedAction() {
        System.out.println("Modo de selección: Obstáculo o Pared");
        currentSelectionMode = SelectionMode.OBSTACLE_CELL;
    }

    // --- Métodos de Acción de los Botones Inferiores ---

    private void onResolverAction() {
        System.out.println("Acción: Resolver Laberinto (Automático)");
        if (controller != null) {
            controller.resetPathColorsInView(); // Limpia solo caminos/visitadas
            controller.resetAnimationIndices(); // Asegurarse de que la nueva animación inicie desde cero
            isStepByStepActive = false; // Reiniciar la bandera si se inicia una resolución automática

            String selectedAlgorithm = (String) algoritmoComboBox.getSelectedItem();
            controller.startSolvingMaze(selectedAlgorithm);
        } else {
            System.err.println("Error: Controlador no está configurado para la vista.");
        }
    }

    private void onPasoAPasoAction() {
        if (controller == null) {
            System.err.println("Error: Controlador no está configurado para la vista.");
            return;
        }

        System.out.println("Acción: Paso a Paso (siguiente celda)");
        String selectedAlgorithm = (String) algoritmoComboBox.getSelectedItem();

        // Si NO está activo el modo "Paso a Paso" (es el primer clic)
        if (!isStepByStepActive) {
            controller.resetPathColorsInView(); // Limpia solo caminos/visitadas
            controller.resetAnimationIndices(); // También resetear los índices de animación.
            isStepByStepActive = true; // Activa la bandera para los clics subsiguientes
            System.out.println("Primer clic en 'Paso a Paso': reiniciando vista y animando.");
        } else {
            System.out.println("Clic consecutivo en 'Paso a Paso': continuando animación.");
        }

        // El controlador se encarga de ejecutar el siguiente paso de la animación.
        controller.performSingleStep(selectedAlgorithm);
    }

    // Método de acción para el botón "Limpiar Laberinto"
    private void onLimpiarAction() {
        System.out.println("Acción: Limpiar Laberinto");
        if (controller != null) {
            controller.resetPathColorsInView();
            controller.resetAnimationIndices();
            isStepByStepActive = false; // Reiniciar la bandera al limpiar el laberinto

            // ... (el resto del código de este método se mantiene igual) ...
        } else {
            System.err.println("Error: Controlador no está configurado para la vista.");
        }
    }

    // Asegúrate de que tu MazeView tenga un método como este para resetear visualmente
    public void resetSelectedCells() {
        // Deseleccionar visualmente las celdas de inicio y fin
        if (selectedStartCellPanel != null) {
            if (selectedStartCellPanel.isObstacle()) { // Si era un muro
                selectedStartCellPanel.setBackground(WALL_COLOR);
            } else {
                selectedStartCellPanel.setBackground(DEFAULT_CELL_COLOR);
            }
            selectedStartCellPanel.revalidate();
            selectedStartCellPanel.repaint();
            selectedStartCellPanel = null; // Quitar la referencia al panel
            selectedStartCell = null;      // Quitar la referencia a la celda lógica
        }
        if (selectedEndCellPanel != null) {
            if (selectedEndCellPanel.isObstacle()) { // Si era un muro
                selectedEndCellPanel.setBackground(WALL_COLOR);
            } else {
                selectedEndCellPanel.setBackground(DEFAULT_CELL_COLOR);
            }
            selectedEndCellPanel.revalidate();
            selectedEndCellPanel.repaint();
            selectedEndCellPanel = null; // Quitar la referencia al panel
            selectedEndCell = null;      // Quitar la referencia a la celda lógica
        }

        // Ahora recorre todos los paneles para asegurar que estén en su estado base (camino o muro)
        MazeCellPanel[][] panels = getCellPanels();
        if (panels != null) {
            for (int i = 0; i < panels.length; i++) {
                for (int j = 0; j < panels[0].length; j++) {
                    MazeCellPanel panel = panels[i][j];
                    if (!panel.isObstacle()) { // Si NO es un obstáculo, píntalo de blanco
                        panel.setBackground(MazeView.DEFAULT_CELL_COLOR);
                    } else { // Si es un obstáculo, asegúrate de que se mantenga negro
                        panel.setBackground(MazeView.WALL_COLOR);
                    }
                    panel.revalidate();
                    panel.repaint();
                }
            }
        }
    }

    private void onCellClicked(MazeCellPanel clickedCellPanel) {
        int row = clickedCellPanel.getRow();
        int col = clickedCellPanel.getCol();
        System.out.println("Celda clicada: [" + row + "][" + col + "] en modo: " + currentSelectionMode);

        // Detener animación y resetear colores del camino si hay una en curso
        // Esto lo hacemos ANTES de procesar el clic, para asegurar una "pizarra limpia"
        // si el usuario decide editar el laberinto.
        if (controller != null) {
            controller.stopAnimation();
            controller.resetPathColorsInView();
            // ¡IMPORTANTE! Al hacer clic en una celda para editar el laberinto,
            // siempre se considera el fin de cualquier secuencia de "Paso a Paso"
            isStepByStepActive = false; // Reinicia la bandera
        }

        // Usamos SwingUtilities.invokeLater para asegurar que todas las actualizaciones de UI
        // se realicen en el Event Dispatch Thread (EDT).
        SwingUtilities.invokeLater(() -> {
            switch (currentSelectionMode) {
                case START_CELL:
                    // Despintar la celda de inicio anterior (si existe y no es la misma que la nueva)
                    if (selectedStartCellPanel != null && selectedStartCellPanel != clickedCellPanel) {
                        // Restaurar a blanco si era camino, a negro si era pared
                        if (selectedStartCellPanel.isObstacle()) {
                            selectedStartCellPanel.setBackground(WALL_COLOR);
                        } else {
                            selectedStartCellPanel.setBackground(DEFAULT_CELL_COLOR);
                        }
                        selectedStartCellPanel.revalidate();
                        selectedStartCellPanel.repaint();
                    }

                    // Configurar la nueva celda de inicio
                    clickedCellPanel.setObstacle(false); // Una celda de inicio no puede ser un obstáculo
                    clickedCellPanel.setBackground(START_COLOR); // Pintar de verde
                    selectedStartCellPanel = clickedCellPanel;
                    selectedStartCell = new Cell(row, col);
                    if (controller != null) {
                        controller.setStartCell(selectedStartCell);
                    }
                    clickedCellPanel.revalidate();
                    clickedCellPanel.repaint();
                    break;

                case END_CELL:
                    // Despintar la celda de fin anterior (si existe y no es la misma que la nueva)
                    if (selectedEndCellPanel != null && selectedEndCellPanel != clickedCellPanel) {
                        // Restaurar a blanco si era camino, a negro si era pared
                        if (selectedEndCellPanel.isObstacle()) {
                            selectedEndCellPanel.setBackground(WALL_COLOR);
                        } else {
                            selectedEndCellPanel.setBackground(DEFAULT_CELL_COLOR);
                        }
                        selectedEndCellPanel.revalidate();
                        selectedEndCellPanel.repaint();
                    }

                    // Configurar la nueva celda de fin
                    clickedCellPanel.setObstacle(false); // Una celda de fin no puede ser un obstáculo
                    clickedCellPanel.setBackground(END_COLOR); // Pintar de rojo
                    selectedEndCellPanel = clickedCellPanel;
                    selectedEndCell = new Cell(row, col);
                    if (controller != null) {
                        controller.setEndCell(selectedEndCell);
                    }
                    clickedCellPanel.revalidate();
                    clickedCellPanel.repaint();
                    break;

                case OBSTACLE_CELL:
                    // Verificar si la celda es inicio o fin ANTES de intentar cambiarla
                    if (clickedCellPanel == selectedStartCellPanel) {
                        JOptionPane.showMessageDialog(MazeView.this,
                            "No puedes convertir la Celda de Inicio en un Obstáculo.",
                            "Advertencia", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if (clickedCellPanel == selectedEndCellPanel) {
                        JOptionPane.showMessageDialog(MazeView.this,
                            "No puedes convertir la Celda de Final en un Obstáculo.",
                            "Advertencia", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // Lógica para alternar el estado de obstáculo y el color
                    if (clickedCellPanel.isObstacle()) {
                        // Si ya era obstáculo, conviértela en camino (blanco)
                        clickedCellPanel.setBackground(DEFAULT_CELL_COLOR);
                        clickedCellPanel.setObstacle(false);
                        System.out.println("Celda [" + row + "][" + col + "] es ahora un CAMINO.");
                    } else {
                        // Si no era obstáculo, conviértela en pared (negro)
                        clickedCellPanel.setBackground(WALL_COLOR);
                        clickedCellPanel.setObstacle(true);
                        System.out.println("Celda [" + row + "][" + col + "] es ahora un OBSTÁCULO.");
                    }
                    clickedCellPanel.revalidate();
                    clickedCellPanel.repaint();
                    break;

                case NONE:
                default:
                    System.out.println("Clic en celda [" + row + "][" + col + "], pero no hay modo de selección activo.");
                    break;
            }
        });
    }

    /**
     * Método para que el controlador actualice el color de una celda específica.
     * Es crucial que esto se ejecute en el EDT.
     */
    public void updateCell(int row, int col, Color color) {
        if (row >= 0 && row < cellPanels.length && col >= 0 && col < cellPanels[0].length) {
            SwingUtilities.invokeLater(() -> {
                cellPanels[row][col].setBackground(color);
            });
        }
    }

    /**
     * Proporciona el estado actual de las celdas (muros/caminos) al controlador.
     * Retorna true para caminos, false para obstáculos.
     */
    public boolean[][] getCellPanelsState() {
        int rows = cellPanels.length;
        int cols = cellPanels[0].length;
        boolean[][] state = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                state[i][j] = !cellPanels[i][j].isObstacle(); // Si NO es obstáculo, es camino (true)
            }
        }
        return state;
    }

    /**
     * Devuelve la matriz de paneles de celdas del laberinto.
     * @return La matriz de MazeCellPanel.
     */
    public MazeCellPanel[][] getCellPanels() { // <-- Aquí el tipo es simplemente MazeCellPanel[][]
        return cellPanels;
    }

    // Getters para las celdas lógicas de inicio y fin (usados por el controlador)
    public Cell getSelectedStartCell() { return selectedStartCell; }
    public Cell getSelectedEndCell() { return selectedEndCell; }
}
