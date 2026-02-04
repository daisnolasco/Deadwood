public class Player {
  private String playerName;
  private int credits = 0;
  private int dollars = 0;
  private int rank = 1;
  private int rehearsalTokens = 0;
  private Room currentRoom;
  private Role currentRole;

  public Player(String playerName, int rank, int credits, int dollars, Room currentRoom) {
    this.playerName = playerName;
    this.rank = rank;
    this.credits = credits;
    this.dollars = dollars;
    this.currentRoom = currentRoom;

  }
  /* Player stats getters and setters */

  public String getPlayerName() {
    return playerName;
  }

  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }

  public int getCredits() {
    return credits;
  }

  public void setCredits(int credits) {
    this.credits = credits;
  }

  public int getDollars() {
    return dollars;
  }

  public void setDollars(int dollars) {
    this.dollars = dollars;
  }

  public int getRank() {
    return rank;
  }

  public void setRank(int rank) {
    this.rank = rank;
  }


  public int getRehearsalTokens() {
    return rehearsalTokens;
  }

  public void setReheasalTokens(int rehearsalTokens) {
    this.rehearsalTokens = rehearsalTokens;
  }

  public Role getRole() {
    return currentRole;
  }

  public void setRole(Role currentRole) {
    this.currentRole = currentRole;
  }

  public void setCurrentRole(Room currentRoom) {
    this.currentRoom = currentRoom;
  }

  public Room getRoom() {
    return currentRoom;
  }

  public void setCurrentRoom(Room currentRoom) {
    this.currentRoom = currentRoom;
  }

    public void resetTokens() {
    rehearsalTokens = 0;
    }
  public void displayPlayerInfo(){
    System.out.println(playerName + ":");
    System.out.println("Rank : " + rank );
    System.out.println(" Credits : " + credits  );
    System.out.println("Dollars : " + dollars  );
    System.out.println("Role : " + currentRole  );



    }
    

}
