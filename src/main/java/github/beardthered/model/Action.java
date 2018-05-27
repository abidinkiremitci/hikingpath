package github.beardthered.model;

public enum Action {
    RIGHT(0, "R: "),
    LEFT(1, "L: "),
    UP(2, "U: "),
    DOWN(3, "D: ");

    public final int index;
    public final String text;
    public static final int NUM_ACTIONS = 4;

    private Action(int index, String text) {
        this.index = index;
        this.text = text;
    }

    public static Action getAction(int index) {
        switch (index) {
            case 0:
                return RIGHT;
            case 1:
                return LEFT;
            case 2:
                return UP;
            case 3:
                return DOWN;
            default:
                return UP;
        }
    }
}
