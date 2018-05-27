package github.beardthered.model;

import lombok.Getter;

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
    @Getter
    private final int start;
    @Getter
    private final int goal;

    public Maze(int nValue, double rValue, QMatrix qMatrix, int start, int goal) {
        this.nValue = nValue;
        this.qMatrix = qMatrix;
        this.numHills = (int) Math.ceil(Math.pow(nValue, 2) * rValue);
        this.maze = new TerrainType[nValue][nValue];
        this.start = start;
        this.goal = goal;
        initMaze();
    }

    private void initMaze() {
        placeGroundTiles();
        placeRandomHills();
        setSurroundingRewards();
    }


    private void placeRandomHills() {
        Random randomGen = new Random();
        int xCoor;
        int yCoor;

        // Place random hills
        while (countOfHills() < numHills) {
            xCoor = randomGen.nextInt(nValue);
            yCoor = randomGen.nextInt(nValue);
            setTerrainType(xCoor, yCoor, TerrainType.HILL);
        }
    }

    private int countOfHills() {
        int hillCnt = 0;
        // Fill the rest of the maze with ground tiles
        for (int xCoor = 0; xCoor < nValue; xCoor++) {
            for (int yCoor = 0; yCoor < nValue; yCoor++) {
                if(TerrainType.HILL.equals(getTerrainType(xCoor,yCoor))) {
                    hillCnt += 1;
                }
            }
        }
        return hillCnt;
    }

    private void placeGroundTiles() {
        // Fill the rest of the maze with ground tiles
        for (int xCoor = 0; xCoor < nValue; xCoor++) {
            for (int yCoor = 0; yCoor < nValue; yCoor++) {
                setTerrainType(xCoor, yCoor, TerrainType.GROUND);
            }
        }
    }

    public TerrainType getTerrainType(int state) {
        return getTerrainType(Coordinate.toX(nValue, state), Coordinate.toY(nValue, state));
    }

    public TerrainType getTerrainType(int xCoor, int yCoor) {
        return maze[nValue - xCoor - 1][nValue - yCoor - 1];
    }

    public TerrainType getTerrainType(Coordinate coordinate) {
        if ((coordinate.getX() >= 0 && coordinate.getX() < nValue) && (coordinate.getY() >= 0 && coordinate.getY() < nValue) ) {
            return maze[nValue - coordinate.getX() - 1][nValue - coordinate.getY() - 1];
        }
        return null;
    }

    public void setTerrainType(int xCoor, int yCoor, TerrainType type) {
        maze[nValue - xCoor - 1][nValue - yCoor - 1] = type;
    }

    private void setSurroundingRewards(){
        for (int xCoor = 0; xCoor < nValue; xCoor++) {
            for (int yCoor = 0; yCoor < nValue; yCoor++) {
                setSurroundingRewards(new Coordinate(xCoor, yCoor));
            }
        }
    }
    private void setSurroundingRewards(Coordinate coordinate) {

        if(!(coordinate.getX() == goalCoordinate().getX() && coordinate.getY() == goalCoordinate().getY())) {
            TerrainType currentType = getTerrainType(coordinate);

            // Arriving to this tile from above
            TerrainType nextTypeToDown = getTerrainType(coordinate.goDown());
            qMatrix.setImmediateReward(coordinate, Action.DOWN, currentType.calculateTransitionPenalty(nextTypeToDown));
            // Arriving to this tile from below
            TerrainType nextTypeToUp = getTerrainType(coordinate.goUp());
            qMatrix.setImmediateReward(coordinate, Action.UP, currentType.calculateTransitionPenalty(nextTypeToUp));
            // Arriving to this tile from left
            TerrainType nextTypeToLeft = getTerrainType(coordinate.goLeft());
            qMatrix.setImmediateReward(coordinate, Action.LEFT, currentType.calculateTransitionPenalty(nextTypeToLeft));
            // Arriving to this tile from right
            TerrainType nextTypeToRight = getTerrainType(coordinate.goRight());
            qMatrix.setImmediateReward(coordinate, Action.RIGHT, currentType.calculateTransitionPenalty(nextTypeToRight));
        } else {
            // Arriving to this tile from above
            qMatrix.setImmediateReward(coordinate, Action.DOWN, 10);
            // Arriving to this tile from below
            qMatrix.setImmediateReward(coordinate, Action.UP, 10);
            // Arriving to this tile from left
            qMatrix.setImmediateReward(coordinate, Action.RIGHT, 10);
            // Arriving to this tile from right
            qMatrix.setImmediateReward(coordinate, Action.LEFT, 10);
        }
    }

    private Coordinate goalCoordinate() {
        return Coordinate.fromState(nValue,goal);
    }
}
