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

    public void setChips(int newChips) {
        chips = newChips;
    }

    public void setWager(int newWager) {
        wager = newWager;
    }

    public void roll(Die[] dice) {
        for (Die die : dice) {
            die.roll();
        }
    }

    public boolean inGame() {
        return chips > 0;
    }
}
