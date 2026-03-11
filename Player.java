//Complete imlementation of player 
public class Player {
  private String playerName;
  private int credits;
  private int dollars;
  private int rank;
  private int rehearsalTokens;
  private Room currentRoom;
  private Role currentRole;

  public Player(String playerName, int rank, int credits, int dollars, Room currentRoom) {
    this.playerName = playerName;
    this.rank = rank;
    this.credits = credits;
    this.dollars = dollars;
    this.rehearsalTokens = 0;
    this.currentRoom = currentRoom;
    this.currentRole = null;

  }
  /* Player stats getters */

  public String getPlayerName() {
    return playerName;
  }

  public int getCredits() {
    return credits;
  }

  public int getDollars() {
    return dollars;
  }

  public int getRank() {
    return rank;
  }

  public int getRehearsalTokens() {
    return rehearsalTokens;
  }

  public Room getCurrentRoom() {
    return currentRoom;
  }

  public Role getCurrentRole() {
    return currentRole;
  }
private char color; 

public char getColor() {
    return color;
}

public void setColor(char color) {
    this.color = color;
}
  // Player stats setters //

  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }

  public void setCredits(int credits) {
    this.credits = credits;
  }

  public void setDollars(int dollars) {
    this.dollars = dollars;
  }

  public void setRank(int rank) {
    this.rank = rank;
  }

  public void setRehearsalTokens(int rehearsalTokens) {
    this.rehearsalTokens = rehearsalTokens;
  }

  public void setCurrentRoom(Room currentRoom) {
    this.currentRoom = currentRoom;
  }

  public void setCurrentRole(Role currentRole) {
    this.currentRole = currentRole;
    if (currentRole == null) {
      resetTokens();
    }

  }

  // checks if player is currently working on role
  public boolean isWorking() {
    return currentRole != null;
  }

  public void leaveRole() {
    this.currentRole = null;
    resetTokens();
  }

  // Rehearsal methods
  public void addRehearsalToken() {
    rehearsalTokens++;
  }

  public void resetTokens() {
    rehearsalTokens = 0;
  }

  // Managing funds
  public void addDollars(int amount) {
    this.dollars += amount;
  }

  public void deductDollars(int amount) {
    this.dollars -= amount;
  }

  public void addCredits(int amount) {
    this.credits += amount;
  }

  // Calc final score to determine winner
  public int playerScore() {
    return dollars + credits + (5 * rank);

  }

  // Displaying player info
  public String getDisplayInfo() {
    String roomName = "None";
    if (currentRoom != null) {
      roomName = currentRoom.getRoomName();
    }

    String roleName = "None";
    if (currentRole != null) {
      roleName = currentRole.getRoleName();
    }

    return "Name: " + playerName
        + "\nRank: " + rank
        + "\nDollars: " + dollars
        + "\nCredits: " + credits
        + "\nRehearsal: " + rehearsalTokens
        + "\nRoom: " + roomName
        + "\nRole: " + roleName;
  }public void displayPlayerInfo() {
        System.out.println();
        System.out.println(getDisplayInfo());
    }

}
