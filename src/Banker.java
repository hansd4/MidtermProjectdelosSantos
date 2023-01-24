public class Banker {
    private int chips;
    private int score;

    public Banker() {
        this.chips = 1000;
    }

    public int getChips() {
        return chips;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int newScore) {
        score = newScore;
    }

    public void setChips(int newChips) {
        chips = newChips;
    }

    public void addChips(int num) {
        chips += num;
    }

    public void subtractChips(int num) {
        chips -= num;
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
