package github.beardthered.view;


import github.beardthered.model.Maze;
import github.beardthered.model.QMatrix;

import javax.swing.*;
import java.awt.*;

public class MapWindow extends JFrame {
    private int nValue;
    private JPanel mainPanel;
    private MapCell[][] cells;

    public MapWindow(int nValue, Maze maze, QMatrix qMatrix) throws HeadlessException {
        this.nValue = nValue;
        setLayout(new BorderLayout());
        initMainPanel();
        initMapCells(maze, qMatrix);
        initWindow();
    }

    private void initMainPanel() {
        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(new GridLayout(nValue, nValue));
        add(mainPanel, BorderLayout.NORTH);
    }

    private void initMapCells(Maze maze, QMatrix qMatrix) {
        cells = new MapCell[nValue][nValue];
        int state = (int) Math.pow(nValue, 2);
        for (int row = 0; row < nValue; row++) {
            for (int col = 0; col < nValue; col++) {
                MapCell cell = new MapCell(state, maze, qMatrix);
                cells[row][col] = cell;
                mainPanel.add(cell);
                state--;
            }
        }
    }

    private void initWindow() {
        update(1);
        setTitle("Q Learning For Optimal Hiking Path");
        setMinimumSize(new Dimension(nValue * MapCell.CELL_WIDTH, nValue * MapCell.CELL_HEIGHT));
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void update(int currStateOfAgent) {
        for (int row = 0; row < nValue; row++) {
            for (int col = 0; col < nValue; col++) {
                cells[row][col].update(currStateOfAgent);
            }
        }
    }

}
