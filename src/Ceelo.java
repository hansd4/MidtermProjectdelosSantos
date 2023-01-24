import java.util.Scanner;

/**
 * A class designed to play the dice game, Cee-lo
 *
 * @author Hans de los Santos
 */
public class Ceelo {
    // object variables
    /** The players of the game */
    private Player[] players;
    /** The banker of the game */
    private Banker banker;
    /** A scanner for user input */
    private Scanner scan;
    /** A list of 3 dice to be rolled throughout the game */
    private Die[] dice;
    /** A list of roll results, where index 0 corresponds to the banker's roll outcome, and indices 1 through 3 correspond with the corresponding player */
    private RollResult[] rollResults;

    // controller variables
    /** Whether the program should be running */
    private boolean on;
    /** Whether the user's main menu option was invalid, i.e. not a whole number between 1 and 3 */
    private boolean invalidMenuOption;
    /** Whether the current round should be running */
    private boolean gameRunning;
    /** Whether the most recent dice roll resulted in an unmatched outcome, i.e. no triple or double rolls, no 456 and no 123 */
    private boolean unmatched;
    /** Whether the player's wager was invalid, i.e. not between 0 and their current chip amount */
    private boolean invalidWager;
    /** Whether the player's round option was invalid, i.e. not a whole number between 1 and 3 */
    private boolean invalidRoundOption;
    /** Whether the player is yet to choose to roll from the round menu */
    private boolean hasntRolled;
    /** Whether the player's game over menu option was invalid, i.e. not a "y" or "n" */
    private boolean invalidFinalOption;

    // high score variables
    /** The greatest score achieved by any player in any game */
    private int highScore;
    /** The holder of the greatest score achieved by any player in any game */
    private Player highScorer;

    /**
     * Instantiate a Ceelo object, initialize all variables to default values
     */
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

