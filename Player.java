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
    resetTokens();
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

  public void addCredits(int amount) {
    this.credits += amount;
  }

  // Calc final score to determine winner
  public int playerScore() {
    return dollars + credits + (5 * rank);

  }

  // Displaying player info
  public void displayPlayerInfo() {
    System.out.println(playerName + ":");
    System.out.println("Rank : " + rank);
    System.out.println("Credits : " + credits);
    System.out.println("Dollars : " + dollars);
    System.out.println("Rehearsal Tokens : " + rehearsalTokens);
    if (currentRoom != null) {
      System.out.println("set: " + currentRoom.getRoomName());
    } else {
      System.out.println("set: none");
    }
    if (currentRole != null) {
      System.out.println("Role : " + currentRole.getRoleName());
    } else {
      System.out.println("Role : none");
    }

  }

}
