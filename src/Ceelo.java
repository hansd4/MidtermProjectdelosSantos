import java.util.Scanner;

public class Ceelo {
    private Player[] players;
    private Banker banker;
    private Scanner scan;
    private Die[] dice;
    private RollResult[] rollResults;

    private boolean gameRunning;
    private boolean unmatched;
    private boolean invalidWager;

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
        this.unmatched = true;
        this.invalidWager = true;
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
                invalidWager = true;
                while (invalidWager) { // !!! check for valid wager (less than what player has)
                    System.out.print(player.getName() + ", enter your wager: ");
                    player.setWager(scan.nextInt());
                    scan.nextLine();
                }
            }
            unmatched = true;
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
                    System.out.println("3 matching dice! The banker wins this round, all wagers go to them.");
                } else {
                    System.out.println("A 4, a 5, and a 6! The banker wins this round, all wagers go to them.");
                }

                for (Player player : players) {
                    if (player.inGame()) {
                        banker.addChips(player.getWager());
                        player.subtractChips(player.getWager());
                    }
                }

                printGameStatus();
            } else if (rollResults[0].isLose()) {
                System.out.println("A 1, a 2, and a 3! The banker loses this round, all wagers doubled!");

                for (Player player : players) {
                    if (player.inGame()) {
                        banker.subtractChips(player.getWager());
                        player.addChips(player.getWager());
                    }
                }

                printGameStatus();
            } else {
                banker.setScore(rollResults[0].getScore());
                System.out.println("The banker rolls a score of " + banker.getScore());
                int currPlayer = 1;
                for (Player player : players) {
                    if (player.inGame()) {
                        System.out.println();
                        System.out.print(player.getName() + ", it's your turn to roll! Ready? ");
                        scan.nextLine();
                        unmatched = true;
                        while (unmatched) {
                            System.out.println();
                            rollResults[currPlayer] = banker.roll(dice);
                            System.out.println(player.getName() + " rolls a " + dice[0] + ", a " + dice[1] + ", and a " + dice[2] + "!");
                            unmatched = rollResults[currPlayer].getCondition().equals("unmatched");
                            if (unmatched) {
                                System.out.println("3 unmatched dice, re-rolling...");
                            }
                        }
                        if (rollResults[currPlayer].isWin()) {
                            if (rollResults[currPlayer].getCondition().equals("triple")) {
                                System.out.println("3 matching dice! " + player.getName() + " wins the round, and receives his wager of " + player.getWager());
                            } else {
                                System.out.println("A 4, a 5, and a 6! " + player.getName() + " wins the round, and receives his wager of " + player.getWager());
                            }

                            player.addChips(player.getWager());
                            banker.subtractChips(player.getWager());
                        } else if (rollResults[0].isLose()) {
                            System.out.println("A 1, a 2, and a 3! " + player.getName() + " loses the round, and loses his wager of " + player.getWager() + " to the banker.");

                            player.subtractChips(player.getWager());
                            banker.addChips(player.getWager());
                        } else {
                            // handle other conditions
                            player.setScore(rollResults[0].getScore());
                            System.out.println(player.getName() + " rolls a score of " + player.getScore());
                            if (player.getScore() < banker.getScore()) {
                                System.out.println(player.getName() + " falls short of the banker's score of " + banker.getScore() + ", and loses their wager of " + player.getWager());
                                player.subtractChips(player.getWager());
                                banker.addChips(player.getWager());
                            } else {
                                System.out.println(player.getName() + " passes the banker's score of " + banker.getScore() + ", and receives their wager of " + player.getWager());
                                player.addChips(player.getWager());
                                banker.subtractChips(player.getWager());
                            }
                        }
                    }
                    currPlayer++;
                }
            }
        }
    }

    private void printGameStatus() {
        System.out.println();
        System.out.println("The banker has " + banker.getChips() + " chips.");
        for (Player player : players) {
            if (player.inGame()) {
                System.out.println(player.getName() + " has " + player.getChips() + " chips.");
            } else {
                System.out.println(player.getName() + " is out of the game!");
            }
        }
    }
}