    /**
     * Runs the game. Contains all the logic of the game.
     */
    public void play() {
        System.out.println("Welcome to the Midterm Project"); // Welcome to the Midterm Project

        for (int i = 0; i < 3; i++) {
            System.out.print("Player " + (i + 1) + ", enter your name: "); // Welcome all players and initialize them into the player list with the proper naming
            players[i] = new Player(scan.nextLine());
            System.out.println("Welcome to the game, " + players[i].getName());
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("             CEELO            ");

        while (on) { // Main Program Loop
            int choice = -1;
            invalidMenuOption = true;
            while (invalidMenuOption) {
                System.out.println();
                System.out.println("Main Menu"); // Main Menu
                System.out.println("1. Play");
                System.out.println("2. View High Score");
                System.out.println("3. Quit");
                System.out.println();
                System.out.print("Enter a choice: ");
                choice = scan.nextInt();
                invalidMenuOption = !(choice >= 1 && choice <= 3);
                if (invalidMenuOption) { // Loop until user enters valid menu choice
                    System.out.println("Invalid choice! Please choose either 1, 2 or 3.");
                }
            }
            if (choice == 1) { // Play Game
                gameRunning = true;
                while (gameRunning) { // Main Game Loop
                    printGameStatus();
                    System.out.println();
                    for (Player player : players) {
                        if (player.inGame()) {
                            invalidWager = true;
                            while (invalidWager) { // Loop until player has made a valid wager (between 0 and their chips)
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
                    while (unmatched) { // Loop until the banker rolls an outcome besides unmatched
                        System.out.println();
                        rollResults[0] = banker.roll(dice);
                        System.out.println("The banker rolls a " + dice[0] + ", a " + dice[1] + ", and a " + dice[2] + "!");
                        unmatched = rollResults[0].getCondition().equals("unmatched");
                        if (unmatched) {
                            System.out.println("3 unmatched dice, re-rolling...");
                        }
                    }
                    if (rollResults[0].isWin()) { // If the banker instantly wins
                        if (rollResults[0].getCondition().equals("triple")) { // If the banker rolled 3 matching dice
                            System.out.println("3 matching dice! The banker wins this round, all wagers go to them.");
                        } else { // If the banker rolled a four-five-six
                            System.out.println("A 4, a 5, and a 6! The banker wins this round, all wagers go to them.");
                        }

                        for (Player player : players) { // Handle losses to players and notify players of their loss
                            if (player.inGame()) {
                                System.out.println(player.getName() + " loses " + player.getWager() + " chips to the banker.");
                                banker.addChips(player.getWager());
                                player.subtractChips(player.getWager());
                            }
                        }
                    } else if (rollResults[0].isLose()) { // If the banker instantly loses
                        System.out.println("A 1, a 2, and a 3! The banker loses this round, all wagers doubled!");

                        for (Player player : players) { // Handle gains to players and notify them of their chip gains
                            if (player.inGame()) {
                                System.out.println(player.getName() + " receives " + player.getWager() + " chips from the banker.");
                                banker.subtractChips(player.getWager());
                                player.addChips(player.getWager());
                            }
                        }
                    } else { // Banker rolled double, generated a score
                        banker.setScore(rollResults[0].getScore());
                        System.out.println("The banker rolls a score of " + banker.getScore());
                        int currPlayer = 1;
                        for (Player player : players) {
                            if (player.inGame()) {
                                invalidRoundOption = true;
                                unmatched = true;
                                hasntRolled = true;
                                int turnChoice;
                                while (invalidRoundOption || unmatched || hasntRolled) { // Continue to show player turn options until player has rolled an outcome
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
                                    if (invalidRoundOption) { // Loop back to turn menu if player entered number not between 1 and 3
                                        System.out.println("Invalid choice! Please choose 1, 2 or 3.");
                                    } else {
                                        hasntRolled = turnChoice != 1;
                                        System.out.println();
                                        if (hasntRolled) { // Loop back to menu after checking
                                            if (turnChoice == 2) { // Check Chip Balance
                                                System.out.println("You have: " + player.getChips() + " chips left.");
                                            } else { // Check Wager
                                                System.out.println("This round, you wagered: " + player.getWager() + " chips.");
                                                System.out.println("Winning this round would get you to: " + (player.getWager() + player.getChips()) + " chips.");
                                                if (player.getChips() - player.getWager() > 0) {
                                                    System.out.println("Losing this round would get you to: " + (player.getChips() - player.getWager()) + " chips.");
                                                } else {
                                                    System.out.println("Losing this round would eliminate you from the game!");
                                                }
                                            }
                                        } else { // Roll
                                            rollResults[currPlayer] = player.roll(dice);
                                            System.out.println(player.getName() + " rolls a " + dice[0] + ", a " + dice[1] + ", and a " + dice[2] + "!");
                                            unmatched = rollResults[currPlayer].getCondition().equals("unmatched");
                                            if (unmatched) { // Loop back to menu after rolling 3 unmatched dice
                                                System.out.println("Unmatched dice. Back to you, " + player.getName() + ".");
                                            }
                                        }
                                    }
                                }
                                if (rollResults[currPlayer].isWin()) { // If player instantly wins regardless of score
                                    if (rollResults[currPlayer].getCondition().equals("triple")) { // If player rolled 3 matching rolls
                                        System.out.println("3 matching dice! " + player.getName() + " wins the round, and receives their wager of " + player.getWager());
                                    } else { // If player rolled a four-five-six
                                        System.out.println("A 4, a 5, and a 6! " + player.getName() + " wins the round, and receives their wager of " + player.getWager());
                                    }

                                    player.addChips(player.getWager()); // Handle player chip gain and banker chip loss
                                    banker.subtractChips(player.getWager());
                                } else if (rollResults[currPlayer].isLose()) { // If player instantly loses regardless of score
                                    System.out.println("A 1, a 2, and a 3! " + player.getName() + " loses the round, and loses their wager of " + player.getWager() + " to the banker.");

                                    player.subtractChips(player.getWager()); // Handle player chip loss and banker chip gain
                                    banker.addChips(player.getWager());
                                } else { // If player generated a score from their roll
                                    player.setScore(rollResults[currPlayer].getScore());
                                    System.out.println(player.getName() + " rolls a score of " + player.getScore());
                                    if (player.getScore() < banker.getScore()) { // If the player scored less than the banker
                                        System.out.println(player.getName() + " falls short of the banker's score of " + banker.getScore() + ", and loses their wager of " + player.getWager());
                                        player.subtractChips(player.getWager()); // Handle player chip loss and banker chip gain
                                        banker.addChips(player.getWager());
                                    } else { // If the player scored more than the banker
                                        System.out.println(player.getName() + " passes the banker's score of " + banker.getScore() + ", and receives their wager of " + player.getWager());
                                        player.addChips(player.getWager()); // Handle player chip gain and banker chip loss
                                        banker.subtractChips(player.getWager());
                                    }
                                }
                            }
                            currPlayer++;
                        }
                    }

                    if (banker.getChips() <= 0) { // If the banker has lost all their chips (round end)
                        System.out.println("The players have broken the bank! Final chips: "); // Display all chips & determine winner (the greatest number of chips)
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
                        invalidFinalOption = true;
                        gameRunning = false;
                        String finalChoice = "";
                        while (invalidFinalOption) { // Loop until user inputs "y" or "n" as an option
                            System.out.print("Game over. Play again? (y/n): ");
                            finalChoice = scan.nextLine();
                            invalidFinalOption = !(finalChoice.equals("y") || finalChoice.equals("n"));
                            if (invalidFinalOption) {
                                System.out.println("Invalid choice. Please enter either \"y\" or \"n\"!");
                            }
                        }
                        if (finalChoice.equals("y")) { // Play again, handle high score, reset chips and loop back to Main Menu
                            if (winner.getChips() > highScore) {
                                highScorer = winner;
                                highScore = winner.getChips();
                            }

                            banker.setChips(1000);
                            for (Player player : players) {
                                player.setChips(100);
                            }
                        } else { // Quit
                            on = false;
                        }
                    } else if (allPlayersLost()) { // Banker wins, all players left without chips (round end)
                        System.out.println("Every player has lost the game. The banker wins!");
                        invalidFinalOption = true;
                        gameRunning = false;
                        String finalChoice = "";
                        while (invalidFinalOption) { // Loop until user inputs "y" or "n" as an option
                            System.out.print("Game over. Play again? (y/n): ");
                            finalChoice = scan.nextLine();
                            invalidFinalOption = !(finalChoice.equals("y") || finalChoice.equals("n"));
                            if (invalidFinalOption) {
                                System.out.println("Invalid choice. Please enter either \"y\" or \"n\"!");
                            }
                        }
                        if (finalChoice.equals("y")) { // Play again, reset chips and loop back to Main Menu
                            banker.setChips(1000);
                            for (Player player : players) {
                                player.setChips(100);
                            }
                        } else { // Quit
                            on = false;
                        }
                    }
                }
            } else if (choice == 2) { // View High Score
                System.out.println();
                if (highScorer == null) { // Checks if there's a high scorer yet (averting NullPointerException)
                    System.out.println("No high score yet. Play a game (where a player wins) first!"); // If no high score, print warning
                } else {
                    System.out.println("Highest score: " + highScorer.getName() + ", with " + highScore + " chips."); // If high score, display it
                }
            } else { // Quit Program
                on = false;
            }
        }
    }

    /**
     * Prints the current game status, i.e. how many chips the banker and the players have, if any.
     */
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

    /**
     * Returns whether the banker has won the round, i.e. no players are left in the game (with more than 0 chips)
     *
     * @return Whether the banker has won the round
     */
    private boolean allPlayersLost() {
        for (Player player : players) {
            if (player.inGame()) {
                return false;
            }
        }
        return true;
    }
}
