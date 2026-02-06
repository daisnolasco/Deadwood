import java.util.*;
//Partially implemented Deadwood Class : Runs main game loop, Keeps track of Days, handles player turn, order and input
public class Deadwood {
    private ArrayList<Player> players = new ArrayList<Player>();
    Board board;
    private Actions action;
    private int playerIndex = 0;
    private int totalDays = 4;
    private int currentDay = 1;
    public boolean gameOver = false;
    private int totalPlayers;
    // default rank, dollars,day
    int rank = 1;
    int credits = 0;
    int dollars = 0;

    public static void main(String[] args) {
        Deadwood game = new Deadwood();
        game.runDeadwood();

    }

    public void setupGame() {
        Scanner input = new Scanner(System.in);
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
            totalDays = 3;

        } else if (totalPlayers == 5) {
            credits = 2;

        } else if (totalPlayers == 6) {
            credits = 4;
        } else if (totalPlayers == 7 || totalPlayers == 8) {
            rank = 2;

        }
        // Player enters name and added to players list
        for (int i = 1; i <= totalPlayers; i++) {
            System.out.println("Enter Name for Player " + i);
            String playerName = input.nextLine();
            players.add(new Player(playerName, rank, credits, dollars, null));
        }input.close();

    }
    // main Game loop, loops through each player in list

    public void runDeadwood() {
        System.out.println("Welcome to DeadWood");
        displayRules();
        setupGame();
        

        while (!gameOver) {
         // main Game loop, loops through each player in list until currentdays=totalDays(gameOver) or player quits game
            gameOver=true;
   
        }

    }

    public void validateAction(String playerInput) {
        /*
         * switch case for game actions that are validated
        and implemented through the  action class
         */

    }
    public void displayRules(){

    }

    public void displayPlayerInfo() {
        // display current player info

    }

    public void displayBoardInfo() {
        // display day ,location, all playerInfo ,scoreboard

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
        return gameOver;
    }

    public void endOfDay() {
        // checks if day is over based on remaining scenes on board
        // current day is incremented and board sets up for new day
      

    }

    public void endOfGame() {
        // * displays winner and player scores
        // ends game loop

    }

}
