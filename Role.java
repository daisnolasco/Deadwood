public class Role {
    private String roleName;
    private String line;
    private int requiredRank;
    private boolean isStarringRole;
    private Player assignedPlayer;

    public Role(String roleName, String line, int requiredRank) {
        this.roleName = roleName;
        this.line = line;
        this.requiredRank = requiredRank;
        this.isStarringRole = isStarringRole;
        this.assignedPlayer = null;

    }

    // Name role name on scenecard
    public String getRoleName() {
        return roleName;

    }

    // getting acting line
    public String getLine() {
        return line;

    }

    // needed rank for on card roles
    public int getRequiredRank() {
        return requiredRank;

    }

    // check if role is starring role
    public boolean isStarringRole() {
        return isStarringRole;
    }

    // get player on that role
    public Player getAssignedPlayer() {
        return assignedPlayer;

    }

    // sets player to role
    public void setAssignedPlayer(Player player) {
        this.assignedPlayer = player;

    }

    // checks if role is availible
    public boolean isOccupied() {
        return assignedPlayer != null;
    }

    // set role to occupied once player has taken role
    public void removePlayerFromRole() {
        this.assignedPlayer = null;
    }

    public void displayRole() {
        System.out.println("Role: " + roleName + ", Rank: " + requiredRank +
                (isStarringRole ? " (Starring)" : " (Extra)"));
    }

}
