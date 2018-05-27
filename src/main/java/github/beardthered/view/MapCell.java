package github.beardthered.view;


import github.beardthered.model.Action;
import github.beardthered.model.Maze;
import github.beardthered.model.QMatrix;
import github.beardthered.model.TerrainType;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.DecimalFormat;

public class MapCell extends JPanel {
    public static final int CELL_WIDTH = 90;
    public static final int CELL_HEIGHT = 90;
    private static final int BORDER_THICKNESS = 1;
    private static final int NUM_ROWS = 5;
    private static final int NUM_COLS = 1;

    private final QMatrix qMatrix;

    private final int state;
    private JLabel stateLabel;
    private JLabel rightRewardLabel;
    private JLabel leftRewardLabel;
    private JLabel upRewardLabel;
    private JLabel downRewardLabel;

    private JLabel currentMaxRewardLabel;
    private double currentMaxReward = -Integer.MAX_VALUE;

    private DecimalFormat numFormat;

    public MapCell(int state, Maze maze, QMatrix qMatrix) {
        this.qMatrix = qMatrix;
        this.state = state;
        this.numFormat = new DecimalFormat("#.####");
        initCell(maze.getTerrainType(state));
        initLabels();
    }

    private void initCell(TerrainType type) {
        setLayout(new GridLayout(NUM_ROWS, NUM_COLS));
        setBorder(new LineBorder(Color.BLACK, BORDER_THICKNESS));
        initBackgroundColor(type);
    }

    private void initBackgroundColor(TerrainType type) {
        if (type == TerrainType.GROUND) {
            setBackground(Color.WHITE);
        } else {
            setBackground(new Color(204, 120, 50));
        }
    }

    private void initLabels() {
        stateLabel = new JLabel("State #" + state);
        stateLabel.setForeground(Color.BLUE);
        this.add(stateLabel);
        rightRewardLabel = new JLabel("R: ");
        this.add(rightRewardLabel);
        leftRewardLabel = new JLabel("L: ");
        this.add(leftRewardLabel);
        upRewardLabel = new JLabel("U: ");
        this.add(upRewardLabel);
        downRewardLabel = new JLabel("D: ");
        this.add(downRewardLabel);
    }

    public void update(int currStateOfAgent) {
        resetColors();
        updateLabel(rightRewardLabel, Action.RIGHT);
        updateLabel(leftRewardLabel, Action.LEFT);
        updateLabel(upRewardLabel, Action.UP);
        updateLabel(downRewardLabel, Action.DOWN);
        setMinChoiceColor();

        if (currStateOfAgent == state) {
            // Agent is in this state/cell
            setBorder(new LineBorder(Color.RED, BORDER_THICKNESS * 3));
        } else {
            // Agent is in another state/cell
            setBorder(new LineBorder(Color.BLACK, BORDER_THICKNESS));
        }
    }

    private void resetColors() {
        rightRewardLabel.setForeground(Color.BLACK);
        leftRewardLabel.setForeground(Color.BLACK);
        upRewardLabel.setForeground(Color.BLACK);
        downRewardLabel.setForeground(Color.BLACK);
    }

    public void updateLabel(JLabel label, Action action) {
        double qValue = qMatrix.getQValue(state, action);
        if (qValue == -Double.MAX_VALUE) {
            label.setText(action.text + "-Inf");
        } else {
            label.setText(action.text + numFormat.format(qValue));
        }
        if(qValue > currentMaxReward) {
            currentMaxReward = qValue;
            currentMaxRewardLabel = label;
        }
    }

    private void setMinChoiceColor() {
        currentMaxRewardLabel.setForeground(Color.RED);
        currentMaxReward = -Integer.MAX_VALUE;
    }
}
