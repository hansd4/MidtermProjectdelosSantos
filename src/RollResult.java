public class RollResult {
    private boolean win;
    private String condition;
    private int score;

    public RollResult(boolean win, String condition, int score) {
        this.win = win;
        this.condition = condition;
        this.score = score;
    }

    public RollResult(boolean win, String condition) {
        this.win = win;
        this.condition = condition;
        this.score = 0;
    }

    public boolean isWin() {
        return win;
    }

    public String getCondition() {
        return condition;
    }

    public int getScore() {
        return score;
    }
}
