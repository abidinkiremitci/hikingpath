package github.beardthered.model;

public enum TerrainType {
    GROUND(-1),
    HILL(-4);

    public final int reward;

    private TerrainType(int reward) {
        this.reward = reward;
    }
}
