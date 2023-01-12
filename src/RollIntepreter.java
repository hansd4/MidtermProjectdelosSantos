public class RollIntepreter {
    private RollIntepreter() {}

    public static RollResult determineResult(int[] rolls) {
        int matchedRolls = matchingRolls(rolls);
        if (matchedRolls == 3) {
            return new RollResult(true, "triple");
        } else if (matchingNumbers(rolls, new int[] {4, 5, 6})) {
            return new RollResult(true, "456")
        } else if (matchingNumbers(rolls, new int[] {1, 2, 3}) {
            return new RollResult(false, "123")
        } else if (matchedRolls == 2) {
            return new RollResult(false, "double", )
        }
    }

    private static int matchingRolls(int[] rolls) {
        int[] rollFrequencies = new int[7];
        int maxFrequency = 0;
        for (int roll : rolls) {
            rollFrequencies[roll]++;
        }
        for (int frequency : rollFrequencies) {
            if (frequency > maxFrequency) {
                maxFrequency = frequency;
            }
        }
        return maxFrequency;
    }

    private static boolean matchingNumbers(int[] rolls, int[] nums) {
        int matches = 0;
        for (int roll : rolls) {
            for (int num : nums) {
                if (roll == num) {
                    matches++;
                }
            }
        }
        return matches == 3;
    }

    private static int scoreFromDouble(int[] rolls) {
        for (int roll : rolls) {

        }
    }
}
