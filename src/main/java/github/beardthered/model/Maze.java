package github.beardthered.model;

import java.util.Random;

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
 *---------------------------------------------------------------
 */

public class Maze {
    private TerrainType[][] maze;

    private final int nValue;
    private final QMatrix qMatrix;
    private final int numHills;

    public Maze(int nValue, double rValue, QMatrix qMatrix) {
        this.nValue = nValue;
        this.qMatrix = qMatrix;
        this.numHills = (int) Math.ceil(Math.pow(nValue, 2) * rValue);
        this.maze = new TerrainType[nValue][nValue];
        initMaze();
    }

    private void initMaze() {
        placeRandomHills();
        placeGroundTiles();
    }


    private void placeRandomHills() {
        Random randomGen = new Random();
        int xCoor;
        int yCoor;

        // Place random hills
        for (int i = 0; i < numHills; i++) {
            // Find a tile that isn't a hill
            do {
                xCoor = randomGen.nextInt(nValue);
                yCoor = randomGen.nextInt(nValue);
            } while (getTerrainType(xCoor, yCoor) == TerrainType.HILL);
            setTerrainTypeAndRewards(xCoor, yCoor, TerrainType.HILL);
        }
    }
    private void placeGroundTiles() {
        // Fill the rest of the maze with ground tiles
        for (int xCoor = 0; xCoor < nValue; xCoor++) {
            for (int yCoor = 0; yCoor < nValue; yCoor++) {
                if (getTerrainType(xCoor, yCoor) != TerrainType.HILL) {
                    setTerrainTypeAndRewards(xCoor, yCoor, TerrainType.GROUND);
                }
            }
        }
    }

    public TerrainType getTerrainType(int state) {
        return getTerrainType(CoorStateConverter.toXCoor(nValue, state), CoorStateConverter.toYCoor(nValue, state));
    }

    public TerrainType getTerrainType(int xCoor, int yCoor) {
        return maze[nValue - xCoor - 1][nValue - yCoor - 1];
    }

    public void setTerrainTypeAndRewards(int xCoor, int yCoor, TerrainType type) {
        maze[nValue - xCoor - 1][nValue - yCoor - 1] = type;
        setSurroundingRewards(xCoor, yCoor, type);
    }

    private void setSurroundingRewards(int xCoor, int yCoor, TerrainType type) {
        // Arriving to this tile from above
        qMatrix.setImmediateReward(xCoor, yCoor + 1, Action.DOWN, type.reward);
        // Arriving to this tile from below
        qMatrix.setImmediateReward(xCoor, yCoor - 1, Action.UP, type.reward);
        // Arriving to this tile from left
        qMatrix.setImmediateReward(xCoor + 1, yCoor, Action.RIGHT, type.reward);
        // Arriving to this tile from right
        qMatrix.setImmediateReward(xCoor - 1, yCoor, Action.LEFT, type.reward);
    }
}
