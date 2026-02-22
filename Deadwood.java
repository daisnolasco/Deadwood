import java.util.*;

//Partially implemented Deadwood Class : Runs main game loop, Keeps track of Days, handles player turn, order and input
public class Deadwood {
    private ArrayList<Player> players = new ArrayList<Player>();
    Board board;
    Actions action;
    private int playerIndex = 0;
    private int totalDays = 4;
    private int currentDay = 1;
    private Player currentPlayer;
    private Room currentRoom;
    public boolean gameOver = false;
    private int totalPlayers;
    protected Scanner input = new Scanner(System.in);
    String playerInput;
    // default rank, dollars,day
    int rank = 1;
    int credits = 0;
    int dollars = 0;

    public static void main(String[] args) {
        Deadwood game = new Deadwood();
        game.runDeadwood();

    }

    public void setupGame() {
        // getting numnber of players
        while (true) {
            System.out.println("Enter Number of Players (2-8).");
            totalPlayers = input.nextInt();
            if (totalPlayers < 2 || totalPlayers > 8) {
                System.out.println("Not valid Number ,Enter Number of Players (2-8).");

            } else {
                break;
            }

        }
        input.nextLine();
        // setting up player and game details based on rank
        if (totalPlayers == 2 || totalPlayers == 3) {
            board.setTotalDays(3);

        } else if (totalPlayers == 5) {
            credits = 2;

        } else if (totalPlayers == 6) {
            credits = 4;
        } else if (totalPlayers == 7 || totalPlayers == 8) {
            rank = 2;

        }
        // Player enters name and added to players list
        for (int i = 1; i <= totalPlayers; i++) {
            System.out.println("Enter Name for Player " + i + ":");
            String playerName = input.nextLine();
            players.add(new Player(playerName, rank, credits, dollars, board.getTrailers()));

        }
        board.moveToTrailer(players);

    }

    // helper method to display all players and their locations on the board, used
    // for testing and debugging
    private void displayAllPlayersLocations() {
        System.out.println("\n--- Player Locations ---");
        for (Player p : players) {
            String activeMark = (p == currentPlayer) ? " (active)" : "";
            String roomName = (p.getCurrentRoom() != null) ? p.getCurrentRoom().getRoomName() : "Unknown";
            System.out.println("- " + p.getPlayerName() + activeMark + " : " + roomName);
        }
        System.out.println("------------------------");
    }
    // main Game loop, loops through each player in list

    public void runDeadwood() {
        board = new Board();
        action = new Actions(board);

        System.out.println("Welcome to DeadWood");
        displayRules();
        setupGame();

        while (!board.isGameOver() && !players.isEmpty()) {
            currentPlayer = getCurrentPlayer();
            currentRoom = currentPlayer.getCurrentRoom();
            currentPlayer.displayPlayerInfo();
            System.out.println();
            displayPlayerOptions(currentPlayer);
            playerInput = input.nextLine().trim().toLowerCase();
            // handling non action commands , continue turn after command
            if (playerInput.equals("who")) {
                currentPlayer.displayPlayerInfo();
                continue;
            }
            if (playerInput.equals("where")) {
                currentRoom.displaySetInfo();
                continue;
            }
            if (playerInput.equals("players") || playerInput.equals("status")) {
                displayAllPlayersLocations();
                continue;
            }
            if (playerInput.equals("quit")) {
                board.removePlayer(players, playerIndex);
                continue;
            }

            if (playerInput.equals("end")) {
                endOfGame();
                // calc score
                break;

            }
            if (playerInput.equals("board")) {
                board.displayPlayers(players);

                continue;
            }
            if (playerInput.equals("skip")) {
                nextTurn();
                continue;
            }
            // skipping turn based on displayplayerOption method
            if (currentPlayer.isWorking() && playerInput.equals("3")) {
                nextTurn();
                continue;
            }
            if (!currentRoom.isSet() && playerInput.equals("2")) {
                nextTurn();
                continue;

            }
            if (currentRoom.isSet() && currentRoom.hasActiveScene() && !currentRoom.getAvailibleRoles().isEmpty()
                    && playerInput.equals("4")) {
                nextTurn();
                continue;

            }
            if (currentRoom.isSet() && (!currentRoom.hasActiveScene() || currentRoom.getAvailibleRoles().isEmpty())
                    && playerInput.equals("3")) {
                nextTurn();
                continue;

            }

            if (currentPlayer.isWorking()) {
                playerWorking();

            } else {
                playerNotworking();
            }
            if (board.isDayOver()) {
                endOfDay();
            }
        }

    }

