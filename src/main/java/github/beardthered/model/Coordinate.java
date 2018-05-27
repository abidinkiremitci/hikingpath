package github.beardthered.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Coordinate {
    int x;
    int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate goUp() {
        return new Coordinate(this.x, this.y+1);
    }

    public Coordinate goDown() {
        return new Coordinate(this.x, this.y-1);
    }

    public Coordinate goLeft() {
        return new Coordinate(this.x+1, this.y);
    }

    public Coordinate goRight() {
        return new Coordinate(this.x-1, this.y);
    }
    public static int toStateIndex(int nValue, int x, int y) {
        return x + (y * nValue) + 1;
    }

    public static int toX(int nValue, int state) {
        if (state % nValue == 0) {
            return nValue - 1;
        } else {
            return (state % nValue) - 1;
        }
    }

    public static int toY(int nValue, int state) {
        if (state % nValue == 0) {
            return (state / nValue) - 1;
        } else {
            return (state / nValue);
        }
    }

    public static Coordinate fromState(int nValue, int state) {
        return new Coordinate(toX(nValue, state), toY(nValue, state));
    }
}
