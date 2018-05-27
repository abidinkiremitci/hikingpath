package github.beardthered.model;

/*
 *---------------------------------------------------------------
 * The bottom-right cell/state has state index = 1
 * The top-left end cell/state has state index = nValue^2 = numStates
 *
 *                                      y:
 *    |----|----|----|----|-----------|
 *    | n*n|....|....|....|(n*(n-1))+1| n-1
 *    |----|----|----|----|-----------|
 *    |....|....|....|....|...........| ...
 *    |----|----|----|----|-----------|
 *    | 3n |....|....|....|   2n+1    |  2
 *    |----|----|----|----|-----------|
 *    | 2n |2n-1|....| n+2|    n+1    |  1
 *    |----|----|----|----|-----------|
 *    | 1n | n-1|....|  2 |      1    |  0
 *    |----|----|----|----|-----------|
 *  x: n-1  n-2  ...   1        0
 *
 *
 * The state-index matrix has:
 *   - Row index = state number
 *   - Column index = action number
 *---------------------------------------------------------------
 */

public class QMatrix {
    private int nValue;
    private int numStates;
    private double[][] actionValueMatrix;
    private double[][] immediateRewards;

    public QMatrix(int nValue) {
        this.nValue = nValue;
        this.numStates = (int) Math.pow(nValue, 2);
        this.actionValueMatrix = new double[numStates][Action.NUM_ACTIONS];
        this.immediateRewards = new double[numStates][Action.NUM_ACTIONS];
        setForbiddenDirectionRewards(nValue);
    }

    private void setForbiddenDirectionRewards(int nValue) {
        for (int i = 1; i <= nValue; i++) {
            // Going down on bottommost cells
            setImmediateReward(i, Action.DOWN, -Double.MAX_VALUE);
            setQValue(i, Action.DOWN, -Double.MAX_VALUE);
            // Going left on leftmost cells
            setImmediateReward(i * nValue, Action.LEFT, -Double.MAX_VALUE);
            setQValue(i * nValue, Action.LEFT, -Double.MAX_VALUE);
            // Going right on rightmost cells
            setImmediateReward(((i - 1) * nValue) + 1, Action.RIGHT, -Double.MAX_VALUE);
            setQValue(((i - 1) * nValue) + 1, Action.RIGHT, -Double.MAX_VALUE);
            // Going up on upmost cells
            setImmediateReward((nValue * (nValue - 1)) + i, Action.UP, -Double.MAX_VALUE);
            setQValue((nValue * (nValue - 1)) + i, Action.UP, -Double.MAX_VALUE);
        }
    }

    public double getQValue(int state, Action action) {
        return actionValueMatrix[state - 1][action.index];
    }

    public void setQValue(int state, Action action, double reward) {
        // -1 to convert from state index to array index.
        actionValueMatrix[state - 1][action.index] = reward;
    }

    public double getImmediateReward(int state, Action action) {
        return immediateRewards[state - 1][action.index];
    }

    public void setImmediateReward(int xCoor, int yCoor, Action action, double reward) {
        // Check if the cell is in bounds
        if ((xCoor >= 0 && xCoor < nValue) && (yCoor >= 0 && yCoor < nValue) ) {
            setImmediateReward(Coordinate.toStateIndex(nValue, xCoor, yCoor), action, reward);
        }
    }

    public void setImmediateReward(Coordinate coordinate, Action action, double reward) {
        setImmediateReward(coordinate.getX(), coordinate.getY(), action, reward);
    }

    public void setImmediateReward(int state, Action action, double reward) {
        // -1 to convert from state index to array index.
        immediateRewards[state - 1][action.index] = reward;
    }
}
