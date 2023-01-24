import java.util.Scanner;

public class Ceelo {
    private Player[] players;
    private Banker banker;
    private Scanner scan;
    private Die[] dice;
    private RollResult[] rollResults;

    private boolean on;
    private boolean invalidMenuOption;
    private boolean gameRunning;
    private boolean unmatched;
    private boolean invalidWager;
    private boolean invalidRoundOption;
    private boolean hasntRolled;
    private boolean invalidFinalOption;

    private int highScore;
    private Player highScorer;

    public Ceelo() {
        this.players = new Player[3];
        this.banker = new Banker();
        this.scan = new Scanner(System.in);
        this.dice = new Die[] {new Die(), new Die(), new Die()};
        this.rollResults = new RollResult[4];
        for (int i = 0; i < rollResults.length; i++) {
            rollResults[i] = new RollResult(false, false, "unmatched");
        }

        this.on = true;
        this.invalidMenuOption = true;
        this.gameRunning = true;
        this.unmatched = true;
        this.invalidWager = true;
        this.invalidRoundOption = true;
        this.hasntRolled = true;
        this.invalidFinalOption = true;

        this.highScore = 0;
        this.highScorer = null;
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

        while (on) {
            int choice = -1;
            invalidMenuOption = true;
            while (invalidMenuOption) {
                System.out.println();
                System.out.println("Main Menu");
                System.out.println("1. Play");
                System.out.println("2. View High Score");
                System.out.println("3. Quit");
                System.out.println();
                System.out.print("Enter a choice: ");
                choice = scan.nextInt();
                invalidMenuOption = !(choice >= 1 && choice <= 3);
                if (invalidMenuOption) {
                    System.out.println("Invalid choice! Please choose either 1, 2 or 3.");
                }
            }
            if (choice == 1) {
                gameRunning = true;
                while (gameRunning) {
                    printGameStatus();
                    System.out.println();
                    for (Player player : players) {
                        if (player.inGame()) {
                            invalidWager = true;
                            while (invalidWager) { // !!! check for valid wager (less than what player has)
                                System.out.print(player.getName() + ", enter your wager: ");
                                player.setWager(scan.nextInt());
                                scan.nextLine();
                                invalidWager = !(player.getWager() <= player.getChips() && player.getWager() >= 0);
                                if (invalidWager) {
                                    System.out.println("Invalid wager! Please enter a number between 0 and the number of chips you currently have, " + player.getChips());
                                }
                            }
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
                                System.out.println(player.getName() + " loses " + player.getWager() + " chips to the banker.");
                                banker.addChips(player.getWager());
                                player.subtractChips(player.getWager());
                            }
                        }
                    } else if (rollResults[0].isLose()) {
                        System.out.println("A 1, a 2, and a 3! The banker loses this round, all wagers doubled!");

                        for (Player player : players) {
                            if (player.inGame()) {
                                System.out.println(player.getName() + " receives " + player.getWager() + " chips from the banker.");
                                banker.subtractChips(player.getWager());
                                player.addChips(player.getWager());
                            }
                        }
                    } else {
                        banker.setScore(rollResults[0].getScore());
                        System.out.println("The banker rolls a score of " + banker.getScore());
                        int currPlayer = 1;
                        for (Player player : players) {
                            if (player.inGame()) {
                                invalidRoundOption = true;
                                unmatched = true;
                                hasntRolled = true;
                                int turnChoice;
                                while (invalidRoundOption || unmatched || hasntRolled) {
                                    System.out.println();
                                    System.out.println("It's " + player.getName() + "'s turn!");
                                    System.out.println("1. Roll");
                                    System.out.println("2. Check Chip Balance");
                                    System.out.println("3. Check Wager");
                                    System.out.println();
                                    System.out.print("Enter a choice: ");
                                    turnChoice = scan.nextInt();
                                    scan.nextLine();
                                    invalidRoundOption = !(turnChoice >= 1 && turnChoice <= 3);
                                    if (invalidRoundOption) {
                                        System.out.println("Invalid choice! Please choose 1, 2 or 3.");
                                    } else {
                                        hasntRolled = turnChoice != 1;
                                        System.out.println();
                                        if (hasntRolled) {
                                            if (turnChoice == 2) {
                                                System.out.println("You have: " + player.getChips() + " chips left.");
                                            } else {
                                                System.out.println("This round, you wagered: " + player.getWager() + " chips.");
                                                System.out.println("Winning this round would get you to: " + (player.getWager() + player.getChips()) + " chips.");
                                                if (player.getChips() - player.getWager() > 0) {
                                                    System.out.println("Losing this round would get you to: " + (player.getChips() - player.getWager()) + " chips.");
                                                } else {
                                                    System.out.println("Losing this round would eliminate you from the game!");
                                                }
                                            }
                                        } else {
                                            rollResults[currPlayer] = player.roll(dice);
                                            System.out.println(player.getName() + " rolls a " + dice[0] + ", a " + dice[1] + ", and a " + dice[2] + "!");
                                            unmatched = rollResults[currPlayer].getCondition().equals("unmatched");
                                            if (unmatched) {
                                                System.out.println("Unmatched dice. Back to you, " + player.getName() + ".");
                                            }
                                        }
                                    }
                                }
                                if (rollResults[currPlayer].isWin()) {
                                    if (rollResults[currPlayer].getCondition().equals("triple")) {
                                        System.out.println("3 matching dice! " + player.getName() + " wins the round, and receives their wager of " + player.getWager());
                                    } else {
                                        System.out.println("A 4, a 5, and a 6! " + player.getName() + " wins the round, and receives their wager of " + player.getWager());
                                    }

                                    player.addChips(player.getWager());
                                    banker.subtractChips(player.getWager());
                                } else if (rollResults[currPlayer].isLose()) {
                                    System.out.println("A 1, a 2, and a 3! " + player.getName() + " loses the round, and loses their wager of " + player.getWager() + " to the banker.");

                                    player.subtractChips(player.getWager());
                                    banker.addChips(player.getWager());
                                } else {
                                    player.setScore(rollResults[currPlayer].getScore());
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

                    if (banker.getChips() <= 0) {
                        System.out.println("The players have broken the bank! Final chips: ");
                        int maxChips = -1;
                        Player winner = null;
                        for (Player player : players) {
                            if (player.inGame()) {
                                System.out.println(player.getName() + ": " + player.getChips() + " chips");
                            } else {
                                System.out.println(player.getName() + ": out of the game!");
                            }
                            if (player.getChips() > maxChips) {
                                maxChips = player.getChips();
                                winner = player;
                            }
                        }
                        System.out.println(winner.getName() + " wins!");
                        gameOver(winner);
                    } else if (allPlayersLost()) {
                        System.out.println("Every player has lost the game. The banker wins!");
                        gameOver();
                    }
                }
            } else if (choice == 2) {
                System.out.println();
                if (highScorer == null) {
                    System.out.println("No high score yet. Play a game (where a player wins) first!");
                } else {
                    System.out.println("Highest score: " + highScorer.getName() + ", with " + highScore + " chips.");
                }
            } else {
                on = false;
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

    private boolean allPlayersLost() {
        for (Player player : players) {
            if (player.inGame()) {
                return false;
            }
        }
        return true;
    }

    private void gameOver(Player winner) {
        invalidFinalOption = true;
        gameRunning = false;
        String finalChoice = "";
        while (invalidFinalOption) {
            System.out.print("Game over. Play again? (y/n): ");
            finalChoice = scan.nextLine();
            invalidFinalOption = !(finalChoice.equals("y") || finalChoice.equals("n"));
            if (invalidFinalOption) {
                System.out.println("Invalid choice. Please enter either \"y\" or \"n\"!");
            }
        }
        if (finalChoice.equals("y")) {
            if (winner.getChips() > highScore) {
                highScorer = winner;
                highScore = winner.getChips();
            }

            banker.setChips(1000);
            for (Player player : players) {
                player.setChips(100);
            }
        } else {
            on = false;
        }
    }

    private void gameOver() {
        invalidFinalOption = true;
        gameRunning = false;
        String finalChoice = "";
        while (invalidFinalOption) {
            System.out.print("Game over. Play again? (y/n): ");
            finalChoice = scan.nextLine();
            invalidFinalOption = !(finalChoice.equals("y") || finalChoice.equals("n"));
            if (invalidFinalOption) {
                System.out.println("Invalid choice. Please enter either \"y\" or \"n\"!");
            }
        }
        if (finalChoice.equals("y")) {
            banker.setChips(1000);
            for (Player player : players) {
                player.setChips(100);
            }
        } else {
            on = false;
        }
    }
}
