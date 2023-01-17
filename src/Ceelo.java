import java.util.Scanner;

public class Ceelo {
    private Player[] players;
    private Banker banker;
    private Scanner scan;
    private Die[] dice;
    private RollResult[] rollResults;

    private boolean gameRunning;

    public Ceelo() {
        this.players = new Player[3];
        this.banker = new Banker();
        this.scan = new Scanner(System.in);
        this.dice = new Die[] {new Die(), new Die(), new Die()};
        this.rollResults = new RollResult[4];
        for (int i = 0; i < rollResults.length; i++) {
            rollResults[i] = new RollResult(false, false, "unmatched");
        }

        this.gameRunning = true;
    }

    public void play() {
        System.out.println("Welcome to the Midterm Project");

        for (int i = 0; i < 3; i++) {
            System.out.print("Player " + (i + 1) + ", enter your name: ");
            players[i] = new Player(scan.nextLine());
            System.out.println("Welcome to the game, " + players[i].getName());
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("             CEELO            ");

        while (gameRunning) {
            System.out.println();
            for (Player player : players) {
                System.out.print(player.getName() + ", enter your wager: ");
                player.setWager(scan.nextInt());
                scan.nextLine();
            }
            boolean unmatched = true;
            while (unmatched) {
                System.out.println();
                rollResults[0] = banker.roll(dice);
                System.out.println("The banker rolls a " + dice[0] + ", a " + dice[1] + ", and a " + dice[2] + "!");
                unmatched = rollResults[0].getCondition().equals("unmatched");
                if (unmatched) {
                    System.out.println("3 unmatched dice, re-rolling...");
                }
            }
            if (rollResults[0].isWin()) {
                if (rollResults[0].getCondition().equals("triple")) {
                    System.out.println("3 matching dice! The banker wins this round.");
                } else {
                    System.out.println("A 4, a 5, and a 6! The banker wins this round.");
                }
            }
        }
    }
}