    public void displayPlayerOptions(Player currP) {
        currentRoom = currP.getCurrentRoom();
        if (!currP.isWorking()) {
            if (!currentRoom.isSet()) {
                System.out.println("1| Move , 2| Skip");

            } else if (currentRoom.hasActiveScene() && !currentRoom.getAvailibleRoles().isEmpty()) {
                System.out.println("1| Move , 2| Take role , 3| Upgrade,  4| Skip");

            } else {
                // no active scne and no availible roles left
                System.out.println("1| Move, 2| Upgrade,  3| Skip");

            }

        } else if (currP.isWorking()) {
            System.out.println("1| act , 2 | rehearse, 3| skip");

        }

    }

    public void playerNotworking() {
        if (playerInput.equals("move") || playerInput.equals("1")) {
            System.out.println("You are currently in " + currentRoom.getRoomName());
            System.out.println("Enter Set number or Set name to move to (or skip) :");
            currentRoom.displayNieghbors();
            boolean moved = false;
            while (!moved) {// loop until valid entry is entered or player skips turn
                playerInput = input.nextLine().trim();
                if (playerInput.equalsIgnoreCase("skip"))
                    break;
                moved = action.Move(currentPlayer, playerInput);
            }
            if (moved) {
                Room newRoom = currentPlayer.getCurrentRoom();
                if (newRoom.isSet() && newRoom.hasActiveScene()) {
                    System.out.println("Would you like to take a role (1.| Yes) or ( 2.| no)");
                    playerInput = input.nextLine().trim();
                    if (playerInput.toLowerCase().startsWith("yes") || playerInput.equals("1")) {
                        System.out.println("Enter Role name or number (or skip): ");
                        newRoom.displayRoleOption(currentPlayer.getRank());
                        playerInput = input.nextLine().trim();
                        action.takeRole(currentPlayer, playerInput);

                    }

                }

            }
            nextTurn();

        } else if (playerInput.equalsIgnoreCase("Take Role")
                || playerInput.equals("2") && currentRoom.hasActiveScene()) {
            Room newRoom = currentPlayer.getCurrentRoom();
            System.out.println("Enter Role name or number (or skip): ");
            newRoom.displayRoleOption(currentPlayer.getRank());
            playerInput = input.nextLine().trim();
            action.takeRole(currentPlayer, playerInput);

            nextTurn();

        } else if (playerInput.equalsIgnoreCase("Upgrade")
                || (currentRoom.isSet()) && currentRoom.hasActiveScene() && !currentRoom.getAvailibleRoles().isEmpty()
                        && playerInput.equals("3")
                ||
                (currentRoom.isSet()) && !currentRoom.hasActiveScene() && currentRoom.getAvailibleRoles().isEmpty()
                        && playerInput.equals("2")) {
            action.displayUpgradeOptions(currentPlayer);
            currentRoom = currentPlayer.getCurrentRoom();
            playerInput = input.nextLine().trim();
            // Parse user input (rank and payment method)

            String[] upgrade = playerInput.split(" ");
            if (upgrade.length == 2) {
                action.upgradeRank(currentPlayer, upgrade[0], upgrade[1]);
            } else {
                System.out.println("Enter Rank and Payment type (d| dollars) or (c| credits) ");
            }
            nextTurn();

        } else {// invalid input for non working player
            System.out.println("Please Enter Valid Input");
            displayPlayerOptions(currentPlayer);

        }
    }

    public void playerWorking() {
        if (playerInput.equalsIgnoreCase("act") || playerInput.equals("1")) {
            action.act(currentPlayer);
            nextTurn();
        } else if (playerInput.equals("2") || playerInput.equalsIgnoreCase("rehearse")) {
            action.Rehearse(currentPlayer);
            nextTurn();
        } else {
            System.out.println("Invalid input for working player");
            System.out.println("1| act , 2 | rehearse, 3| skip");
        }

    }

    public void displayRules() {

    }

    // helpful getters

    public Player getCurrentPlayer() {
        return players.get(playerIndex);
    }

    public List<Player> getPlayers() {
        return players;
    }

    private void nextTurn() {
        playerIndex++;
        if (playerIndex >= players.size()) {
            playerIndex = 0;
        }

    }

    public Board getBoard() {
        return board;

    }

    public int getCurrentDay() {
        return currentDay;

    }

    public int getTotalDays() {
        return totalDays;

    }

    public boolean isGameOver() {
        // triggers ends to game loop
        return players.isEmpty();
    }

    public void endOfDay() {
        // checks if day is over based on remaining scenes on board
        // current day is incremented and board sets up for new day
        System.out.println("End of day" + board.getDayCount());
        board.setupNewday(players);
        playerIndex = 0;
        if (board.isGameOver()) {
            endOfGame();
        }

    }

    public void endOfGame() {
        System.out.println("Game over!");
        board.displayScore(players);

    }

}
