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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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

/**
 * Interfaz gráfica principal para crear, visualizar y resolver laberintos.
 * Permite al usuario definir el laberinto, seleccionar puntos de inicio/fin,
 * elegir algoritmos de resolución y ver el proceso.
 */
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

    public static final Color DEFAULT_CELL_COLOR = Color.WHITE;
    public static final Color WALL_COLOR = Color.BLACK;
    public static final Color START_COLOR = Color.GREEN;
    public static final Color END_COLOR = Color.RED;
    public static final Color VISITED_COLOR = new Color(230, 230, 230);
    public static final Color PATH_COLOR = Color.BLUE;

    private MazeController controller;

    private boolean isStepByStepActive = false;

    /**
     * Constructor de MazeView. Inicializa la ventana y sus componentes.
     *
     * @param filas Número de filas del laberinto.
     * @param columnas Número de columnas del laberinto.
     */
    public MazeView(int filas, int columnas) {
        super("Maze Creator");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);

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

    /**
     * Establece el controlador de la vista para manejar las interacciones.
     *
     * @param controller El controlador del laberinto.
     */
    public void setController(MazeController controller) {
        this.controller = controller;
    }

    /** Configura la barra de menú con opciones de archivo y ayuda. */
    private void setupMenuBar() {
        menuBar = new JMenuBar();
        archivoMenu = new JMenu("Archivos");
        nuevoLaberintoItem = new JMenuItem("Nuevo Laberinto");
        verResultadosItem = new JMenuItem("Ver Resultados");
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

    /**
     * Configura los botones superiores para la selección de tipo de celda.
     *
     * @param panel El panel donde se añadirán los botones.
     */
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

    /**
     * Configura el panel principal del laberinto con las celdas visuales.
     *
     * @param filas Número de filas del laberinto.
     * @param columnas Número de columnas del laberinto.
     */
    private void setupMazePanel(int filas, int columnas) {
        mazePanel = new JPanel();
        mazePanel.setLayout(new GridLayout(filas, columnas));
        cellPanels = new MazeCellPanel[filas][columnas];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                MazeCellPanel cell = new MazeCellPanel(i, j);
                cell.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                cell.setPreferredSize(new Dimension(30, 30));
                cell.setBackground(DEFAULT_CELL_COLOR);

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
        mazePanel.revalidate();
        mazePanel.repaint();
    }

    /**
     * Configura los controles inferiores para la selección de algoritmo y botones de acción.
     *
     * @param panel El panel donde se añadirán los controles.
     */
    private void setupBottomControls(JPanel panel) {
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.add(new JLabel("Algoritmo: "));

        String[] algoritmos = {
            "Metodo Recursivo",
            "Metodo Recursivo Completo",
            "Metodo Recursivo Completo BT",
            "Metodo BFS",
            "Metodo DFS"
        };
        algoritmoComboBox = new JComboBox<>(algoritmos);
        algoritmoComboBox.setSelectedItem("Metodo Recursivo");
        panel.add(algoritmoComboBox);

        resolverButton = new JButton("Resolver Laberinto");
        pasoAPasoButton = new JButton("Paso a Paso");
        limpiarButton = new JButton("Limpiar Laberinto");

        panel.add(resolverButton);
        panel.add(pasoAPasoButton);
        panel.add(limpiarButton);

        resolverButton.addActionListener(e -> onResolverAction());
        pasoAPasoButton.addActionListener(e -> onPasoAPasoAction());
        limpiarButton.addActionListener(e -> onLimpiarAction());
    }

    /** Maneja la acción de crear un nuevo laberinto. */
    private void onNuevoLaberintoAction() {
        System.out.println("Acción: Nuevo Laberinto - Solicitando nuevas dimensiones.");
        if (controller != null) {
            controller.stopAnimation();
        }

        int[] newDims = CreadorMatriz.solicitarTamanioMatriz();

        if (newDims != null) {
            final int newFilas = newDims[0];
            final int newColumnas = newDims[1];
            dispose();

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

    /** Maneja la acción de ver resultados (funcionalidad no implementada). */
    private void onVerResultadosAction() {
        System.out.println("Acción: Ver Resultados (sin acción implementada).");
    }

    /** Muestra información "Acerca de" la aplicación y el desarrollador. */
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
                } catch (IOException | URISyntaxException ex) {
                    System.err.println("Error al abrir el enlace: " + ex.getMessage());
                    JOptionPane.showMessageDialog(null, """
                                                         No se pudo abrir el enlace. Copia y pega en tu navegador:
                                                         https://github.com/YandriSanchez/icc-est-u3-proyectoFinal""",
                                        "Error de Navegación", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JOptionPane.showMessageDialog(this,
                                     messageLabel,
                                     "Acerca de",
                                     JOptionPane.INFORMATION_MESSAGE);
    }

    /** Establece el modo de selección a celda de inicio. */
    private void onCeldaInicioAction() {
        System.out.println("Modo de selección: Celda de Inicio");
        currentSelectionMode = SelectionMode.START_CELL;
    }

    /** Establece el modo de selección a celda final. */
    private void onCeldaFinalAction() {
        System.out.println("Modo de selección: Celda de Final");
        currentSelectionMode = SelectionMode.END_CELL;
    }

    /** Establece el modo de selección a obstáculo o pared. */
    private void onObstaculoParedAction() {
        System.out.println("Modo de selección: Obstáculo o Pared");
        currentSelectionMode = SelectionMode.OBSTACLE_CELL;
    }

    /** Inicia la resolución del laberinto con el algoritmo seleccionado. */
    private void onResolverAction() {
        if (controller != null) {
            controller.resetPathColorsInView();
            controller.resetAnimationIndices();
            isStepByStepActive = false;

            String selectedAlgorithm = (String) algoritmoComboBox.getSelectedItem();
            controller.startSolvingMaze(selectedAlgorithm);
        } else {
            System.err.println("Error: Controlador no está configurado para la vista.");
        }
    }

    /**
     * Ejecuta el siguiente paso de la resolución del laberinto en modo paso a paso.
     * Si no está activo el modo, lo inicializa.
     */
    private void onPasoAPasoAction() {
        if (controller == null) {
            System.err.println("Error: Controlador no está configurado para la vista.");
            return;
        }
        String selectedAlgorithm = (String) algoritmoComboBox.getSelectedItem();

        if (!isStepByStepActive) {
            controller.resetPathColorsInView();
            controller.resetAnimationIndices();
            isStepByStepActive = true;
        } else {
            System.out.println("Clic consecutivo en 'Paso a Paso': continuando animación.");
        }
        controller.performSingleStep(selectedAlgorithm);
    }

    /** Restablece el laberinto a su estado original (muros y caminos por defecto). */
    private void onLimpiarAction() {
        if (controller != null) {
            controller.resetPathColorsInView();
            controller.resetAnimationIndices();
            isStepByStepActive = false;
        } else {
            System.err.println("Error: Controlador no está configurado para la vista.");
        }
    }

    /**
     * Restablece visualmente las celdas de inicio y fin seleccionadas,
     * y el resto de celdas a su estado de camino o muro.
     */
    public void resetSelectedCells() {
        if (selectedStartCellPanel != null) {
            if (selectedStartCellPanel.isObstacle()) {
                selectedStartCellPanel.setBackground(WALL_COLOR);
            } else {
                selectedStartCellPanel.setBackground(DEFAULT_CELL_COLOR);
            }
            selectedStartCellPanel.revalidate();
            selectedStartCellPanel.repaint();
            selectedStartCellPanel = null;
            selectedStartCell = null;
        }
        if (selectedEndCellPanel != null) {
            if (selectedEndCellPanel.isObstacle()) {
                selectedEndCellPanel.setBackground(WALL_COLOR);
            } else {
                selectedEndCellPanel.setBackground(DEFAULT_CELL_COLOR);
            }
            selectedEndCellPanel.revalidate();
            selectedEndCellPanel.repaint();
            selectedEndCellPanel = null;
            selectedEndCell = null;
        }

        MazeCellPanel[][] panels = getCellPanels();
        if (panels != null) {
            for (MazeCellPanel[] panel1 : panels) {
                for (int j = 0; j < panels[0].length; j++) {
                    MazeCellPanel panel = panel1[j];
                    if (!panel.isObstacle()) {
                        panel.setBackground(MazeView.DEFAULT_CELL_COLOR);
                    } else {
                        panel.setBackground(MazeView.WALL_COLOR);
                    }
                    panel.revalidate();
                    panel.repaint();
                }
            }
        }
    }

    /**
     * Maneja el evento de clic en una celda del laberinto.
     * Actualiza la celda según el modo de selección actual.
     *
     * @param clickedCellPanel El panel de la celda clicada.
     */
    private void onCellClicked(MazeCellPanel clickedCellPanel) {
        int row = clickedCellPanel.getRow();
        int col = clickedCellPanel.getCol();
        System.out.println("Celda clicada: [" + row + "][" + col + "] en modo: " + currentSelectionMode);

        if (controller != null) {
            controller.stopAnimation();
            controller.resetPathColorsInView();
            isStepByStepActive = false;
        }

        SwingUtilities.invokeLater(() -> {
            switch (currentSelectionMode) {
                case START_CELL:
                    if (selectedStartCellPanel != null && selectedStartCellPanel != clickedCellPanel) {
                        if (selectedStartCellPanel.isObstacle()) {
                            selectedStartCellPanel.setBackground(WALL_COLOR);
                        } else {
                            selectedStartCellPanel.setBackground(DEFAULT_CELL_COLOR);
                        }
                        selectedStartCellPanel.revalidate();
                        selectedStartCellPanel.repaint();
                    }

                    clickedCellPanel.setObstacle(false);
                    clickedCellPanel.setBackground(START_COLOR);
                    selectedStartCellPanel = clickedCellPanel;
                    selectedStartCell = new Cell(row, col);
                    if (controller != null) {
                        controller.setStartCell(selectedStartCell);
                    }
                    clickedCellPanel.revalidate();
                    clickedCellPanel.repaint();
                    break;

                case END_CELL:
                    if (selectedEndCellPanel != null && selectedEndCellPanel != clickedCellPanel) {
                        if (selectedEndCellPanel.isObstacle()) {
                            selectedEndCellPanel.setBackground(WALL_COLOR);
                        } else {
                            selectedEndCellPanel.setBackground(DEFAULT_CELL_COLOR);
                        }
                        selectedEndCellPanel.revalidate();
                        selectedEndCellPanel.repaint();
                    }

                    clickedCellPanel.setObstacle(false);
                    clickedCellPanel.setBackground(END_COLOR);
                    selectedEndCellPanel = clickedCellPanel;
                    selectedEndCell = new Cell(row, col);
                    if (controller != null) {
                        controller.setEndCell(selectedEndCell);
                    }
                    clickedCellPanel.revalidate();
                    clickedCellPanel.repaint();
                    break;

                case OBSTACLE_CELL:
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

                    if (clickedCellPanel.isObstacle()) {
                        clickedCellPanel.setBackground(DEFAULT_CELL_COLOR);
                        clickedCellPanel.setObstacle(false);
                        System.out.println("Celda [" + row + "][" + col + "] es ahora un CAMINO.");
                    } else {
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
     * Actualiza el color de una celda en la interfaz gráfica.
     * Debe ejecutarse en el Event Dispatch Thread (EDT).
     *
     * @param row La fila de la celda.
     * @param col La columna de la celda.
     * @param color El nuevo color para la celda.
     */
    public void updateCell(int row, int col, Color color) {
        if (row >= 0 && row < cellPanels.length && col >= 0 && col < cellPanels[0].length) {
            SwingUtilities.invokeLater(() -> {
                cellPanels[row][col].setBackground(color);
            });
        }
    }

    /**
     * Obtiene el estado actual de las celdas del laberinto (camino/muro).
     *
     * @return Una matriz booleana donde true es camino y false es obstáculo.
     */
    public boolean[][] getCellPanelsState() {
        int rows = cellPanels.length;
        int cols = cellPanels[0].length;
        boolean[][] state = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                state[i][j] = !cellPanels[i][j].isObstacle();
            }
        }
        return state;
    }

    /**
     * Devuelve la matriz de paneles de celdas del laberinto.
     *
     * @return La matriz de MazeCellPanel.
     */
    public MazeCellPanel[][] getCellPanels() {
        return cellPanels;
    }

    /**
     * Obtiene la celda de inicio seleccionada.
     * @return La celda de inicio lógica.
     */
    public Cell getSelectedStartCell() { return selectedStartCell; }

    /**
     * Obtiene la celda final seleccionada.
     * @return La celda final lógica.
     */
    public Cell getSelectedEndCell() { return selectedEndCell; }
}
