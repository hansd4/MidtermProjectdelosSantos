public class RollIntepreter {
    private RollIntepreter() {}

    public static String determineResult(int[] rolls) {

    }

    private static boolean triple(int[] rolls) {
        return rolls[0] == rolls[1] && rolls[1] == rolls[2];
    }

    private static boolean fourFiveSix(int[] rolls) {
        boolean four = false;
        boolean five = false;
        boolean six = false;
        for (int roll : rolls) {
            if (!four && roll == 4) {
                four = true;
            } else if (!five && roll == 5) {
                five = true;
            } else if (!six && roll == 6) {
                six = true;
            }
        }
        return four && five && six;
    }
}
