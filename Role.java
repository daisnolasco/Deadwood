public class Role {
    private String roleName;
    private String line;
    private int requiredRank;
    private boolean isStar;;
    private Player assignedPlayer;
private int roleX;
    private int roleY;
    public Role(String roleName, String line, int requiredRank,boolean isStar) {
        
        this.roleName = roleName;
        this.line = line;
        this.requiredRank = requiredRank;

        this.isStar=isStar;
        this.assignedPlayer=null;
        

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
        return isStar;
    }

    // get player on that role
    public Player getAssignedPlayer() {
        return assignedPlayer;

    }
public int getRoleX() { return roleX; }
    public int getRoleY() { return roleY; }
    public void setRoleX(int x) { this.roleX = x; }
    public void setRoleY(int y) { this.roleY = y; }
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

   

    public void roleOptions(){
        if(assignedPlayer.getCurrentRole().isStar){
            System.out.println("Starring Roles:");
            displayRole();

        }else{
            System.out.println("Extra Roles:");
            displayRole();

        }
    }
    
    public void displayRole() {
        System.out.println("Role Name: " + roleName +"\t"+"Rank: " + requiredRank);
       


  
    }
      public void displayRoleInfo() {
        System.out.println("Role Name: " + roleName);
        System.out.println("Line: " + line);
        System.out.println("Required Rank: " + requiredRank);

  
    }

}
