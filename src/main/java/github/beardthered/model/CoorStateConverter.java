package github.beardthered.model;

public class CoorStateConverter {

    public static int toStateIndex(int nValue, int xCoor, int yCoor) {
        return xCoor + (yCoor * nValue) + 1;
    }

    public static int toXCoor(int nValue, int state) {
        if (state % nValue == 0) {
            return nValue - 1;
        } else {
            return (state % nValue) - 1;
        }
    }

    public static int toYCoor(int nValue, int state) {
        if (state % nValue == 0) {
            return (state / nValue) - 1;
        } else {
            return (state / nValue);
        }
    }

}
