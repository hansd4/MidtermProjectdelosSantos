public class RollIntepreter {
    private RollIntepreter() {}

    public static RollResult determineResult(int[] rolls) {
        int[] rollFrequencies = rollFrequencies(rolls);
        int matchedRolls = matchingRolls(rollFrequencies);
        if (matchedRolls == 3) {
            return new RollResult(true, false, "triple");
        } else if (matchingNumbers(rolls, new int[] {4, 5, 6})) {
            return new RollResult(true, false, "456");
        } else if (matchingNumbers(rolls, new int[] {1, 2, 3})) {
            return new RollResult(false, true, "123");
        } else if (matchedRolls == 2) {
            return new RollResult(false, false, "double", scoreFromDouble(rollFrequencies));
        } else {
            return new RollResult(false, false, "unmatched");
        }
    }

    private static int matchingRolls(int[] rollFrequencies) {
        int maxFrequency = 0;
        for (int frequency : rollFrequencies) {
            if (frequency > maxFrequency) {
                maxFrequency = frequency;
            }
        }
        return maxFrequency;
    }

    private static boolean matchingNumbers(int[] rolls, int[] nums) {
        int matches = 0;
        int[] tempRolls = new int[3];
        int[] tempNums = new int[3];
        for (int i = 0; i < tempRolls.length; i++) {
            tempRolls[i] = rolls[i];
            tempNums[i] = nums[i];
        }
        for (int i = 0; i < tempRolls.length; i++) {
            for (int j = 0; j < tempNums.length; j++) {
                if (tempRolls[i] == tempNums[j]) {
                    matches++;
                    tempNums[j] = -1;
                }
            }
        }
        return matches == 3;
    }

    private static int scoreFromDouble(int[] rollFrequencies) {
        for (int i = 1; i < rollFrequencies.length; i++) {
            if (rollFrequencies[i] == 1) {
                return i;
            }
        }
        return -1;
    }

    private static int[] rollFrequencies(int[] rolls) {
        int[] rollFrequencies = new int[7];
        for (int roll : rolls) {
            rollFrequencies[roll]++;
        }
        return rollFrequencies;
    }
}
