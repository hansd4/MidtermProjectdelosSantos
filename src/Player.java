public class Player {
    private String name;
    private int chips;
    private int score;
    private int wager;

    public Player(String name) {
        this.name = name;
        this.chips = 100;
        this.score = 0;
        this.wager = 0;
    }

    public String getName() {
        return name;
    }

    public int getWager() {
        return wager;
    }

    public int getChips() {
        return chips;
    }

    public void setWager(int newWager) {
        wager = newWager;
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

    public boolean inGame() {
        return chips > 0;
    }
}
