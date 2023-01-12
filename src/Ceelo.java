import java.util.Scanner;

public class Ceelo {
    private Player[] players;
    private Banker banker;
    private Scanner scan;
    private Die[] dice;

    private boolean gameRunning;

    public Ceelo() {
        this.players = new Player[3];
        this.banker = new Banker();
        this.scan = new Scanner(System.in);
        this.dice = new Die[] {new Die(), new Die(), new Die()};

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
            System.out.println();
            banker.roll(dice);
            System.out.println("The banker rolls a " + dice[0] + ", a " + dice[1] + ", and a " + dice[2] + ".");

        }
    }
}
