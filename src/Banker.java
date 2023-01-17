public class Banker {
    private int chips;
    private int score;

    public Banker() {
        this.chips = 1000;
    }

    public RollResult roll(Die[] dice) {
        int[] rolls = new int[3];
        for (int i = 0; i < dice.length; i++) {
            dice[i].roll();
            rolls[i] = dice[i].getRoll();
        }
        return RollIntepreter.determineResult(rolls);
    }
}
