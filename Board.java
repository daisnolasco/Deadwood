import java.util.*;

public class Board {
    private static final int totalNumSets = 10;
    private Room trailers;
    private Room castingOffice;
    private int activeScenes;
    private int dayCount = 1;
    private int totalDays = 4;
    private Map<String, Room> rooms;
    private List<Scene> sceneDeck;

    public Board() {
        // Initialize Board rooms and scenecards
        rooms = new HashMap<>();
        sceneDeck = new ArrayList<>();
        // create board ,find the adjacency of each room and deal scene cards
        createBoard();

        shuffleDealScene();

    }

    private void createBoard() {
        // adds rooms to board and shot counters
        ParseXML parser = new ParseXML();
        try {
            rooms = parser.readBoardData(parser.getDocFromFile("board.xml"));
            sceneDeck = parser.readCardData(parser.getDocFromFile("cards.xml"));
            trailers = rooms.get("trailer");
            castingOffice = rooms.get("office");
        } catch (Exception e) {
            System.out.println("Error reading board data: " + e.getMessage());
        }

    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }

    public int getDayCount() {
        return dayCount;
    }

    private void shuffleDealScene() {
        // shuffles scenes in random order then distrutes one in every room
        Collections.shuffle(sceneDeck);
        int index = 0;
        for (Room room : getAllSets()) {
            if (index < sceneDeck.size()) {
                room.setCurrentScene(sceneDeck.get(index));
                index++;
            }
        }
        activeScenes = totalNumSets;

    }

    // day ends when there is only 1 scene left
    public boolean isDayOver() {
        return activeScenes <= 1;

    }

    public boolean isGameOver() {
        return dayCount > totalDays;

    }

    // remove singular scene from room once scene is wrapped
    public void removeScene(Room setRoom) {
        if (setRoom != null && setRoom.hasActiveScene()) {
            setRoom.removeScene();
            activeScenes--;
        }

    }

    // remove all scenes for a new day
    public void clearAllScene() {
        for (Room room : rooms.values()) {

            room.setCurrentScene(null);

        }
        activeScenes = 0;

    }

    // sets board up for a new day
    public void setupNewday(List<Player> players) {
        dayCount++;
        if (!isGameOver()) {
            clearAllScene();
            shuffleDealScene();
            moveToTrailer(players);
            System.out.println("New Day : " + dayCount + "/" + totalDays);
        }

    }

    public Player removePlayer(List<Player> players, int playerIndex) {
        Player p = players.get(playerIndex);
        System.out.println(p.getPlayerName() + " Has left the game");
        if (p.getCurrentRoom() != null) {
            p.getCurrentRoom().removePlayer(p);
           

        }
         players.remove(playerIndex);
                return p;

    }

    // all rooms including trailers and casting office
    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms.values());
    }

    // returns only sets with scenes (not trailer or cassting office)
    public List<Room> getAllSets() {
        List<Room> sets = new ArrayList<>();
        for (Room room : rooms.values()) {
            if (room.isSet()) {
                sets.add(room);
            }
        }
        return sets;
    }

    public void moveToTrailer(List<Player> players) {
        for (Player player : players) {
            if (player.getCurrentRoom() != null) {
                player.getCurrentRoom().removePlayer(player);
            }

            player.setCurrentRoom(trailers);
            trailers.addPlayer(player);
            player.leaveRole();
        }
    }
    public void moveToOffice(Player player){
        Room currentRoom=player.getCurrentRoom();
        if(!currentRoom.getRoomName().equalsIgnoreCase("office")){
            Room office =getCastingOffice();
            currentRoom.removePlayer(player);
            player.setCurrentRoom(office);
            office.addPlayer(player);
            System.out.println("You have been moved to Casting Office ");

        }
    }

    

    public void displayPlayers(List<Player> players) {
        System.out.println("Total players: " + players.size());
        System.out.println("Players :");
        for (Player player : players) {

            player.displayPlayerInfo();

        }

    }

    public void displayBoardInfo(List<Player> players) {
        System.out.println("Days remaining : " + dayCount + "/" + totalDays);
        System.out.println("Active Scenes remaing " + activeScenes);
        System.out.println("Total Players :" + players.size());
    }

    public void displayScore(List<Player> players) {

        int winnerScore = 0;
        Player winner = null;
        // print score of all players and determine winner
        for (Player player : players) {
            int score = player.playerScore();
            System.out.println(player.getPlayerName() + "Total Score : " + score + " points");
            if (score > winnerScore) {
                winnerScore = score;
                winner = player;
            }
            

        }
        if (winner != null) {
                System.out.println("Winner: " + winner.getPlayerName());
            } else {
                System.out.println("No winner ");
            }
    }

    public Room getRoom(String roomName) {
        return rooms.get(roomName);
    }

    public Room getTrailers() {
        return trailers;

    }

    public Room getCastingOffice() {
        return castingOffice;

    }

    public int getActiveScenes() {
        return activeScenes;
    }

}