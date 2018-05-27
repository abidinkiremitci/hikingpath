package github.beardthered.model;

public enum TerrainType {
    GROUND,
    HILL;

    public double calculateTransitionPenalty(TerrainType nextTerrain) {
        if (nextTerrain !=null) {
            if (GROUND.equals(this) && GROUND.equals(nextTerrain)) {
                return -1;
            } else if (GROUND.equals(this) && HILL.equals(nextTerrain)) {
                return -3;
            } else if (HILL.equals(this) && GROUND.equals(nextTerrain)) {
                return -1;
            } else {
                return -2;
            }
        }
        return -Double.MAX_VALUE;
    }
}
