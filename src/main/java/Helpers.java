public class Helpers {
    public static double generateNumber(double min, double max) {
        return Math.floor(Math.random() * (max-min) + min);
    }
}
