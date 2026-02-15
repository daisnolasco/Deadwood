import java.util.*;

public class Room {
    private String roomName;
    private int totalShots;
    private int remainingShots;
    private Scene currentScene;
    private List<Role> extraroles = new ArrayList<>();
    private List<Role> availibleExtraroles = new ArrayList<>();
    private List<Player> playersInRoom = new ArrayList<>();
    private List<Room> adjacentRooms = new ArrayList<>();
     List<Role> availibleRoles = new ArrayList<>();
    private boolean isSet;
// Room constructor for trailer and casting office
    public Room(String roomName, boolean isSet) {
        this.roomName = roomName;
        this.isSet = isSet;
    }
//Room constructor for sets 
    public Room(String roomName, int totalShots, boolean isSet) {
        this.roomName = roomName;
        this.totalShots = totalShots;
        this.remainingShots = totalShots;
        this.isSet = isSet;
   

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
        //assign scen and reset shots
        if(isSet){
               this.currentScene = scene;
               if(scene==null){
                this.remainingShots = 0;
               }else{
               this.remainingShots = totalShots;
               }
        }else{
            return;
        }

    }

    public boolean hasActiveScene() {
        return currentScene != null;
    }

    public void removeScene() {
        if(currentScene!=null){
            currentScene.resetScene();
        }
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

    public List<Role> getAvailibleExtraRoles() {
        availibleExtraroles.clear();    
        // for-loop for extra roles in role and returns unoccupied roles
        for (Role extra : getExtraRole()) {
            if (!extra.isOccupied())
                availibleExtraroles.add(extra);
        }
        return availibleExtraroles;
    }

    public List<Role> getAllRoles() {
        List<Role> allRoles = new ArrayList<>();
        // combines star and extra roles
        allRoles.addAll(extraroles);
        if (currentScene != null && currentScene.getStarRoles() != null) {
            allRoles.addAll(currentScene.getStarRoles());
        }

        return allRoles;

    }

    public List<Role> getAvailibleRoles() {
        availibleRoles.clear();
        for (Role roles : getAllRoles()) {
            if (!roles.isOccupied()) {
                availibleRoles.add(roles);
            }

        }
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
   

  

    public void addAdjacentRooms(Room room) {
        adjacentRooms.add(room);

    }

    public void displaySetInfo() {
        System.out.println("Room Name: " + roomName);
        System.out.println("Remaining Shots: " + remainingShots);
        if (currentScene != null) {
            currentScene.displaySceneInfo();
            System.out.println("Extra Roles:");
            for (Role extra : getAvailibleExtraRoles()) {
                extra.displayRole();

            }
        } else {
            System.out.println("Scene is complete");
        }
        

    }

    public void displayNieghbors() {
        System.out.println(getRoomName());
        for(Room neighbors: getAdjacentRooms()){
            System.out.println(neighbors.getRoomName() + neighbors.hasActiveScene());
        }
  
        

    }

}
