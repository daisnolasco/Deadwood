public class Role {
    private String roleName;
    private int requiredRank;
    private boolean isRankAvailible;
    Player assignedPlayer;
    public Role(){
        
    }
   public Role(String roleName,int requiredRank){
    this.roleName=roleName;
    this.requiredRank=requiredRank;
    }

}
class Star extends Role {

    public Star(){

    }
    
}
