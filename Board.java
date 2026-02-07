import java.util.*;

public class Board {
    private static final int totalNumSets = 10;
    private Room trailers;
    private Room castingOffice;

    private int activeScenes;
    private Map<String, Room> rooms = new HashMap<String, Room>();
    private List<Scene> sceneCards = new ArrayList<Scene>();

    public Board() {
        //Initialize  Board rooms and scenecards
        this.rooms=new HashMap<>();
        this.sceneCards=new ArrayList<>();
        //create board ,find the adjacency of each room  and deal scene cards
        createBoard();
        locateAdjacentRooms();
        initlizeSceneDeck();
        shuffleDealScene();

    }

    private void createBoard() {
        // adds rooms to board and shot counters

    }

    private void resetBoard() {
        // removes scene card from the room
    }

    private void shuffleDealScene() {
        // shuffles scenes in random order then distrutes one in every room

    }

    private void initlizeSceneDeck() {
        // creates and contains scene cards

    }

    private void locateAdjacentRooms() {
        // get adjacent rooms based on player current room

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

    // all rooms including trailers and casting office
    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms.values());
    }

    // returns only sets with scenes (not trailer or cassting office)
    public List<Room> getAllSets() {
        List<Room> sets = new ArrayList<>();
        return sets;
    }

    // day ends when there is only 1 scene left
    public boolean isDayOver() {
        return activeScenes > 1;

    }
//remove singular scene from room once scene is wrapped
    public void removeScene(Room setRoom) {

    }
//remove all scenes for a new day 
    public void clearAllScene() {

    }
// sets board up for a new day
    public void setupNewday() {

    }

}