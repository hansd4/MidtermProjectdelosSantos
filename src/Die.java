public class Die {
    private int roll;

    public Die() {}

    public int getRoll() {
        return roll;
    }

    public void roll() {
        roll = (int) (Math.random() * 6) + 1;
    }

    public String toString() {
        return Integer.toString(roll);
    }
}
