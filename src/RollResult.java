public class RollResult {
    private boolean win;
    private boolean lose;
    private String condition;
    private int score;

    public RollResult(boolean win, boolean lose, String condition, int score) {
        this.win = win;
        this.lose = lose;
        this.condition = condition;
        this.score = score;
    }

    public RollResult(boolean win, boolean lose, String condition) {
        this.win = win;
        this.lose = lose;
        this.condition = condition;
        this.score = -1;
    }

    public boolean isWin() {
        return win;
    }

    public boolean isLose() {
        return lose;
    }

    public String getCondition() {
        return condition;
    }

    public int getScore() {
        return score;
    }

    public String toString() {
        return "win? " + win +
                "\nlose? " + lose +
                "\ncondition? " + condition +
                "\nscore? " + score;
    }
}
