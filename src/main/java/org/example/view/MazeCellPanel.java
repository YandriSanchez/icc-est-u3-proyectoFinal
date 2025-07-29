package org.example.view;

import javax.swing.JPanel;

public class MazeCellPanel extends JPanel {
    private final int row;
    private final int col;
    private boolean isObstacle;

    public MazeCellPanel(int row, int col) {
        this.row = row;
        this.col = col;
        this.isObstacle = false;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isObstacle() {
        return isObstacle;
    }

    public void setObstacle(boolean obstacle) {
        isObstacle = obstacle;
    }
}


