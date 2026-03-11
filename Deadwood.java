import java.util.*;

import javax.swing.SwingUtilities;

//Controller 
public class Deadwood  {
    private ArrayList<Player> players = new ArrayList<Player>();
    private Board board;
    private Actions action;
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
    private GameView view;
    private final CastingOffice castingOffice = new CastingOffice();
public Deadwood() {
        Board.resetInstance();
        board = Board.getInstance();
        action = new Actions(board);
    }
   public static void main(String[] args) {
        Deadwood controller = new Deadwood();
        BoardLayersListener gui = new BoardLayersListener(controller);
        gui.setVisible(true);
        SwingUtilities.invokeLater(() -> gui.runSetupDialogs());
    }

    public void setView(GameView view) {
        this.view = view;
    }

    // called by view after num players and names are set
    public void setNumPlayers(int numPlayers) {
        this.totalPlayers = numPlayers;

        if (totalPlayers <= 3) {
            totalDays = 3;
            board.setTotalDays(3);
        } else {
            totalDays = 4;
            board.setTotalDays(4);
        }

        if (totalPlayers == 5) {
            credits = 2;
        } else if (totalPlayers == 6) {
            credits = 4;
        } else if (totalPlayers == 7 || totalPlayers == 8) {
            rank = 2;
        }
    }

    public void setupGame(List<String> playerNames) {
        players.clear();
char[] colors = {'b', 'c','g','o','p','r','v','w','y'};
       
        for (int i = 0; i < totalPlayers; i++) {
            String name = playerNames.get(i);
            if (name == null || name.trim().isEmpty()) {
                name = "Player " + (i + 1);
            }

            Player p = new Player(name.trim(), rank, credits, dollars, board.getTrailers());
            p.setColor(colors[i]);
            p.setCurrentRoom(board.getTrailers());

            players.add(p);
        }

        board.moveToTrailer(players);

        playerIndex = 0;
        currentPlayer = players.get(playerIndex);
        currentRoom = currentPlayer.getCurrentRoom();

        if (view != null) {
            view.log("Welcome to Deadwood!");
            view.log(currentPlayer.getPlayerName() + "'s turn");
            view.refreshView();
        }
    
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
//Player buttons called by view 
    public void moveAction(Room targetRoom) {
        boolean moved = action.Move(currentPlayer, targetRoom.getRoomName());
        if (moved) {
            currentRoom = currentPlayer.getCurrentRoom();
            view.log(currentPlayer.getPlayerName()
                    + " moved to " + currentRoom.getRoomName());
            view.refreshView();

            // Offer take role if new room has active scene with roles in range
            if (currentRoom.isSet() && currentRoom.hasActiveScene()
                    && !currentRoom.getAvailibleRoles().isEmpty()) {
                boolean hasAvailableRole = false;
                for (Role r : currentRoom.getAvailibleRoles()) {
                    if (r.getRequiredRank() <= currentPlayer.getRank()) {
                        hasAvailableRole = true;
                        break;
                    }
                }
                if (hasAvailableRole) {
                    view.offerTakeRole();
                } else {
                    nextTurn();
                }
            } else {
                nextTurn();
            }
        } else {
            view.log("Cannot move to " + targetRoom.getRoomName() + ".");
        }
    }

    public void TakeRoleAction(Role role) {
        if (role == null)
            return;
        action.takeRole(currentPlayer, role.getRoleName());
        if (currentPlayer.getCurrentRole() != null) {
            view.log(currentPlayer.getPlayerName()
                    + " took role: " + role.getRoleName());
            view.refreshView();
            nextTurn();
        } else {
            view.log("Could not take role: " + role.getRoleName());
        }
    }

    public void actAction() {
        if (!currentPlayer.isWorking()) {
            view.log("You need a role to act!");
            return;
        }
        action.act(currentPlayer);
        view.log(currentPlayer.getPlayerName() + " acted.");
        view.refreshView();
        nextTurn();
    }

    public void RehearseAction() {
        if (!currentPlayer.isWorking()) {
            view.log("You need a role to rehearse!");
            return;
        }
        if (!action.validateRehearse(currentPlayer)) {
            view.log("Guaranteed success must Act!");
            return;
        }
        action.Rehearse(currentPlayer);
        view.log(currentPlayer.getPlayerName() + " rehearsed. Tokens: "
                + currentPlayer.getRehearsalTokens());
        view.refreshView();
        nextTurn();
    }

    public void skipAction() {
        view.log(currentPlayer.getPlayerName() + " skipped.");
        nextTurn();
    }

    public void quitAction() {
        view.log(currentPlayer.getPlayerName() + " has quit.");
        board.removePlayer(players, playerIndex);

        if (players.isEmpty()) {
            endOfGame();
            return;
        }
        if (playerIndex >= players.size()) {
            playerIndex = 0;
        }
        currentPlayer = players.get(playerIndex);
        currentRoom = currentPlayer.getCurrentRoom();
        view.refreshView();
    }
public void endAction() { 
    endOfGame(); }
    public void displayRules() {

        System.out.println("To Play:\n" + //

                "1. Players start at trailer and on turn can only move from trailer or skip turn \n" + //
                "2. A player is given option to move , take role , upgrade or skip turn when they dont have a role\n" + //
                "3. Player can only  act rehearse or skip turn when they are working a role\n" + //
                "4. On Turn ,you can either enter a Action Name or Number for \n" + //
                "5. game loops through each player and only ends when all days are complete , if all plauyers quit or player enters in \n"
                + //
                "\n" + //
                "User Can Enter these commands on turn:\n" + //
                "\"who\": Active player info\n" + //
                "\n" + //
                "\"board\": display board and player info for all players\n" + //
                "\n" + //
                "\"skip\" : to skip a turn\n" + //
                "\n" + //
                "\"quit\" : to quit playing and remove player\n" + //
                "\n" + //
                "\"end\" : end game at any time an display score");
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
        currentPlayer = players.get(playerIndex);
        currentRoom = currentPlayer.getCurrentRoom();

        if (board.isDayOver()) {
            endOfDay();
            return;
        }

        view.refreshView();
        view.log(currentPlayer.getPlayerName() + "'s turn");

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
        currentPlayer = players.get(playerIndex);
        currentRoom = currentPlayer.getCurrentRoom();
        view.log("Day " + board.getDayCount() + " begins! ");
        view.refreshView();

    }

    public void endOfGame() {
        System.out.println("Game over!");
        view.showEndGame();
        board.displayScore(players);

    }}

  

