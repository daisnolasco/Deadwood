<<<<<<< HEAD
// Add this to a separate file or at the bottom of CastingOffice.java for testing
class Room {
    private String name;
    
    public Room(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}
=======
import java.util.*;

public class Room {
    private String roomName;
    private int totalShots;
    private int remainingShots;
    private Scene currentScene;
    private List<Role> extraroles = new ArrayList<>();
    private List<Player> playersInRoom = new ArrayList<>();
    private List<Room> adjacentRooms = new ArrayList<>();
    private boolean isSet;

    public Room(String roomName, int totalShots, boolean isSet) {
        this.roomName = roomName;
        this.totalShots = totalShots;
        this.remainingShots = totalShots;
        this.isSet = isSet;
        this.extraroles = new ArrayList<>();
        this.playersInRoom = new ArrayList<>();
        this.adjacentRooms = new ArrayList<>();

    }

    // setters and getters
    public String getRoomName() {
        return roomName;
    }


    // if room is a set or castingoffice/trailer
    public boolean isSet() {
        return isSet;
    }

    // scene and shot details
      public int getShotCounters() {
        return totalShots;
    }

    public int getRemainingCounters() {
        return remainingShots;
    }

    public void removeShotCounter() {
        if (remainingShots > 0) {
            remainingShots--;
        }

    }

    public boolean isSceneComeplete() {
        return remainingShots == 0;
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(Scene scene) {
        this.currentScene = scene;
        this.remainingShots = totalShots;
    }

    public boolean hasActiveScene() {
        return currentScene != null;
    }

    public void removeScene() {
        this.remainingShots = 0;
        this.currentScene = null;

    }  

    // Role methods
    public void addExtraRole(Role extra) {
        extraroles.add(extra);

    }

    public List<Role> getExtraRole() {
        return extraroles;

    }

    public List<Role> getAllRoles() {
        List<Role> allRoles = new ArrayList<>();
        // adds all extra and star roles to set of all roles in room
        allRoles.addAll(extraroles);
        allRoles.addAll(currentScene.getStarRoles());
        return allRoles;

    }

    public List<Role> getAvailibleRoles() {
        List<Role> availibleRoles = new ArrayList<>();
        // for loop ,getting all roles in room and return non occupied roles
        return availibleRoles;
    }

    // adding players to room
    public List<Player> getPlayersInRoom() {
        return playersInRoom;
    }

    public void addPlayer(Player player) {
        playersInRoom.add(player);

    }

    public void removePlayer(Player player) {
        playersInRoom.remove(player);

    }

    // getting adjacent rooms
    public List<Room> getAdjacentRooms() {
        return adjacentRooms;

    }

    public boolean isAdjacent(Room otherRoom) {
        return adjacentRooms.contains(otherRoom);
    }

    public void setAdjacent(List<Room> rooms) {
        this.adjacentRooms = rooms;
    }

    public void addAdjacentRooms(Room room) {
        adjacentRooms.add(room);

    }

    public void resetRoom() {
    

    }

}
>>>>>>> 30f6d5ccaa4b2840c17838f1d493c604e8c186f4
